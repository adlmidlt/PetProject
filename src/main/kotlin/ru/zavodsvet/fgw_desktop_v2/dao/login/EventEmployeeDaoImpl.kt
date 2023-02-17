package ru.zavodsvet.fgw_desktop_v2.dao.login

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.common.TEXT_EVENT_EMPLOYEE_RECORDED
import ru.zavodsvet.fgw_desktop_v2.dao.SV_EVENTS_ADD
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным события сотрудника.
 */
class EventEmployeeDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(EventEmployeeDaoImpl::class.java)

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
     * Добавить событие сотрудника.
     *
     * @param comm Комментарии события(то бишь его действия) сотрудника.
     */
    fun addEventEmployee(comm: String) {
        try {
            pstm = conn.prepareStatement(SV_EVENTS_ADD)
            pstm.setInt(1, auth.tabnum)
            pstm.setString(2, comm)
            pstm.execute()
            logger.info(TEXT_EVENT_EMPLOYEE_RECORDED)
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addEventEmployee", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addEventEmployee", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addEventEmployee")
        }
    }
}
