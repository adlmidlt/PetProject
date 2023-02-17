package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.cardOper

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.*
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper.CardOperationModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным операций "Карточка операции".
 */
class CardOperationDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardOperationDaoImpl::class.java)

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
     * Список спецификации операции.
     */
    private var specOperList: ObservableList<CardOperationModel> = FXCollections.observableArrayList()

    /**
     * Сформировать ордер по ИД.
     *
     * @param operId ИД операции.
     * @param tabNum Табельный номер.
     */
    fun makeOrderByOperId(operId: Int, tabNum: Int, date: String) {
        try {
            pstm = conn.prepareStatement(SV_MAKE_ORD_BY_OPER_ID)
            pstm.setInt(1, operId)
            pstm.setInt(2, tabNum)
            pstm.setString(3, date)
            pstm.executeUpdate()

            logger.info(
                loggStoredProcedure(
                    SV_MAKE_ORD_BY_OPER_ID, "1_id={$operId}, 2_tabnum={$tabNum} 3_date={$date}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "makeOrderByOperId", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "makeOrderByOperId", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "makeOrderByOperId")
        }
    }

    /**
     * Удалить ордер по ИД операции.
     *
     * @param operId ИД операции.
     */
    fun delOrderByOperId(operId: Int) {
        try {
            pstm = conn.prepareStatement(SV_DEL_ORD_BY_OPER_ID)
            pstm.setInt(1, operId)
            pstm.setInt(2, auth.tabnum)
            pstm.executeUpdate()

            logger.info(loggStoredProcedure(SV_DEL_ORD_BY_OPER_ID, "1_id={$operId}, 2_tabnum={${auth.tabnum}}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delOrderByOperId", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delOrderByOperId", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delOrderByOperId")
        }
    }

    /**
     * Удалить связь операции с авто-пропуском по ИД операции.
     *
     * @param operid ИД операции.
     */
    fun unlinkOperWithAutoSkipPermitByOperId(operid: Int) {
        try {
            pstm = conn.prepareStatement(SV_DEL_RELATION_OPER_AND_AUTO_PERMIT_BY_OPER_ID)
            pstm.setInt(1, operid)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_DEL_RELATION_OPER_AND_AUTO_PERMIT_BY_OPER_ID, "1_operid={$operid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "unlinkOperWithAutoSkipPermitByOperId", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(
                eNPE, "unlinkOperWithAutoSkipPermitByOperId", "${eNPE.message}\n$INFO_FOR_IT"
            )
        } finally {
            resourcesHandler.freeingDBResource(pstm, "unlinkOperWithAutoSkipPermitByOperId")
        }
    }

    /**
     * Добавить связь операции с авто-пропуском.
     *
     * @param operid ИД операции.
     * @param barcode Штрих-код.
     * @param num Номер пропуска.
     */
    fun addLinkOperWithAutoSkipPermit(operid: Int, barcode: String, num: String) {
        try {
            pstm = conn.prepareStatement(SV_SET_RELATION_OPER_AND_AUTO_PERMIT)
            pstm.setInt(1, operid)
            pstm.setString(2, barcode)
            pstm.setString(3, num)
            pstm.execute()

            logger.info(
                loggStoredProcedure(
                    SV_SET_RELATION_OPER_AND_AUTO_PERMIT, "1_operid={$operid}, 2_barcode={$barcode}, 3_num={$num}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addLinkOperWithAutoSkipPermit", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(
                eNPE, "createRelationOperAndAutoPermit", "${eNPE.message}\n$INFO_FOR_IT"
            )
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addLinkOperWithAutoSkipPermit")
        }
    }

    /**
     * Получить список спецификации по ИД операции.
     *
     * @param operId ИД операции.
     */
    fun findAllSpOperByOperID(operId: Int): ObservableList<CardOperationModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_SP_OPER_PALLETS_BY_OPER_ID)
            pstm.setInt(1, operId)
            val rs = pstm.executeQuery()

            specOperList.removeAll(specOperList)
            while (rs.next()) specOperList.addAll(getSpecOper(rs))

            logger.info(loggStoredProcedure(SV_SHOW_SP_OPER_PALLETS_BY_OPER_ID, "1_id={$operId}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "findAllSpOperByOperID", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "findAllSpOperByOperID", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "findAllSpOperByOperID")
        }

        return specOperList
    }

    /**
     * Заполнить спецификацию операции инвентаризации по ID.
     *
     * @param operid ID операции.
     * @param locid ID инвентаризируемого участка хранения.
     */
    fun fillSpByOperIdForInventory(operid: Int, locid: Int) {
        try {
            pstm = conn.prepareStatement(SV_FILL_SP_BY_OPER_ID_FOR_INVENTORY)
            pstm.setInt(1, operid)
            pstm.setInt(2, locid)
            pstm.setInt(3, auth.tabnum)

            pstm.execute()
            logger.info(
                loggStoredProcedure(
                    SV_FILL_SP_BY_OPER_ID_FOR_INVENTORY,
                    "1_operid={$operid}, 2_locid={$locid}, 3_tabnum={${auth.tabnum}}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "fillSpByOperIdForInventory", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "fillSpByOperIdForInventory", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "fillSpByOperIdForInventory")
        }
    }

    /**
     * Удалить "Спецификацию операции" по ИД спецификации.
     *
     * @param idSpec ИД спецификации.
     */
    fun delSpOperDel(idSpec: Int) {
        try {
            pstm = conn.prepareStatement(SV_SP_OPERATIONS_DEL)
            pstm.setInt(1, idSpec)
            pstm.setInt(2, auth.tabnum)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_SP_OPERATIONS_DEL, "1_idSpec={$idSpec}, 2_tabnum={${auth.tabnum}}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delSpOperDel", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delSpOperDel", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delSpOperDel")
        }
    }

    /**
     * Получить спецификацию операции.
     *
     * @param rs Набор данных.
     */
    private fun getSpecOper(rs: ResultSet) = CardOperationModel(
        spoperid = rs.getInt("spoperid"),
        art = rs.getString("art"),
        kdname = rs.getString("kdname"),
        color = rs.getString("color"),
        rows = rs.getInt("rows"),
        cou = rs.getInt("cou"),
        dwh = rs.getString("dwh"),
        locfrom = rs.getString("locfrom"),
        locto = rs.getString("locto"),
        kol = rs.getInt("kol"),
        dtcreate = rs.getString("dtcreate"),
        dtexpiry = rs.getString("dtexpiry"),
        packid = rs.getInt("packid"),
        loctoid = rs.getInt("loctoid"),
        pieces = rs.getInt("pieces"),
        massa = rs.getInt("massa"),
    )
}