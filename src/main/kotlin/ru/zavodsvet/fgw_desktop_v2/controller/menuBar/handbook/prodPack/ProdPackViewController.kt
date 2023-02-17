package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.MenuItem
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.CARD_PROD_PACK_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_CARD_PROD_PACK
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.CardProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.prodPack.ProdPackDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.ProdPackModel
import ru.zavodsvet.fgw_desktop_v2.util.ExportExcelUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна справочника: "Варианта упаковки".
 */
class ProdPackViewController : Initializable {
    /**
     * Главное окно Варианта упаковки.
     */
    @FXML
    private lateinit var prodPackMainWindow: AnchorPane

    /**
     * Окно с фильтром колонок "Варианта упаковки".
     */
    @FXML
    private lateinit var filterColumnsProdPackHBox: HBox

    /**
     * Поиск по колонке "Артикул".
     */
    @FXML
    private lateinit var artSearchTF: TextField

    /**
     * Поиск по колонке "Конструкторское наименование".
     */
    @FXML
    private lateinit var packnameSearchTF: TextField

    /**
     * Поиск по колонке "Цвет".
     */
    @FXML
    private lateinit var colorSearchTF: TextField

    /**
     * Поиск по колонке "Рядность".
     */
    @FXML
    private lateinit var rowsSearchTF: TextField

    /**
     * Поиск по колонке "Кол-ву в ряду".
     */
    @FXML
    private lateinit var couSearchTF: TextField

    /**
     * Поиск по колонке "Глубина Ширина Высота".
     */
    @FXML
    private lateinit var dwhSearchTF: TextField
    /**
     * Таблица: "Варианты упаковки".
     */
    @FXML
    private lateinit var prodPackTable: TableView<ProdPackModel>

    /**
     * Контекстное меню для экспорта в Эксель.
     */
    @FXML
    private lateinit var exportInExcelMenuItem: MenuItem

    /**
     * Контекстное меню Карточки варианта упаковки.
     */
    @FXML
    private lateinit var cardProdPackMenuItem: MenuItem

    /**
     * Реализация объекта доступа к данным справочника: "Варианты упаковки".
     */
    private var prodPackDaoImpl = ProdPackDaoImpl()

    /**
     * Список: "Варианта упаковки".
     */
    private var prodPackList: ObservableList<ProdPackModel> = FXCollections.observableArrayList()

    /**
     * Фильтр по таблице "Остатки".
     */
    private lateinit var filterByProdPack: FilteredList<ProdPackModel>

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Экспорт в Эксель.
     */
    private var exportExcelUtil: ExportExcelUtil<ProdPackModel> = ExportExcelUtil()

    // Контролеры окон.
    private var windowControllerCardProdPack = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        filterColumnsProdPackHBox.stylesheets.add("css/search.css")

        exportInExcelMenuItem.setOnAction { exportInExcel() }
        cardProdPackMenuItem.setOnAction { showCardProdPackWindow() }
    }

    /**
     * Заполнить таблицу: "Варианты упаковки".
     */
    fun fillTableProdPack() {
        resetFilter()
        prodPackTable.items.removeAll()

        prodPackList = prodPackDaoImpl.getAllProdPackList()

        filterByProdPack = FilteredList(prodPackList)
        getListTFSearchProdPack().forEach { item -> prodPackList = filterByColumn(item, filterByProdPack) }
        prodPackTable.items = prodPackList

        prodPackTable.selectionModel.selectFirst()
    }

    /**
     * Отправить выбранный элемент: "Вид операции".
     *
     * @param callback Возвращает сущность.
     */
    fun sendSelectedItemProdPack(callback: (ProdPackModel) -> Unit) {
        fillTableProdPack()

        onDoubleClickMouse(prodPackTable, callback)
        onClickEnterKey(prodPackTable, callback)
    }

    /**
     * Получить список текстовых полей вариантов упаковки.
     */
    private fun getListTFSearchProdPack() = FXCollections.observableArrayList(
        artSearchTF,
        packnameSearchTF,
        colorSearchTF,
        rowsSearchTF,
        couSearchTF,
        dwhSearchTF,
    )

    /**
     * При нажатии клавиши ввода.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onClickEnterKey(tableView: TableView<ProdPackModel>, callback: (ProdPackModel) -> Unit) {
        tableView.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER) {
                setSelectedItemProdPack(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * При двойном щелчке мыши.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onDoubleClickMouse(tableView: TableView<ProdPackModel>, callback: (ProdPackModel) -> Unit) {
        tableView.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                setSelectedItemProdPack(callback)
                helperWithScene.closeStage(ev)
            }
        }

    }

    /**
     * Установить выбранный элемент: "Варианта упаковки".
     *
     * @param callback Возвращает сущность.
     */
    private fun setSelectedItemProdPack(callback: (ProdPackModel) -> Unit) {
        if (getSelectedItemProdPack() != null) {
            prodPackList
                .filter { it.id == getSelectedItemProdPack().id }
                .forEach { callback.invoke(it) }
        }
    }

    /**
     * Показать карточку варианта упаковки.
     */
    private fun showCardProdPackWindow() {
        windowControllerCardProdPack = NewWindow(windowControllerCardProdPack).openWindow(CARD_PROD_PACK_VIEW, TITLE_CARD_PROD_PACK, Modality.NONE)
        val cardProdPackController = windowControllerCardProdPack.controller as CardProdPackViewController
        cardProdPackController.fillTopCardProdPackByProdId(getSelectedItemProdPack().id)
    }

    /**
     * Экспорт в Эксель файл.
     */
    private fun exportInExcel() {
        prodPackMainWindow.isDisable = true
        exportExcelUtil.excelWriter(prodPackTable, "по вариантам упаковки")
        prodPackMainWindow.isDisable = false
    }

    /**
     * Получить выбранный элемент: "Варианта упаковки".
     */
    private fun getSelectedItemProdPack() = prodPackTable.selectionModel.selectedItem

    /**
     * Фильтр по колонке.
     *
     * @param searchKeyword Поле для поиска.
     * @param filter Список для фильтра.
     */
    private fun filterByColumn(searchKeyword: TextField, filter: FilteredList<ProdPackModel>): ObservableList<ProdPackModel> {
        searchKeyword.textProperty().addListener { _, _, _ ->
            filter.setPredicate { prodPack ->
                return@setPredicate (artSearchTF.text.isEmpty() || prodPack.art.lowercase()
                    .indexOf(artSearchTF.text.lowercase()) > -1) && (packnameSearchTF.text.isEmpty() || prodPack.packname.lowercase()
                    .indexOf(packnameSearchTF.text.lowercase()) > -1) && (colorSearchTF.text.isEmpty() || prodPack.color.lowercase()
                    .indexOf(colorSearchTF.text.lowercase()) > -1) && (rowsSearchTF.text.isEmpty() || prodPack.rows.toString()
                    .lowercase()
                    .indexOf(rowsSearchTF.text.lowercase()) > -1) && (couSearchTF.text.isEmpty() || prodPack.cou.toString()
                    .lowercase()
                    .indexOf(couSearchTF.text.lowercase()) > -1) && (dwhSearchTF.text.isEmpty() || prodPack.dwh.lowercase()
                    .indexOf(dwhSearchTF.text.lowercase()) > -1)
            }
            if (!prodPackTable.isFocused) {
                prodPackTable.selectionModel.selectFirst()
            }
        }

        return filter
    }

    /**
     * Сбросить фильтр.
     */
    private fun resetFilter() {
        artSearchTF.text = ""
        packnameSearchTF.text = ""
        colorSearchTF.text = ""
        rowsSearchTF.text = ""
        couSearchTF.text = ""
        dwhSearchTF.text = ""
    }
}