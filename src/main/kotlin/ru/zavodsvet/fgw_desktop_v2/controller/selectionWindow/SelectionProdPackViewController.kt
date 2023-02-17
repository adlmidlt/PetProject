package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.PROD_PACK_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_PROD_PACK
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.ProdPackViewController
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Выбор варианта упаковки".
 */
class SelectionProdPackViewController : Initializable {
    /**
     * Поле: "Вариант упаковки".
     */
    @FXML
    private lateinit var prodPackTF: TextField

    /**
     * Кнопка: "Выбора варианта упаковки".
     */
    @FXML
    private lateinit var selectProdPackBtn: Button

    /**
     * Кнопка: "Стереть поле варианта упаковки".
     */
    @FXML
    private lateinit var clearProdPackTFBtn: Button

    /**
     * ИД варианта упаковки.
     */
    private var prodPackId: Int? = null

    private var windowControllerProdPack = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        prodPackTF.isDisable = true
    }

    /**
     * Открыть окно: "Варианта упаковки".
     */
    fun openWindowSelectProdPack() {
        selectProdPackBtn.setOnAction { onClickSelectProdPackBtn() }
        clearProdPackTFBtn.setOnAction { onClickClearProdPackTFBtn() }
    }

    /**
     * Получить ИД варианта упаковки.
     */
    fun getProdPackId() = prodPackId

    /**
     * При нажатии кнопки выбрать вид операции.
     */
    private fun onClickSelectProdPackBtn() {
        windowControllerProdPack = NewWindow(windowControllerProdPack).openWindow(
            PROD_PACK_VIEW, TITLE_PROD_PACK, Modality.APPLICATION_MODAL
        )
        val prodPackController = windowControllerProdPack.controller as ProdPackViewController
        prodPackController.sendSelectedItemProdPack { selectItemProdPack ->
            prodPackTF.text = selectItemProdPack.packname
            prodPackId = selectItemProdPack.id
        }
    }

    /**
     * При нажатии кнопки очистить поле вида операции.
     */
    private fun onClickClearProdPackTFBtn() {
        prodPackTF.text = ""
        prodPackId = null
    }
}