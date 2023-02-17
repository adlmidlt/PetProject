package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.END_DAY
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.START_DAY
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateTimeViewController

/**
 * Контроллер окна: "Шаблон выбора периода даты время".
 */
class SelectionDateTimeOfPeriodViewController {
    /**
     * Окно сцены: "Шаблон даты время".
     */
    @FXML
    private lateinit var startTemplateDateTimeView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты время". (начало)
     */
    @FXML
    private lateinit var startTemplateDateTimeViewController: TemplateDateTimeViewController

    /**
     * Окно сцены: "Шаблон даты время".
     */
    @FXML
    private lateinit var endTemplateDateTimeView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты время". (начало)
     */
    @FXML
    private lateinit var endTemplateDateTimeViewController: TemplateDateTimeViewController

    /**
     * Получить дату время начала.
     */
    fun getStartDateTime() = startTemplateDateTimeViewController.getTemplateDateTime()

    /**
     * Получить дату время окончания.
     */
    fun getEndDateTime() = endTemplateDateTimeViewController.getTemplateDateTime()

    /**
     * Установить дату время начала.
     *
     * @param startDateTime Дата начала.
     */
    fun setStartDateTime(startDateTime: String) {
        startTemplateDateTimeViewController.setTemplateDateTime(
            startDateTime.substring(0, 4).toInt(),
            startDateTime.substring(5, 7).toInt(),
            startDateTime.substring(8, 10).toInt(),
            startDateTime.substring(11, 13).toInt(),
            startDateTime.substring(14).toInt(),
        )
    }

    /**
     * Установить дату время окончания.
     *
     * @param endDateTime Дата окончания.
     */
    fun setEndDateTime(endDateTime: String) {
        endTemplateDateTimeViewController.setTemplateDateTime(
            endDateTime.substring(0, 4).toInt(),
            endDateTime.substring(5, 7).toInt(),
            endDateTime.substring(8, 10).toInt(),
            endDateTime.substring(11, 13).toInt(),
            endDateTime.substring(14).toInt(),
        )
    }
}