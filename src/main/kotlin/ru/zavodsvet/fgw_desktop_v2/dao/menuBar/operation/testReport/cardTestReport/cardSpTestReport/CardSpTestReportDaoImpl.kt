package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport.cardSpTestReport

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.common.WRONG_FORMAT_DATA
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_SP_TEST_REPORT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SP_TEST_REPORT_ADD
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SP_TEST_REPORT_DEL
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SP_TEST_REPORT_EDIT
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.cardTestReport.cardSpTestReport.SpecificationTestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным "Спецификации карточки протокола испытаний".
 */
class CardSpTestReportDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardSpTestReportDaoImpl::class.java)

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
     * Спецификация протокола испытаний.
     */
    private var spTestReport = SpecificationTestReportModel()

    /**
     * Добавить спецификацию карточки протокола испытаний.
     *
     * @param spCardTestReport Спецификация карточки испытаний.
     */
    fun addSpTestReport(spCardTestReport: SpecificationTestReportModel) {
        try {
            pstm = conn.prepareStatement(SV_SP_TEST_REPORT_ADD)
            pstm.setInt(1, spCardTestReport.id)
            pstm.setInt(2, spCardTestReport.prodid)
            pstm.setInt(3, spCardTestReport.extend)
            pstm.setString(4, spCardTestReport.dtn)
            pstm.setString(5, spCardTestReport.dtk)
            pstm.execute()

            logger.info(
                loggStoredProcedure(
                    SV_SP_TEST_REPORT_ADD,
                    "1_id={${spCardTestReport.id}}, 2_prodid={${spCardTestReport.prodid}}, " + "3_extend={${spCardTestReport.extend}}, 4_dtn={${spCardTestReport.dtn}}, " + "5_dtk={${spCardTestReport.dtk}}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addSpTestReport", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addSpTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addSpTestReport")
        }
    }

    /**
     * Получить карточку спецификации протокола испытаний.
     *
     * @param spdocid ИД спецификации.
     */
    fun getCardSpTestReport(spdocid: Int): SpecificationTestReportModel {
        try {
            pstm = conn.prepareStatement(SV_GET_SP_TEST_REPORT)
            pstm.setInt(1, spdocid)
            val rs = pstm.executeQuery()

            while (rs.next()) spTestReport = getSpTestReport(rs)

            logger.info(loggStoredProcedure(SV_GET_SP_TEST_REPORT, "1_spdocid={$spdocid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getCardSpTestReport", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getCardSpTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getCardSpTestReport")
        }

        return spTestReport
    }

    /**
     * Обновить карточку спецификации протокола испытаний.
     *
     * @param spdocid ИД спецификации.
     * @param prodid ИД конструкторского наименования.
     * @param extend Кол-во месяцев (срок годности).
     * @param dtn С даты производства.
     * @param dtk По дате производства.
     */
    fun updCardSpTestReport(spdocid: Int, prodid: Int, extend: Int, dtn: String, dtk: String) {
        try {
            pstm = conn.prepareStatement(SV_SP_TEST_REPORT_EDIT)
            pstm.setInt(1, spdocid)
            pstm.setInt(2, prodid)
            pstm.setInt(3, extend)
            pstm.setString(4, dtn)
            pstm.setString(5, dtk)
            pstm.executeUpdate()

            logger.info(
                loggStoredProcedure(
                    SV_SP_TEST_REPORT_EDIT,
                    "1_spdocid={$spdocid}, 2_prodid={$prodid}, 3_extend={$extend}, 4_dtn={$dtn}, 5_dtk={$dtk}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "updCardSpTestReport", "${eSQL.message}\n$WRONG_FORMAT_DATA")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "updCardSpTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "updCardSpTestReport")
        }
    }

    /**
     * Удалить карточку спецификацию протокола испытания.
     *
     * @param spdocid ИД спецификации.
     */
    fun delCardSpTestReport(spdocid: Int) {
        try {
            pstm = conn.prepareStatement(SV_SP_TEST_REPORT_DEL)
            pstm.setInt(1, spdocid)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_SP_TEST_REPORT_DEL, "1_spdocid={$spdocid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delCardSpTestReport", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delCardSpTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delCardSpTestReport")
        }
    }

    /**
     * Получить спецификация протокола испытаний.
     */
    private fun getSpTestReport(rs: ResultSet) = SpecificationTestReportModel(
        id = rs.getInt("id"),
        prodid = rs.getInt("prodid"),
        name = rs.getString("name"),
        extend = rs.getInt("extend"),
        dtn = rs.getString("dtn"),
        dtk = rs.getString("dtk")
    )
}