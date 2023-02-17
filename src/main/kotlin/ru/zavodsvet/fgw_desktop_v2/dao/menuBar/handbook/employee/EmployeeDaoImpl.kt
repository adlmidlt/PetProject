package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.employee

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_PERFORMERS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.employee.EmployeeModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным справочника: "Сотрудники".
 */
class EmployeeDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(EmployeeDaoImpl::class.java)

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
     * Список сотрудников.
     */
    private val employeeList: ObservableList<EmployeeModel> = FXCollections.observableArrayList()


    /**
     * Список работающих сотрудников.
     */
    fun getAllEmployee(): ObservableList<EmployeeModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_PERFORMERS)
            val rs = pstm.executeQuery()

            employeeList.removeAll(employeeList)
            while (rs.next()) employeeList.addAll(getEmployee(rs))

            logger.info(loggStoredProcedure(SV_SHOW_PERFORMERS, ""))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getAllEmployee", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getAllEmployee", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getAllEmployee")
        }

        return employeeList
    }

    /**
     * Получить сотрудника.
     *
     * @param rs Набор данных.
     */
    private fun getEmployee(rs: ResultSet) = EmployeeModel(
        id = rs.getInt("id"),
        fio = rs.getString("fio")
    )
}