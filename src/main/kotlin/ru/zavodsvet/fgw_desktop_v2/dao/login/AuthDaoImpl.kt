package ru.zavodsvet.fgw_desktop_v2.dao.login

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_LOGIN_BY_TN_AND_PS
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SET_LOGIN_PASS
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным авторизации сотрудника.
 */
class AuthDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(AuthDaoImpl::class.java)

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
     * Найти логин по ТН и паролю.
     *
     * @param tabnum ТН.
     * @param pass Пароль.
     */
    fun findLoginByTNAndPS(tabnum: Int, pass: String): Boolean {
        try {
            auth.tabnum = tabnum
            pstm = conn.prepareStatement(SV_LOGIN_BY_TN_AND_PS)
            pstm.setInt(1, auth.tabnum)
            pstm.setString(2, pass)

            val rs = pstm.executeQuery()

            if (rs!!.next()) {
                auth.perif = rs.getInt("perif")
                auth.fio = rs.getString("fio")
            }
            logger.info(loggStoredProcedure(SV_LOGIN_BY_TN_AND_PS, "1_tabnum={$tabnum}, 2_passwd={******}"))
            return true
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "findLoginByTNAndPS", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "findLoginByTNAndPS", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "findLoginByTNAndPS")
        }

        return false
    }

    /**
     * Установить новый пароль.
     *
     * @param tabnum ТН.
     * @param pass Пароль.
     */
    fun setNewPasswd(tabnum: Int, pass: String) {
        try {
            auth.tabnum = tabnum
            pstm = conn.prepareStatement(SV_SET_LOGIN_PASS)
            pstm.setInt(1, auth.tabnum)
            pstm.setString(2, pass)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_SET_LOGIN_PASS, "1_tabnum={${auth.tabnum}}, 2_passwd={******}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "setNewPasswd", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "setNewPasswd", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "setNewPasswd")
        }
    }
}