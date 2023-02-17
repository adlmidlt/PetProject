package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.manufacturingProduct

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.CardLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.CardProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.CardOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionDateTimeOfPeriodViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.manufacturingProduct.ManufacturingProductDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.manufacturingProduct.ManufacturingProductModel
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Производство продукции".
 */
class ManufacturingProductViewController : Initializable {
    /**
     * Окно сцены: "Шаблон выбора периода дата время".
     */
    @FXML
    private lateinit var selectionDateTimeOfPeriodView: AnchorPane

    /**
     * Контроллер окна: "Шаблон выбора периода дата время".
     */
    @FXML
    private lateinit var selectionDateTimeOfPeriodViewController: SelectionDateTimeOfPeriodViewController

    /**
     * Сцена окна: "Выбор участка хранения".
     */
    @FXML
    private lateinit var selectionLocStoreView: AnchorPane

    /**
     * Контроллер окна: "Выбор участка хранения".
     */
    @FXML
    private lateinit var selectionLocStoreViewController: SelectionLocStoreViewController

    /**
     * Кнопка: "Применить фильтр".
     */
    @FXML
    private lateinit var applyFilterBtn: Button

    /**
     * Поиск по колонкам.
     */
    @FXML
    private lateinit var searchColumnsManufacturingPackHBox: HBox

    /**
     * Поиск по колонке Артикул.
     */
    @FXML
    private lateinit var artSearchTF: TextField

    /**
     * Поиск по колонке Наименованию продукта.
     */
    @FXML
    private lateinit var prodSearchTF: TextField

    /**
     * Поиск по колонке Бар-коду.
     */
    @FXML
    private lateinit var barcodeSearchTF: TextField

    /**
     * Поиск по колонке Дата действия.
     */
    @FXML
    private lateinit var dtactSearchTF: TextField

    /**
     * Поиск по колонке Дата создания.
     */
    @FXML
    private lateinit var dtcreateSearchTF: TextField

    /**
     * Поиск по колонке Действие.
     */
    @FXML
    private lateinit var actionSearchTF: TextField

    /**
     * Поиск по колонке ФИО.
     */
    @FXML
    private lateinit var fioSearchTF: TextField

    /**
     * Поиск по колонке Линии упаковки.
     */
    @FXML
    private lateinit var locpackSearchTF: TextField

    /**
     * Поиск по колонке Участку хранения.
     */
    @FXML
    private lateinit var locstoreSearchTF: TextField

    /**
     * Таблица: "Производство продукции".
     */
    @FXML
    private lateinit var manufacturingProductTable: TableView<ManufacturingProductModel>

    /**
     * Контекстное меню: Перейти в карточку вариантов упаковки.
     */
    @FXML
    private lateinit var goToCardProdPackMenuItem: MenuItem

    /**
     * Контекстное меню: Перейти в карточку участка упаковки.
     */
    @FXML
    private lateinit var goToCardLocStoreMenuItem: MenuItem

    /**
     * Контекстное меню: Перейти в карточку операции.
     */
    @FXML
    private lateinit var goToCardOperStoreMenuItem: MenuItem

    /**
     * Контекстное меню: Перейти к просмотру этикетки.
     */
    @FXML
    private lateinit var goToGifTicketStoreMenuItem: MenuItem

    /**
     * Подвал кол-во строк в таблице.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Список производственных продуктов.
     */
    private var manufacturingProductList: ObservableList<ManufacturingProductModel> =
        FXCollections.observableArrayList()

    /**
     * Фильтр по таблице "Производство продукции".
     */
    private lateinit var filterByPackingProd: FilteredList<ManufacturingProductModel>

    /**
     * Объект доступа к данным операций "Производство продукции".
     */
    private var manufacturingProductDaoImpl = ManufacturingProductDaoImpl()

    // Контролеры окон.
    private var windowControllerCardProdPack = WindowController()
    private var windowControllerCardOper = WindowController()
    private var windowControllerCardLocStore = WindowController()
    private var windowControllerTicket = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        fillFilterTableManufacturingProd()
        contextManuTableGoTo()
        searchColumnsManufacturingPackHBox.stylesheets.addAll("css/search.css")
    }

    /**
     * Заполнить таблицу: "Производство продукции по фильтру".
     *
     * @param startDateTime Начальная дата.
     * @param endDateTime Конечная дата.
     * @param locId Участок хранения.
     */
    fun fillTableManufacturingProduct(startDateTime: String?, endDateTime: String?, locId: Int?) {
        resetFilter()
        manufacturingProductTable.items.removeAll()

        selectionDateTimeOfPeriodViewController.setStartDateTime(startDateTime!!)
        selectionDateTimeOfPeriodViewController.setEndDateTime(endDateTime!!)

        manufacturingProductList = manufacturingProductDaoImpl.showPackPalletsActions(startDateTime, endDateTime, locId)

        filterByPackingProd = FilteredList(manufacturingProductList)
        getListTFSearchManufacturingProd().forEach { item ->
            manufacturingProductList = filterByColumns(item, filterByPackingProd)
        }

        manufacturingProductTable.items = manufacturingProductList
        manufacturingProductTable.selectionModel.selectFirst()
        fillFooterTableManufacturingProduct()
    }

    private fun fillFilterTableManufacturingProd() {
        selectionLocStoreViewController.openWindowSelectLocStore()

        applyFilterBtn.setOnAction {
            fillTableManufacturingProduct(
                selectionDateTimeOfPeriodViewController.getStartDateTime(),
                selectionDateTimeOfPeriodViewController.getEndDateTime(),
                selectionLocStoreViewController.getLocStoreId()
            )
        }
    }

    /**
     * Контекстное меню таблицы: "Переходы по карточкам".
     */
    private fun contextManuTableGoTo() {
        goToCardProdPackMenuItem.setOnAction { showCardProdPackWindow() }
        goToCardLocStoreMenuItem.setOnAction { showCardLocStoreWindow() }
        goToCardOperStoreMenuItem.setOnAction { showCardOperWindow() }
        goToGifTicketStoreMenuItem.setOnAction { showTicketWindow() }
    }

    /**
     * Показать карточку варианта упаковки.
     */
    private fun showCardProdPackWindow() {
        windowControllerCardProdPack =
            NewWindow(windowControllerCardProdPack).openWindow(CARD_PROD_PACK_VIEW, TITLE_CARD_PROD_PACK, Modality.NONE)
        val cardProdPackController = windowControllerCardProdPack.controller as CardProdPackViewController
        cardProdPackController.fillTopCardProdPackByProdId(getSelectedItemManufacturingProd().packId)
    }

    /**
     * Показать карточку участка хранения.
     */
    private fun showCardLocStoreWindow() {
        windowControllerCardLocStore =
            NewWindow(windowControllerCardLocStore).openWindow(CARD_LOC_STORE_VIEW, TITLE_CARD_LOC_STORE, Modality.NONE)
        val cardLocStoreController = windowControllerCardLocStore.controller as CardLocStoreViewController
        cardLocStoreController.fillCardLocStoreByIdLocStore(getSelectedItemManufacturingProd().locstoreid)
    }

    /**
     * Показать карточку операции.
     */
    private fun showCardOperWindow() {
        windowControllerCardOper =
            NewWindow(windowControllerCardOper).openWindow(CARD_OPERATION_VIEW, TITLE_CARD_OPERATION, Modality.NONE)
        val cardOperController = windowControllerCardOper.controller as CardOperationViewController
        cardOperController.fillTopCardOper(getSelectedItemManufacturingProd().operid)
    }

    /**
     * Показать этикетку.
     */
    private fun showTicketWindow() {
        windowControllerTicket = NewWindow(windowControllerTicket).openWindow(TICKET_VIEW, TITLE_TICKET, Modality.NONE)
        val ticketController = windowControllerTicket.controller as TicketViewController
        ticketController.showGifTicketProdPack(getSelectedItemManufacturingProd().ticketid)
    }

    /**
     * Заполнить подвал таблицы "Производство продукции".
     */
    private fun fillFooterTableManufacturingProduct() {
        rowCountLabel.text = manufacturingProductTable.items.count().toString()
    }

    /**
     * Получить выбранный элемент: "Производства продукции".
     */
    private fun getSelectedItemManufacturingProd() = manufacturingProductTable.selectionModel.selectedItem

    private fun getListTFSearchManufacturingProd() = FXCollections.observableArrayList(
        artSearchTF,
        prodSearchTF,
        barcodeSearchTF,
        dtactSearchTF,
        dtcreateSearchTF,
        actionSearchTF,
        fioSearchTF,
        locpackSearchTF,
        locstoreSearchTF,
    )

    /**
     * Фильтр поиск по колонкам.
     *
     * @param searchKeyword Поле для поиска.
     * @param filter Список для фильтра.
     */
    private fun filterByColumns(
        searchKeyword: TextField,
        filter: FilteredList<ManufacturingProductModel>,
    ): ObservableList<ManufacturingProductModel> {
        searchKeyword.textProperty().addListener { _, _, _ ->
            filter.setPredicate { packingProd ->
                return@setPredicate (artSearchTF.text.isEmpty() || packingProd.art.lowercase()
                    .indexOf(artSearchTF.text.lowercase()) > -1)
                        && (prodSearchTF.text.isEmpty() || packingProd.prod.lowercase()
                    .indexOf(prodSearchTF.text.lowercase()) > -1)
                        && (barcodeSearchTF.text.isEmpty() || packingProd.barcode.lowercase()
                    .indexOf(barcodeSearchTF.text.lowercase()) > -1)
                        && (dtactSearchTF.text.isEmpty() || packingProd.dtact.lowercase()
                    .indexOf(dtactSearchTF.text.lowercase()) > -1)
                        && (dtcreateSearchTF.text.isEmpty() || packingProd.dtcreate.lowercase()
                    .indexOf(dtcreateSearchTF.text.lowercase()) > -1)
                        && (actionSearchTF.text.isEmpty() || packingProd.action.lowercase()
                    .indexOf(actionSearchTF.text.lowercase()) > -1)
                        && (fioSearchTF.text.isEmpty() || packingProd.fio.lowercase()
                    .indexOf(fioSearchTF.text.lowercase()) > -1)
                        && (locpackSearchTF.text.isEmpty() || packingProd.locpack.lowercase()
                    .indexOf(locpackSearchTF.text.lowercase()) > -1)
                        && (locstoreSearchTF.text.isEmpty() || packingProd.locstore.lowercase()
                    .indexOf(locstoreSearchTF.text.lowercase()) > -1)
            }
            if (!manufacturingProductTable.isFocused) {
                manufacturingProductTable.selectionModel.selectFirst()
            }
            fillFooterTableManufacturingProduct()
        }

        return filter
    }

    /**
     * Сбросить фильтр.
     */
    private fun resetFilter() {
        artSearchTF.text = ""
        prodSearchTF.text = ""
        barcodeSearchTF.text = ""
        dtactSearchTF.text = ""
        dtcreateSearchTF.text = ""
        actionSearchTF.text = ""
        fioSearchTF.text = ""
        locpackSearchTF.text = ""
        locstoreSearchTF.text = ""
    }
}