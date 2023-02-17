package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.KD_NAME_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_KD_NAME
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.kdName.KDNameViewController
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Выбор конструкторского наименования".
 */
class SelectionKDNameViewController : Initializable {

    /**
     * Поле: "Конструкторское наименование".
     */
    @FXML
    private lateinit var kdNameTF: TextField

    /**
     * Кнопка: "Выбрать конструкторское наименование".
     */
    @FXML
    private lateinit var selectKDNameBtn: Button

    /**
     * Кнопка: "Стереть поле конструкторское наименование"
     */
    @FXML
    private lateinit var clearKDNameTFBtn: Button

    /**
     * ИД конструкторского наименования.
     */
    private var kdNameId: Int? = null

    private var windowControllerKDName = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        kdNameTF.isDisable = true
    }

    /**
     * Открыть окно: "Конструкторские наименования".
     */
    fun openWindowSelectKDName() {
        selectKDNameBtn.setOnAction { onClickSelectKDNameBtn() }
        clearKDNameTFBtn.setOnAction { onClickClearKDNameTFBtn() }
    }

    /**
     * Получить ид конструкторского наименования.
     */
    fun getKDNameId() = kdNameId

    /**
     * Установить наименование конструкторской документации.
     *
     * @param kdName Конструкторская документация.
     */
    fun setKdName(kdName: String) {
        kdNameTF.text = kdName
    }

    /**
     * При нажатии кнопки выбрать конструкторское наименование.
     */
    private fun onClickSelectKDNameBtn() {
        windowControllerKDName = NewWindow(windowControllerKDName).openWindow(
            KD_NAME_VIEW, TITLE_KD_NAME, Modality.APPLICATION_MODAL
        )
        val kdNameController = windowControllerKDName.controller as KDNameViewController
        kdNameController.sendSelectedItemKDName { selectedItemKDName ->
            kdNameTF.text = selectedItemKDName.name
            kdNameId = selectedItemKDName.id
        }
    }

    /**
     * При нажатии кнопки очистить поле конструкторского наименования.
     */
    private fun onClickClearKDNameTFBtn() {
        kdNameTF.text = ""
        kdNameId = null
    }
}