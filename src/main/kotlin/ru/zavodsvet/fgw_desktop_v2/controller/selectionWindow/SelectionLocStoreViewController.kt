package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.LOC_STORE_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.LocStoreViewController
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Выбор участка хранения".
 */
class SelectionLocStoreViewController : Initializable {
    /**
     * Поле: "Вид операции".
     */
    @FXML
    private lateinit var locStoreTF: TextField

    /**
     * Кнопка: "Выбрать вид операции."
     */
    @FXML
    private lateinit var selectLocStoreBtn: Button

    /**
     * Кнопка: "Стереть поле вид операции".
     */
    @FXML
    private lateinit var clearLocStoreTFBtn: Button

    /**
     * Код вида операции.
     */
    private var locStoreId: Int? = null

    private var windowControllerLocStore = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        locStoreTF.isDisable = true
    }

    /**
     * Открыть окно: "Выбрать вид операции".
     */
    fun openWindowSelectLocStore() {
        selectLocStoreBtn.setOnAction { onClickSelectLocStoreBtn() }
        clearLocStoreTFBtn.setOnAction { onClickClearLocStoreTFBtn() }
    }

    /**
     * Получить код вида операции.
     */
    fun getLocStoreId() = locStoreId

    /**
     * При нажатии кнопки выбрать участок хранения.
     */
    private fun onClickSelectLocStoreBtn() {
        windowControllerLocStore = NewWindow(windowControllerLocStore).openWindow(
            LOC_STORE_VIEW, TITLE_LOC_STORE, Modality.APPLICATION_MODAL
        )
        val locStoreController = windowControllerLocStore.controller as LocStoreViewController
        locStoreController.sendSelectedItemLocStore { selectItemLocStore ->
            locStoreTF.text = selectItemLocStore.name
            locStoreId = selectItemLocStore.id
        }
    }

    /**
     * При нажатии кнопки очистить поле вида операции.
     */
    private fun onClickClearLocStoreTFBtn() {
        locStoreTF.text = ""
        locStoreId = null
    }
}