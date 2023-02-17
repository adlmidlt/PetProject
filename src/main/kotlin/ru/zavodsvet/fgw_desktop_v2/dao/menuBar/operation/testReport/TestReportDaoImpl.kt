package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_TEST_REPORT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_TEST_REPORTS
import ru.zavodsvet.fgw_desktop_v2.dao.SV_TEST_REPORT_ADD
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.TestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Реализация объекта доступа к данным операций "Протоколы операции".
 */
class TestReportDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(TestReportDaoImpl::class.java)

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
     * Список протоколов испытаний.
     */
    private var testReportList: ObservableList<TestReportModel> = FXCollections.observableArrayList()

    /**
     * Протокол испытаний.
     */
    private var testReport = TestReportModel()

    /**
     * Найти список протоколов испытаний с фильтрацией.
     *
     * @param ddocn Дата документа с ...
     * @param ddock Дата документа по ...
     * @param dtestn Дата испытаний с ...
     * @param dtestk Дата испытаний по ...
     * @param prodid ИД конструкторского наименования.
     */
    fun findAllTestReportsWithFilter(
        ddocn: String?,
        ddock: String?,
        dtestn: String?,
        dtestk: String?,
        prodid: Int?,
    ): ObservableList<TestReportModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_TEST_REPORTS)
            if (ddocn != null) pstm.setString(1, ddocn) else pstm.setNull(1, Types.DATE)
            if (ddock != null) pstm.setString(2, ddock) else pstm.setNull(2, Types.DATE)
            if (dtestn != null) pstm.setString(3, dtestn) else pstm.setNull(3, Types.DATE)
            if (dtestk != null) pstm.setString(4, dtestk) else pstm.setNull(4, Types.DATE)
            if (prodid != null) pstm.setInt(5, prodid) else pstm.setNull(5, Types.INTEGER)
            val rs = pstm.executeQuery()

            testReportList.removeAll(testReportList)
            while (rs.next()) testReportList.addAll(getTestReport(rs))

            logger.info(
                loggStoredProcedure(
                    SV_SHOW_TEST_REPORTS, "1_ddocn={$ddocn}, 2_ddock={$ddock}, 3_dtestn={$dtestn}, 4_dtestk={$dtestk}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(
                eSQL, "findAllTestReportsWithFilter", "${eSQL.message}\n$INFO_FOR_IT"
            )
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(
                eNPE, "findAllTestReportsWithFilter", "${eNPE.message}\n$INFO_FOR_IT"
            )
        } finally {
            resourcesHandler.freeingDBResource(pstm, "findAllTestReportsWithFilter")
        }

        return testReportList
    }

    /**
     * Получить протокол испытаний по ИД документа.
     *
     * @param idDoc ИД документа.
     */
    fun getTestReportById(idDoc: Int): TestReportModel {
        try {
            pstm = conn.prepareStatement(SV_GET_TEST_REPORT)
            pstm.setInt(1, idDoc)
            val rs = pstm.executeQuery()

            while (rs.next()) testReport = getTestReport(rs)

            logger.info(loggStoredProcedure(SV_GET_TEST_REPORT, "1_idDoc={$idDoc}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getTestReportById", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getTestReportById", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getTestReportById")
        }

        return testReport
    }

    /**
     * Добавить карточку протокола испытаний.
     */
    fun addTestReport(): Int {
        var idDoc = 0
        try {
            pstm = conn.prepareStatement(SV_TEST_REPORT_ADD)
            val rs = pstm.executeQuery()
            while (rs.next()) {
                idDoc = rs.getInt("id")
            }

            logger.info(loggStoredProcedure(SV_TEST_REPORT_ADD, "1_idDoc={${idDoc}}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addTestReport", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addTestReport")
        }

        return idDoc
    }

    /**
     * Получить протокол испытаний.
     */
    private fun getTestReport(rs: ResultSet) = TestReportModel(
        id = rs.getInt("id"),
        num = rs.getString("num"),
        ddoc = rs.getString("ddoc"),
        dtest = rs.getString("dtest"),
        operid = rs.getInt("operid"),
    )
}