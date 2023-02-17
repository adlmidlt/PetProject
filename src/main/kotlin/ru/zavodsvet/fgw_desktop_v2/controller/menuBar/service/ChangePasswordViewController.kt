package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.service

import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.Tooltip
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.PASSWORDS_NOT_MATCH
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна "Изменить пароль".
 */
class ChangePasswordViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ChangePasswordViewController::class.java)

    /**
     * Поле пароль.
     */
    @FXML
    private lateinit var passwdPF: PasswordField

    /**
     * Подсказка для поля пароль.
     */
    @FXML
    private lateinit var passwdTooltip: Tooltip

    /**
     * Поле повторить пароль.
     */
    @FXML
    private lateinit var repeatPasswdPF: PasswordField

    /**
     * Подсказка для поля повторить пароль.
     */
    @FXML
    private lateinit var repeatPasswdTooltip: Tooltip

    /**
     * Кнопка подтвердить.
     */
    @FXML
    private lateinit var confirmBtn: Button

    /**
     * Кнопка отменить.
     */
    @FXML
    private lateinit var cancelBtn: Button

    /**
     * Оповещение о событии.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Валидация текстового поля.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        passwdTooltip.isAutoHide = true
        passwdTFValidation()

        repeatPasswdTooltip.isAutoHide = true
        repeatTFValidation()
    }

    /**
     * Подтвердить пароль.
     *
     * @param callback Вернуть событие.
     */
    fun confirmPasswd(callback: (Boolean) -> Unit) {
        resetFilledFields()

        confirmBtn.setOnAction { onClickConfirmBtn(it, callback) }
        cancelBtn.setOnAction { onClickCancelBtn(it, callback) }
    }

    /**
     * Получить введенный пароль.
     */
    fun getInputPasswd(): String = passwdPF.text

    /**
     * Событие на нажатие кнопки подтвердить.
     *
     * @param event Событие на нажатие кнопки.
     * @param callback Вернуть событие.
     */
    private fun onClickConfirmBtn(event: Event, callback: (Boolean) -> Unit) {
        val passwordsMatch = isPasswordsMatch(passwdPF.text, repeatPasswdPF.text)
        if (passwordsMatch) {
            helperWithScene.closeStage(event)
            callback(true)
        }
    }

    /**
     * Событие на нажатие кнопки отмена.
     *
     * @param event Событие на нажатие кнопки.
     * @param callback Вернуть событие.
     */
    private fun onClickCancelBtn(event: Event, callback: (Boolean) -> Unit) {
        helperWithScene.closeStage(event)
        callback(false)
    }

    /**
     * Совпадают ли пароли.
     *
     * @param passwdOne Первый пароль.
     * @param passwdTwo Второй пароль.
     */
    private fun isPasswordsMatch(passwdOne: String, passwdTwo: String): Boolean {
        if (passwdOne != passwdTwo) {
            eventAlert.alertWarning(PASSWORDS_NOT_MATCH)
            logger.warn(PASSWORDS_NOT_MATCH)

            return false
        }

        return true
    }

    /**
     * Валидация текстового поля: Пароль.
     */
    private fun passwdTFValidation() {
        passwdPF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputSymbolsWithoutApostrophes(passwdPF, newValue)
            passwdTooltip.text = newValue
        }
    }

    /**
     * Валидация текстового поля: Повторите пароль.
     */
    private fun repeatTFValidation() {
        repeatPasswdPF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputSymbolsWithoutApostrophes(repeatPasswdPF, newValue)
            repeatPasswdTooltip.text = newValue
        }
    }

    /**
     * Сбросить заполненные поля.
     */
    private fun resetFilledFields() {
        passwdPF.text = ""
        repeatPasswdPF.text = ""
    }
}