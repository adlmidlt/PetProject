package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_OPERATION_BY_ID
import ru.zavodsvet.fgw_desktop_v2.dao.SV_OPERATION_ADD
import ru.zavodsvet.fgw_desktop_v2.dao.SV_OPERATION_DEL
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_OPERATION_PALLETS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.OperationModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Реализация объекта доступа к данным операций "Операции".
 */
class OperationDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(OperationDaoImpl::class.java)

    /**
     * Объект, представляющий предварительно скомпилированный оператор SQL. (Для работы с параметрами.(?))
     */
    private lateinit var pstm: PreparedStatement

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Обработчик ресурсов.
     */
    private var resourcesHandler = ResourcesHandlerUtil()

    /**
     * Список операции по фильтру.
     */
    private val operList: ObservableList<OperationModel> = FXCollections.observableArrayList()

    private lateinit var oper: OperationModel

    /**
     * Вывести все операции согласно фильтру за последние дни.
     *
     * @param oper Операция.
     * @param prodid ИД конструкторского наименования.
     * @param packid ИД варианта упаковки.
     * @param crePerfid ИД создателя операции.
     * @param creDtn Левая граница фильтра по дате создания операции.
     * @param creDtk Правая граница фильтра по дате создания операции.
     * @param ordPerfid ИД создателя ордеров.
     * @param ordDtn  Левая граница фильтра по дате создания ордеров.
     * @param ordDtk Правая граница фильтра по дате создания ордеров.
     */
    fun getAllOperationsPallets(
        oper: Int?,
        prodid: Int?,
        packid: Int?,
        crePerfid: Int?,
        creDtn: String?,
        creDtk: String?,
        ordPerfid: Int?,
        ordDtn: String?,
        ordDtk: String?,
    ): ObservableList<OperationModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_OPERATION_PALLETS)
            if (oper != null) pstm.setInt(1, oper) else pstm.setNull(1, Types.INTEGER)
            if (prodid != null) pstm.setInt(2, prodid) else pstm.setNull(2, Types.INTEGER)
            if (packid != null) pstm.setInt(3, packid) else pstm.setNull(3, Types.INTEGER)
            if (crePerfid != null) pstm.setInt(4, crePerfid) else pstm.setNull(4, Types.INTEGER)
            if (creDtn != null) pstm.setString(5, creDtn) else pstm.setNull(5, Types.DATE)
            if (creDtk != null) pstm.setString(6, creDtk) else pstm.setNull(6, Types.DATE)
            if (ordPerfid != null) pstm.setInt(7, ordPerfid) else pstm.setNull(7, Types.INTEGER)
            if (ordDtn != null) pstm.setString(8, ordDtn) else pstm.setNull(8, Types.DATE)
            if (ordDtk != null) pstm.setString(9, ordDtk) else pstm.setNull(9, Types.DATE)
            val rs = pstm.executeQuery()

            operList.removeAll(operList)
            while (rs.next()) operList.addAll(getOperation(rs))

            logger.info(
                loggStoredProcedure(
                    SV_SHOW_OPERATION_PALLETS,
                    "1_oper={$oper}, 2_prodid={$prodid}, 3_packid={$packid}, 4_cre_perfid={$crePerfid}, 5_cre_dtn={$creDtn}, 6_cre_dtk={$creDtk}, 7_ord_perfid={$ordPerfid}, 8_ord_dtn={$ordDtn}, 9_ord_dtk={$ordDtk}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getAllOperationsPallets", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getAllOperationsPallets", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getAllOperationsPallets")
        }

        return operList
    }

    /**
     * Добавление "Операции".
     *
     * @param oper Номер вида операции.
     * @param dateTimeInventory Дата время инвентаризации.
     */
    fun addOper(oper: Int, dateTimeInventory: String?): Int {
        var operId = 0
        try {
            pstm = conn.prepareStatement(SV_OPERATION_ADD)
            pstm.setInt(1, oper)
            pstm.setInt(2, auth.tabnum)
            if (dateTimeInventory != null) pstm.setString(3, dateTimeInventory) else pstm.setNull(3, Types.DATE)
            val rs = pstm.executeQuery()

            if (rs.next()) operId = rs.getInt("operid")
            logger.info(
                loggStoredProcedure(
                    SV_OPERATION_ADD,
                    "1_kodobj={0}, 2_oper={$oper}, 3_tabnum={${auth.tabnum}}, 4_dateTimeInventory={$dateTimeInventory}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addOper", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addOper", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addOper")
        }

        return operId
    }

    /**
     * Удалить операцию по ИД.
     *
     * @param idOper ИД операции.
     */
    fun delOper(idOper: Int) {
        try {
            pstm = conn.prepareStatement(SV_OPERATION_DEL)
            pstm.setInt(1, idOper)
            pstm.setInt(2, auth.tabnum)

            pstm.execute()
            logger.info(loggStoredProcedure(SV_OPERATION_DEL, "1_idOper={$idOper}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delOper", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delOper", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delOper")
        }
    }

    /**
     * Получить операцию по ИД.
     *
     * @param id ИД операции.
     */
    fun getOperByID(id: Int): OperationModel {
        try {
            pstm = conn.prepareStatement(SV_GET_OPERATION_BY_ID)
            pstm.setInt(1, id)

            val rs = pstm.executeQuery()
            while (rs.next()) {
                oper = getOperation(rs)
            }

            logger.info(loggStoredProcedure(SV_GET_OPERATION_BY_ID, "1_id={$id}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getOperByID", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getOperByID", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getOperByID")
        }

        return oper
    }

    /**
     * Получить операцию.
     *
     * @param rs Набор данных.
     */
    private fun getOperation(rs: ResultSet) = OperationModel(
        id = rs.getInt("id"),
        vidoper = rs.getString("vidoper"),
        dtcreate = rs.getString("dtcreate"),
        perfcreate = rs.getString("perfcreate"),
        dtord = rs.getString("dtord"),
        perford = rs.getString("perford"),
        pieces = rs.getInt("pieces"),
        oper = rs.getInt("oper"),
        dtn = rs.getString("dtn"),
        permit = rs.getString("permit"),
    )
}