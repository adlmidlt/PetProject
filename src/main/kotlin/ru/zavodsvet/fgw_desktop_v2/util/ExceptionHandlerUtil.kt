package ru.zavodsvet.fgw_desktop_v2.util

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.APP_CLOSE
import kotlin.system.exitProcess

/**
 * Обработчик исключений.
 */
class ExceptionHandlerUtil {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ExceptionHandlerUtil::class.java)

    /**
     * Оповещение о событии.
     */
    private val eventAlert = EventAlertUtil()

    /**
     * Распечатать в консоль элемент трассировки стека с последующим завершением процесса и записать в журнал
     * логирования.
     *
     * @param ex Обрабатываемая ошибка.
     * @param nameMethod Наименование метода для сравнения из трассировки.
     * @param textAlert Сообщение оповещателя.
     */
    fun printStackTraceElemWithExitProcess(ex: Exception, nameMethod: String, textAlert: String) {
        ex.stackTrace.forEach { stackTrace ->
            if (stackTrace.methodName.equals(nameMethod)) {
                println(stackTrace)
                logger.error(stackTrace)
                eventAlert.alertError(textAlert)
                eventAlert.alertError(APP_CLOSE)
                exitProcess(1)
            }
        }
    }

    /**
     * Распечатать в консоль элемент трассировки стека и записать в журнал логирования.
     *
     * @param ex Обрабатываемая ошибка.
     * @param nameMethod Имя метода.
     * @param textAlert Сообщение оповещателя.
     */
    fun printStackTraceElem(ex: Exception, nameMethod: String, textAlert: String) {
        ex.stackTrace.forEach { stackTrace ->
            if (stackTrace.methodName.equals(nameMethod)) {
                println(stackTrace)
                logger.error(stackTrace)
                eventAlert.alertError(textAlert)
            }
        }
    }
}