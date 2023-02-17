package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна на привязку пропуска.
 */
class BindPermitDialogViewController : Initializable {
    /**
     * Штрих-код пропуска
     */
    @FXML
    private lateinit var barcodePermitTF: TextField

    /**
     * Номер пропуска.
     */
    @FXML
    private lateinit var numberPermitTF: TextField

    /**
     * Кнопка выбрано "Да".
     */
    @FXML
    private lateinit var yesBtn: Button

    /**
     * Кнопка выбрано "Нет".
     */
    @FXML
    private lateinit var noBtn: Button

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Валидация текстового поля.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    /**
     * Подтверждение привязки пропуска.
     *
     * @param callback Возвращает Да или Нет.
     */
    fun confirm(callback: ((Boolean) -> Unit)) {
        clearFields()

        yesBtn.setOnAction {
            callback(true)
            helperWithScene.closeStage(it)
        }
        noBtn.setOnAction {
            callback(false)
            helperWithScene.closeStage(it)
        }
    }

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        barcodeTFValidation()
        numberTFValidation()
    }

    /**
     * Валидация текстового поля: "Штрих код".
     */
    private fun barcodeTFValidation() {
        barcodePermitTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputNumber(barcodePermitTF, newValue)
        }
    }

    /**
     * Валидация текстового поля: "Номер пропуска".
     */
    private fun numberTFValidation() {
        numberPermitTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputNumber(numberPermitTF, newValue)
        }
    }

    /**
     * Получить штрих-код.
     */
    fun getBarcodePermit(): String {
        return barcodePermitTF.text
    }

    /**
     * Получить номер пропуска.
     */
    fun getNumberPermit(): String {
        return numberPermitTF.text
    }

    /**
     * Очистить все поля.
     */
    private fun clearFields() {
        numberPermitTF.text = ""
        barcodePermitTF.text = ""
    }
}