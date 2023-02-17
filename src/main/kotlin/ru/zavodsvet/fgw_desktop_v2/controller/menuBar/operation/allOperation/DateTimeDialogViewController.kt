package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateTimeViewController
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil

/**
 * Контроллер диалогового окна: "Формирование операции на дату время".
 */
class DateTimeDialogViewController {
    /**
     * Сцена окна шаблона: Дата время.
     */
    @FXML
    private lateinit var templateDateTimeView: AnchorPane

    /**
     * Контроллер окна шаблона: Дата время.
     */
    @FXML
    private lateinit var templateDateTimeViewController: TemplateDateTimeViewController

    /**
     * Кнопка: "ОК".
     */
    @FXML
    private lateinit var okBtn: Button

    /**
     * Кнопка: "Отмена".
     */
    @FXML
    private lateinit var cancelBtn: Button

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Получить дату время.
     */
    fun getDateTime() = templateDateTimeViewController.getTemplateDateTime()

    /**
     * Подтверждение формирования операции.
     */
    fun confirm(callback: (Boolean) -> Unit) {
        okBtn.setOnAction {
            callback(true)
            helperWithScene.closeStage(it)
        }
        cancelBtn.setOnAction {
            callback(false)
            helperWithScene.closeStage(it)
        }
    }
}