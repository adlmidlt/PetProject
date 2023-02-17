package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.common.WRONG_FORMAT_DATA
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_SP_TEST_REPORT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_TEST_REPORT_DEL
import ru.zavodsvet.fgw_desktop_v2.dao.SV_TEST_REPORT_EDIT
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.cardTestReport.cardSpTestReport.SpecificationTestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным операций "Карточке протокола операции".
 */
class CardTestReportDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardTestReportDaoImpl::class.java)

    /**
     * Объект, представляющий предварительно скомпилированный оператор SQL. (Для работы с параметрами.(?))
     */
    private lateinit var pstm: PreparedStatement

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Список спецификации протокола испытаний.
     */
    private var spTestReportList: ObservableList<SpecificationTestReportModel> = FXCollections.observableArrayList()

    /**
     * Обработчик ресурсов.
     */
    private var resourcesHandler = ResourcesHandlerUtil()

    /**
     * Найти все спецификации протокола испытаний по ИД документа.
     *
     * @param idDoc ИД документа.
     */
    fun findAllSpTestReportByIdDoc(idDoc: Int): ObservableList<SpecificationTestReportModel> {
        try {
            conn.prepareStatement(SV_SHOW_SP_TEST_REPORT).also { pstm = it }
            pstm.setInt(1, idDoc)
            val rs = pstm.executeQuery()

            spTestReportList.removeAll(spTestReportList)
            while (rs.next()) spTestReportList.addAll(getSpTestReport(rs))

            logger.info(loggStoredProcedure(SV_SHOW_SP_TEST_REPORT, "1_ddocn={$idDoc}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(
                eSQL, "findAllSpTestReportByIdDoc", "${eSQL.message}\n$INFO_FOR_IT"
            )
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(
                eNPE, "findAllSpTestReportByIdDoc", "${eNPE.message}\n$INFO_FOR_IT"
            )
        } finally {
            resourcesHandler.freeingDBResource(pstm, "findAllSpTestReportByIdDoc")
        }

        return spTestReportList
    }

    /**
     * Добавить карточку протокола испытаний.
     */
    fun updTestReport(docid: Int, num: String, ddoc: String, dtest: String) {
        try {
            pstm = conn.prepareStatement(SV_TEST_REPORT_EDIT)
            pstm.setInt(1, docid)
            pstm.setString(2, num)
            pstm.setString(3, ddoc)
            pstm.setString(4, dtest)
            pstm.execute()

            logger.info(
                loggStoredProcedure(
                    SV_TEST_REPORT_EDIT, "1_docid={$docid}, 2_num={$num}, 3_ddoc={$ddoc}, 4_dtest={$dtest}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "updTestReport", "${eSQL.message}\n$WRONG_FORMAT_DATA]")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "updTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "updTestReport")
        }
    }

    /**
     * Удалить карточку протокола испытаний.
     *
     * @param idDoc ИД документа.
     */
    fun delCardTestReport(idDoc: Int) {
        try {
            pstm = conn.prepareStatement(SV_TEST_REPORT_DEL)
            pstm.setInt(1, idDoc)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_TEST_REPORT_DEL, "1_idDoc={$idDoc}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delCardTestReport", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delCardTestReport", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delCardTestReport")
        }
    }

    /**
     * Получить спецификацию протокола испытаний.
     */
    private fun getSpTestReport(rs: ResultSet) = SpecificationTestReportModel(
        id = rs.getInt("id"),
        prodid = rs.getInt("prodid"),
        name = rs.getString("name"),
        extend = rs.getInt("extend"),
        dtn = rs.getString("dtn"),
        dtk = rs.getString("dtk"),
    )
}