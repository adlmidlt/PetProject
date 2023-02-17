package ru.zavodsvet.fgw_desktop_v2.controller.login

import javafx.application.Platform
import javafx.event.ActionEvent
import javafx.event.Event
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.App
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateTime
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.logoCompany
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.config.DBConfig
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.service.ChangePasswordViewController
import ru.zavodsvet.fgw_desktop_v2.dao.login.AuthDaoImpl
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна авторизации в приложении сотрудника.
 */
class AuthViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(AuthViewController::class.java)

    /**
     * Окно авторизации.
     */
    @FXML
    private lateinit var authWindow: AnchorPane

    /**
     * Поле: ТН.
     */
    @FXML
    private lateinit var tabNumTF: TextField

    /**
     * Поле: Пароль.
     */
    @FXML
    private lateinit var passwdPF: PasswordField

    /**
     * Кнопка авторизации.
     */
    @FXML
    private lateinit var authBtn: Button

    /**
     * Реализация объекта доступа к данным авторизации сотрудника.
     */
    private val authDaoImpl = AuthDaoImpl()

    /**
     * Оповещение о событии.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Валидация текстового поля.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // Контроллеры окна.
    private var windowControllerChangedPasswd = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        tabnumTFValidation()
        passPFValidation()

        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        authWindow.setOnKeyPressed { onClickAuthBtn(it) }
        authBtn.setOnAction { onClickAuthBtn(it) }
    }

    /**
     * Событие на нажатие кнопки авторизация.
     *
     * @param event Событие.
     */
    private fun onClickAuthBtn(event: Event) {
        if ((event is KeyEvent && event.code == KeyCode.ENTER) || (event is ActionEvent)) {
            try {
                logIn(tabNumTF.text.toInt(), passwdPF.text)
            } catch (eNFE: NumberFormatException) {
                logger.warn(eNFE.message)
                exceptionHandler.printStackTraceElem(eNFE, "onClickAuthBtn", "${eNFE.message}\n$WRONG_FORMAT_DATA")
            }
        }
    }

    /**
     * Авторизоваться.
     *
     * @param tabNum ТН сотрудника.
     * @param passwd Пароль сотрудника.
     */
    private fun logIn(tabNum: Int, passwd: String) {
        val authSuccess = authDaoImpl.findLoginByTNAndPS(tabNum, passwd)
        if (authSuccess) {
            if (passwdPF.text == "") logInWithChangedPasswd() else openMainScene()
        } else {
            eventAlert.alertWarning(INCORRECTLY_ENTERED_LOGIN_OR_PASSWD)
            logger.warn(INCORRECTLY_ENTERED_LOGIN_OR_PASSWD)
        }
    }

    /**
     * Авторизоваться с изменённым паролем.
     */
    private fun logInWithChangedPasswd() {
        windowControllerChangedPasswd =
            NewWindow(windowControllerChangedPasswd).openWindow(CHANGE_PASSWD_VIEW, TITLE_CHANGE_PASSWORD, null)
        val changePasswdController = windowControllerChangedPasswd.controller as ChangePasswordViewController
        changePasswdController.confirmPasswd { confirm ->
            val newPasswd = changePasswdController.getInputPasswd()
            if (confirm) {
                authDaoImpl.setNewPasswd(tabNumTF.text.toInt(), newPasswd)
                openMainScene()
                logger.info(PASSWORD_EDIT)
            }
        }
    }

    /**
     * Валидация текстового поля: ТН.
     */
    private fun tabnumTFValidation() {
        tabNumTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputNumber(tabNumTF, newValue)
        }
    }

    /**
     * Валидация текстового поля: Пароль.
     */
    private fun passPFValidation() {
        passwdPF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputSymbolsWithoutApostrophes(passwdPF, newValue)
        }
    }

    /**
     * Открыть новую сцену.
     */
    private fun openMainScene() {
        authBtn.scene.window.hide() //прячем основное окно
        val fxmlLoader = FXMLLoader(App::class.java.getResource(MAIN_VIEW))
        val stage = Stage()
        logoCompany(stage)
        stage.title = TITLE_WINDOW_TLK
        stage.scene = Scene(fxmlLoader.load())
        stage.isResizable = false
        stage.show()

        stage.setOnCloseRequest {
            loggStoredProcedure("MAIN_VIEW", "Выход из программы={$currentDateTime}")
            DBConfig().dbDisconnect()
            Platform.exit()
        }
    }
}