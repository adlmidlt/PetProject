package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_VID_OPERATION
import ru.zavodsvet.fgw_desktop_v2.common.VID_OPERATION_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.vidOper.VidOperationViewController
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Выбор вида операции".
 */
class SelectionVidOperViewController : Initializable {

    /**
     * Поле: "Вид операции".
     */
    @FXML
    private lateinit var vidOperTF: TextField

    /**
     * Кнопка: "Выбрать вид операции."
     */
    @FXML
    private lateinit var selectVidOperBtn: Button

    /**
     * Кнопка: "Стереть поле вид операции".
     */
    @FXML
    private lateinit var clearVidOperTFBtn: Button

    /**
     * Код вида операции.
     */
    private var vidOperKod: Int? = null

    private var windowControllerVidOper = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        vidOperTF.isDisable = true
    }

    /**
     * Открыть окно: "Выбрать вид операции".
     */
    fun openWindowSelectVidOper() {
        selectVidOperBtn.setOnAction { onClickSelectVidOperBtn() }
        clearVidOperTFBtn.setOnAction { onClickClearVidOperTFBtn() }
    }

    /**
     * Получить код вида операции.
     */
    fun getVidOperKod() = vidOperKod

    /**
     * При нажатии кнопки выбрать вид операции.
     */
    private fun onClickSelectVidOperBtn() {
        windowControllerVidOper = NewWindow(windowControllerVidOper).openWindow(
            VID_OPERATION_VIEW, TITLE_VID_OPERATION, Modality.APPLICATION_MODAL
        )
        val vidOperController = windowControllerVidOper.controller as VidOperationViewController
        vidOperController.sendSelectedItemVidOper { selectItemVidOper ->
            vidOperTF.text = selectItemVidOper.name
            vidOperKod = selectItemVidOper.kod
        }
    }

    /**
     * При нажатии кнопки очистить поле вида операции.
     */
    private fun onClickClearVidOperTFBtn() {
        vidOperTF.text = ""
        vidOperKod = null
    }
}