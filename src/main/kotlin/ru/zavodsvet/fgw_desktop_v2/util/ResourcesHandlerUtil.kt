package ru.zavodsvet.fgw_desktop_v2.util

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.common.SQL_CLOSE
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Обработчик ресурсов.
 */
class ResourcesHandlerUtil {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ResourcesHandlerUtil::class.java)

    /**
     * Утилита для обработки ошибок.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Освобождение ресурса БД.
     *
     * @param pstm Объект, представляющий предварительно скомпилированный оператор SQL.
     * @param nameMethod Наименование метода, для сравнения из трассировки.
     */
    fun freeingDBResource(pstm: PreparedStatement, nameMethod: String) {
        try {
            pstm.close()
            logger.info(SQL_CLOSE)
        } catch (eSQL: SQLException) {
            logger.info(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, nameMethod, "${eSQL.message}\n$INFO_FOR_IT")
        }
    }
}