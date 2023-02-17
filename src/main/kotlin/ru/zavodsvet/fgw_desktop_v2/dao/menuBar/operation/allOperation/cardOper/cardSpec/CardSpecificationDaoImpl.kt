package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.cardOper.cardSpec

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.common.WRONG_FORMAT_DATA
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_SP_OPER_PALLETS_BY_SP_OPER_ID
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SP_OPERATIONS_ADD_V2
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SP_OPERATIONS_EDIT_V2
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper.cardSpec.CardSpecificationModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным: "Карточка спецификации".
 */
class CardSpecificationDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardSpecificationDaoImpl::class.java)

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
     * Карточка операции.
     */
    private lateinit var cardSpec: CardSpecificationModel

    /**
     * Получить спецификацию по ИД спецификации.
     *
     * @param id ИД спецификации.
     */
    fun getSpOperBySpOperID(id: Int): CardSpecificationModel {
        try {
            pstm = conn.prepareStatement(SV_GET_SP_OPER_PALLETS_BY_SP_OPER_ID)
            pstm.setInt(1, id)
            val rs = pstm.executeQuery()

            while (rs.next()) cardSpec = getCardSpec(rs)

            logger.info(loggStoredProcedure(SV_GET_SP_OPER_PALLETS_BY_SP_OPER_ID, "1_id={$id}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getSpOperBySpOperID", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getSpOperBySpOperID", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getSpOperBySpOperID")
        }

        return cardSpec
    }

    /**
     * Добавить: "Спецификации операции".
     *
     * @param cardSpec "Спецификация операции".
     */
    fun addSpOperV2(cardSpec: CardSpecificationModel) {
        try {
            pstm = conn.prepareStatement(SV_SP_OPERATIONS_ADD_V2)
            pstm.setInt(1, cardSpec.operid)
            pstm.setInt(2, cardSpec.packid)
            pstm.setString(3, cardSpec.dtcreate)
            if (cardSpec.locfromid == "") cardSpec.locfromid = "0"
            pstm.setInt(4, cardSpec.locfromid.toInt())
            if (cardSpec.loctoid == "") cardSpec.loctoid = "0"
            pstm.setInt(5, cardSpec.loctoid.toInt())
            pstm.setInt(6, cardSpec.kol)
            pstm.setInt(7, auth.tabnum)
            pstm.execute()

            logger.info(
                loggStoredProcedure(
                    SV_SP_OPERATIONS_ADD_V2,
                    "1_operid={${cardSpec.operid}}, 2_kodobj={0}, 3_packid={${cardSpec.packid}}, " + "4_dtcreate={${cardSpec.dtcreate}}, 5_locfromid={${cardSpec.locfromid}}, " + "6_loctoid={${cardSpec.loctoid}}, 7_kol={${cardSpec.kol}}, 8_tabnum={${auth.tabnum}}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addSpOperV2", "${eSQL.message}\n")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addSpOperV2", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addSpOperV2")
        }
    }

    /**
     * Изменения "Спецификации операции".
     *
     * @param spoper ИД спецификации.
     * @param objid ИД объекта учёта.
     * @param layer Разрез хранения, пример: '2021-10-01'.
     * @param locfrom ИД участка ОТКУДА.
     * @param locto ИД участка КУДА.
     * @param kol Количество.
     */
    fun updSpOperEditV2(spoper: Int, objid: Int, layer: String, locfrom: Int, locto: Int, kol: Int) {
        try {
            pstm = conn.prepareStatement(SV_SP_OPERATIONS_EDIT_V2)
            pstm.setInt(1, spoper)
            pstm.setInt(2, objid)
            pstm.setString(3, layer)
            pstm.setInt(4, locfrom)
            pstm.setInt(5, locto)
            pstm.setInt(6, kol)
            pstm.setInt(7, auth.tabnum)

            pstm.executeUpdate()
            logger.info(
                loggStoredProcedure(
                    SV_SP_OPERATIONS_EDIT_V2,
                    "1_spoper={$spoper}, 2_objid={$objid}, 3_layer={$layer}, 4_locfrom={$locfrom}," + " 5_locto={$locto}, 6_kol={$kol}, 7_tabnum={${auth.tabnum}}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "updSpOperEditV2", "${eSQL.message}\n$WRONG_FORMAT_DATA")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "updSpOperEditV2", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "updSpOperEditV2")
        }
    }

    /**
     * Получить карточку спецификации.
     *
     * @param rs Набор данных.
     */
    private fun getCardSpec(rs: ResultSet) = CardSpecificationModel(
        operid = rs.getInt("operid"),
        spoperid = rs.getInt("spoperid"),
        oper = rs.getInt("oper"),
        packid = rs.getInt("packid"),
        art = rs.getString("art"),
        name = rs.getString("name"),
        color = rs.getString("color"),
        rows = rs.getInt("rows"),
        cou = rs.getInt("cou"),
        locfromid = rs.getString("locfromid"),
        loctoid = rs.getString("loctoid"),
        locfrom = rs.getString("locfrom"),
        locto = rs.getString("locto"),
        dtcreate = rs.getString("dtcreate"),
        dtexpiry = rs.getString("dtexpiry"),
        kol = rs.getInt("kol"),
        existord = rs.getInt("existord"),
        direct = rs.getInt("kol") <= 0,
        dtbal = rs.getString("dtbal")
    )
}