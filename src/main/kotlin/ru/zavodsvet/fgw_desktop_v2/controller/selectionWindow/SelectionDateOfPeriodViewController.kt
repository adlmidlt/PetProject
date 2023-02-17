package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.END_DAY
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.START_DAY
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateViewController

/**
 * Контроллер окна: "Шаблон выбора периода даты".
 */
class SelectionDateOfPeriodViewController {
    /**
     * Окно сцены: "Шаблон даты".
     */
    @FXML
    private lateinit var startTemplateDateView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты". (начало)
     */
    @FXML
    private lateinit var startTemplateDateViewController: TemplateDateViewController

    /**
     * Окно сцены: "Шаблон даты".
     */
    @FXML
    private lateinit var endTemplateDateView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты". (окончание)
     */
    @FXML
    private lateinit var endTemplateDateViewController: TemplateDateViewController

    /**
     * Получить дату начала.
     */
    fun getStartDate() = startTemplateDateViewController.getTemplateDate() + START_DAY

    /**
     * Получить окончание даты.
     */
    fun getEndDate() = endTemplateDateViewController.getTemplateDate() + END_DAY

    fun setStartDate(year: Int, mountOfValue: Int, dayOfMonth: Int) {
        startTemplateDateViewController.setTemplateDate(year, mountOfValue, dayOfMonth)
    }

    fun setStartDate(date: String) {
        startTemplateDateViewController.fillParseTemplateDate(date)
    }

    fun setEndDate(date: String) {
        endTemplateDateViewController.fillParseTemplateDate(date)
    }
}