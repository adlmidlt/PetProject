package ru.zavodsvet.fgw_desktop_v2.util

import javafx.scene.control.Alert
import javafx.scene.image.Image
import javafx.stage.Stage
import ru.zavodsvet.fgw_desktop_v2.common.ERROR_TEXT
import ru.zavodsvet.fgw_desktop_v2.common.INFO_TEXT
import ru.zavodsvet.fgw_desktop_v2.common.WARNING_TEXT

/**
 * Оповещение о событии.
 */
class EventAlertUtil {
    /**
     * Общая информация.
     *
     * @param contextText Подставляется текст описания предупреждения.
     */
    fun alertInfo(contextText: String) {
        val info = Alert(Alert.AlertType.INFORMATION)
        addIconInAlertDialog(info)
        info.title = INFO_TEXT
        info.headerText = null
        info.contentText = contextText
        info.showAndWait()
    }

    /**
     * Информация об ошибке.
     *
     * @param contextText Подставляется текст описания ошибки.
     */
    fun alertError(contextText: String) {
        val error = Alert(Alert.AlertType.ERROR)
        addIconInAlertDialog(error)
        error.title = ERROR_TEXT
        error.headerText = null
        error.contentText = contextText
        error.showAndWait()
    }

    /**
     * Предупреждающая информация.
     *
     * @param contextText Подставляется текст описания предупреждения.
     */
    fun alertWarning(contextText: String) {
        val warning = Alert(Alert.AlertType.WARNING)
        addIconInAlertDialog(warning)
        warning.title = WARNING_TEXT
        warning.headerText = null
        warning.contentText = contextText
        warning.showAndWait()
    }

    /**
     * Добавить иконку в левом верхнем углу оповещателя.
     *
     * @param alert Оповещатель.
     */
    private fun addIconInAlertDialog(alert: Alert) {
        val stage = alert.dialogPane.scene.window as Stage
        stage.icons.add(Image("img/logo.png"))
    }
}