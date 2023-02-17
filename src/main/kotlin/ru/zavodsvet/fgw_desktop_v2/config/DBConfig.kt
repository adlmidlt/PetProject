package ru.zavodsvet.fgw_desktop_v2.config

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.DB_CONNECTED
import ru.zavodsvet.fgw_desktop_v2.common.DB_CONN_CLOSED
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import java.sql.DriverManager
import java.sql.SQLException

/**
 * Конфигурация БД.
 */
class DBConfig {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(DBConfig::class.java)

    /**
     * Обработчик исключений.
     */
    private val exceptionHandler = ExceptionHandlerUtil()

    companion object {
        private const val DB_URL = "Секрет"
        private const val DB_DRIVER = "Секрет"
        private const val DB_USERNAME = "Секрет"
        private const val DB_PASSWD = "Секрет"
    }

    /**
     * Создать подключение к БД.
     */
    @Throws(SQLException::class, ClassNotFoundException::class)
    fun dbConnection() {
        try {
            Class.forName(DB_DRIVER)
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWD)

            logger.info(DB_CONNECTED)
        } catch (eCNF: ClassNotFoundException) {
            logger.error(eCNF.message)
            exceptionHandler.printStackTraceElemWithExitProcess(eCNF, "dbConnection", "${eCNF.message}\n$INFO_FOR_IT")
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElemWithExitProcess(eSQL, "dbConnection", "${eSQL.message}\n$INFO_FOR_IT")
        }
    }

    /**
     * Закрыть соединение к БД.
     */
    @Throws(Exception::class, SQLException::class)
    fun dbDisconnect() {
        try {
            if (!conn.isClosed) {
                conn.close()

                logger.info(DB_CONN_CLOSED)
            }
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElemWithExitProcess(eSQL, "dbDisconnect", "${eSQL.message}\n$INFO_FOR_IT")
        }
    }
}