package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.util.*

/**
 * Контроллер диалогово окна "Кол-во под участков хранения".
 */
class QuantitySubLocStoreDialogViewController : Initializable {
    /**
     * Поле для ввода кол-во участков хранения.
     */
    @FXML
    private lateinit var quantitySubLocStoreTF: TextField

    /**
     * Кнопка создать участки хранения.
     */
    @FXML
    private lateinit var createBtn: Button

    /**
     * Кнопка отмены создания участков хранения.
     */
    @FXML
    private lateinit var cancelBtn: Button

    /**
     * Помощник со сценой.
     */
    private var helperScene = HelperWithSceneUtil()

    /**
     * Класс для проверки текстовых полей на валидность.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        quantitySubLocStoreTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputNumber(quantitySubLocStoreTF, newValue)
        }
    }

    /**
     * Кнопка подтверждения создания под участков хранения.
     *
     * @param callback Нажата кнопка или нет.
     */
    fun confirm(callback: ((Boolean) -> Unit)) {
        createBtn.setOnAction { ev ->
            callback(true)
            helperScene.closeStage(ev)
        }
        cancelBtn.setOnAction { ev ->
            callback(false)
            helperScene.closeStage(ev)
        }
    }

    /**
     * Получить кол-во под участков хранения.
     */
    fun getQuantitySubLocStore(): Int {
        return quantitySubLocStoreTF.text.toInt()
    }
}