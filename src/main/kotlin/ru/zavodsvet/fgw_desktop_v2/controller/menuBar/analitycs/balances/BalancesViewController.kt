package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.analitycs.balances

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.ALL_GOOD_WORK_NEXT
import ru.zavodsvet.fgw_desktop_v2.common.CARD_PROD_PACK_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateTimeFormat
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_CARD_PROD_PACK
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.CardProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionKDNameViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateTimeViewController
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.balances.BalancesDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances.BalancesModel
import ru.zavodsvet.fgw_desktop_v2.util.ExportExcelUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.time.LocalDateTime
import java.util.*

/**
 * Контроллер окна аналитики: "Остатки".
 */
class BalancesViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(BalancesViewController::class.java)

    /**
     * Главное окно Остатки.
     */
    @FXML
    private lateinit var balanceMainWindow: AnchorPane

    /**
     * Сцена окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameView: AnchorPane

    /**
     * Контроллер окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameViewController: SelectionKDNameViewController

    /**
     * Сцена окна: "Выбор варианта упаковки".
     */
    @FXML
    private lateinit var selectionProdPackView: AnchorPane

    /**
     * Контроллер окна: "Выбор варианта упаковки".
     */
    @FXML
    private lateinit var selectionProdPackViewController: SelectionProdPackViewController

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
     * Флаг: "Даты создания произведенной продукции".
     */
    @FXML
    private lateinit var dateCreationManufacturingProductCheckBox: CheckBox

    /**
     * Окно сцены: "Шаблон даты".
     */
    @FXML
    private lateinit var dateManufacturingProductView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты". Дата производства.
     */
    @FXML
    private lateinit var dateManufacturingProductViewController: TemplateDateViewController

    /**
     * Окно сцены: "Шаблон даты время".
     */
    @FXML
    private lateinit var dateTimeOfPeriodBalancesView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты время". Остатки на...
     */
    @FXML
    private lateinit var dateTimeOfPeriodBalancesViewController: TemplateDateTimeViewController

    /**
     * Объединить по варианту упаковки.
     */
    @FXML
    private lateinit var prodPackCheckBox: CheckBox

    /**
     * Является ли выбранным чек бокс по варианту упаковки.
     */
    private var flagProdPack = 0

    /**
     * Объединить по дате создания Варианта упаковки.
     */
    @FXML
    private lateinit var dateCreationCheckBox: CheckBox

    /**
     * Является ли выбранным чек бокс по дате создания Варианта упаковки.
     */
    private var flagDateCreation = 0

    /**
     * Объединить по участкам хранения.
     */
    @FXML
    private lateinit var locStoreCheckBox: CheckBox

    /**
     * Является ли выбранным чек бокс по участкам хранения.
     */
    private var flagLocStore = 0

    /**
     * Объединить по дочерним участкам хранения.
     */
    @FXML
    private lateinit var childLocStoreCheckBox: CheckBox

    /**
     * Является ли выбранным чек бокс по дочерним участкам хранения.
     */
    private var flagChildLocStore = 1

    /**
     * Кнопка: "Применить фильтр".
     */
    @FXML
    private lateinit var applyFilterBtn: Button

    /**
     * Поиск по колонкам таблицы остатки.
     */
    @FXML
    private lateinit var searchByColumnsBalancesHBox: HBox

    /**
     * Поиск по колонке "Артикул".
     */
    @FXML
    private lateinit var artSearchTF: TextField

    /**
     * Поиск по колонке "Конструкторское наименование".
     */
    @FXML
    private lateinit var konsnameSearchTF: TextField

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
     * Поиск по колонке "Дата производства".
     */
    @FXML
    private lateinit var dtcreateSearchTF: TextField

    /**
     * Поиск по колонке "Участки хранения".
     */
    @FXML
    private lateinit var locnameSearchTF: TextField

    /**
     * Таблица: "Остатки".
     */
    @FXML
    private lateinit var balancesTable: TableView<BalancesModel>

    /**
     * Кол-во строк.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Кол-во п/п.
     */
    @FXML
    private lateinit var sumKolLabel: Label

    /**
     * Сумма Кол-ва изделий.
     */
    @FXML
    private lateinit var sumPiecesLabel: Label

    /**
     * Таблица: "Детализированные остатки".
     */
    @FXML
    private lateinit var detailsBalancesTable: TableView<BalancesModel>

    /**
     * Контекстное меню: Перейти в карточку вариантов упаковки.
     */
    @FXML
    private lateinit var goToCardProdPackMenuItem: MenuItem

    /**
     * Контекстное меню для экспорта в Эксель.
     */
    @FXML
    private lateinit var exportInExcelContextMenu: MenuItem

    /**
     * Кол-во строк.
     */
    @FXML
    private lateinit var rowDetailCountLabel: Label

    /**
     * Кол-во п/п.
     */
    @FXML
    private lateinit var sumDetailKolLabel: Label

    /**
     * Сумма Кол-ва изделий.
     */
    @FXML
    private lateinit var sumDetailPiecesLabel: Label

    /**
     * Список остатков.
     */
    private lateinit var balancesList: ObservableList<BalancesModel>

    /**
     * Фильтр по таблице "Остатки".
     */
    private lateinit var filterByBalance: FilteredList<BalancesModel>

    /**
     * Список детализированных остатков.
     */
    private lateinit var detailsBalancesList: ObservableList<BalancesModel>

    /**
     * Реализация объекта доступа к данным аналитики: "Остатки".
     */
    private var balancesDaoImpl = BalancesDaoImpl()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Экспорт в Эксель.
     */
    private var exportExcelUtil: ExportExcelUtil<BalancesModel> = ExportExcelUtil()

    // Контролеры окон.
    private var windowControllerCardProdPack = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        searchByColumnsBalancesHBox.stylesheets.add("css/search.css")
        dateManufacturingProductView.isDisable = true

        resetFilter()
        onClickApplyFilter()
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        applyFilterBtn.setOnAction { onClickApplyFilter() }
        balancesTable.setOnMouseClicked { fillTableDetailsBalances() }
        goToCardProdPackMenuItem.setOnAction { showCardProdPackWindow() }
        exportInExcelContextMenu.setOnAction { exportInExcel() }
    }

    /**
     * Отправить выбранный элемент: "Участок хранения".
     */
    fun sendSelectedItemBalances(dateTimeBalance: String?, callback: ((BalancesModel) -> Unit)?) {
        fillTableBalances(dateTimeBalance)
        onClickMouse(dateTimeBalance, callback)
        onClickEnterKey(dateTimeBalance, callback)
    }

    /**
     * При нажатии кнопки применить фильтр.
     */
    private fun onClickApplyFilter() {
        selectionKDNameViewController.openWindowSelectKDName()
        selectionProdPackViewController.openWindowSelectProdPack()
        selectionLocStoreViewController.openWindowSelectLocStore()

        dateCreationManufacturingProductCheckBox.setOnAction {
            dateManufacturingProductView.isDisable = !dateCreationManufacturingProductCheckBox.isSelected
        }
        filterBySelectFlags()
        fillTableBalances()
    }

    /**
     * Фильтр по выбранным флагам.
     */
    private fun filterBySelectFlags() {
        prodPackCheckBox.setOnAction {
            flagProdPack = if (prodPackCheckBox.isSelected) 1 else 0
        }

        dateCreationCheckBox.setOnAction {
            flagDateCreation = if (dateCreationCheckBox.isSelected) 1 else 0
        }

        locStoreCheckBox.setOnAction {
            flagLocStore = if (locStoreCheckBox.isSelected) 1 else 0
        }

        childLocStoreCheckBox.setOnAction {
            flagChildLocStore = if (childLocStoreCheckBox.isSelected) 1 else 0
        }
    }

    /**
     * Заполнить таблицу: "Остатки".
     *
     * @param dateTimeBalance Остатки на дату время.
     */
    fun fillTableBalances(dateTimeBalance: String? = null) {
        resetFilter()
        balancesTable.items.removeAll()
        setDateTimeBalances(dateTimeBalance)

        balancesList = FXCollections.observableArrayList()
        balancesDaoImpl.findAllBalancesPallets(
            balancesList,
            selectionKDNameViewController.getKDNameId(),
            selectionProdPackViewController.getProdPackId(),
            if (dateCreationManufacturingProductCheckBox.isSelected) dateManufacturingProductViewController.getTemplateDate() else null,
            selectionLocStoreViewController.getLocStoreId(),
            dateTimeBalance,
            flagProdPack,
            flagDateCreation,
            flagLocStore,
            flagChildLocStore
        )
        filterByBalance = FilteredList(balancesList)
        getTextFieldsOfBalance().forEach { item -> balancesList = filterByColumns(item, filterByBalance) }

        balancesTable.items = balancesList
        balancesTable.selectionModel.selectFirst()
        fillFooterTableBalances()
    }

    /**
     * Получить список текстовых полей остатков.
     */
    private fun getTextFieldsOfBalance() = FXCollections.observableArrayList(
        artSearchTF,
        konsnameSearchTF,
        colorSearchTF,
        rowsSearchTF,
        couSearchTF,
        dwhSearchTF,
        dtcreateSearchTF,
        locnameSearchTF,
    )

    /**
     * Фильтр поиск по колонкам.
     *
     * @param searchKeyword Поле для поиска.
     * @param filter Список для фильтра.
     */
    private fun filterByColumns(searchKeyword: TextField, filter: FilteredList<BalancesModel>): ObservableList<BalancesModel> {
        searchKeyword.textProperty().addListener { _, _, _ ->
            filter.setPredicate { balances ->
                return@setPredicate (artSearchTF.text.isEmpty() || balances.art.lowercase()
                    .indexOf(artSearchTF.text.lowercase()) > -1)
                        && (konsnameSearchTF.text.isEmpty() || balances.konsname.lowercase()
                    .indexOf(konsnameSearchTF.text.lowercase()) > -1)
                        && (colorSearchTF.text.isEmpty() || balances.color.lowercase()
                    .indexOf(colorSearchTF.text.lowercase()) > -1)
                        && (rowsSearchTF.text.isEmpty() || balances.rows.toString().lowercase()
                    .indexOf(rowsSearchTF.text.lowercase()) > -1)
                        && (couSearchTF.text.isEmpty() || balances.cou.toString().lowercase()
                    .indexOf(couSearchTF.text.lowercase()) > -1)
                        && (dwhSearchTF.text.isEmpty() || balances.dwh.lowercase()
                    .indexOf(dwhSearchTF.text.lowercase()) > -1)
                        && (dtcreateSearchTF.text.isEmpty() || balances.dtcreate.lowercase()
                    .indexOf(dtcreateSearchTF.text.lowercase()) > -1)
                        && (locnameSearchTF.text.isEmpty() || balances.locname.lowercase()
                    .indexOf(locnameSearchTF.text.lowercase()) > -1)
            }
            if (!balancesTable.isFocused) {
                balancesTable.selectionModel.selectFirst()
            }
            fillFooterTableBalances()
        }

        return filter
    }

    /**
     * Заполнить таблицу: "Детализированные остатки".
     *
     * @param dateTimeBalance Остатки на дату время.
     */
    private fun fillTableDetailsBalances(dateTimeBalance: String? = null) {
        detailsBalancesTable.items.removeAll()
        detailsBalancesList = FXCollections.observableArrayList()

        if (getSelectItemBalances() != null) {
            balancesDaoImpl.findAllBalancesPallets(
                detailsBalancesList,
                getSelectItemBalances().prodid,
                getSelectItemBalances().packid,
                getSelectItemBalances().dtcreate,
                getSelectItemBalances().locid,
                dateTimeBalance,
                0,
                0,
                0,
                0,
            )
            detailsBalancesTable.items = detailsBalancesList
            detailsBalancesTable.selectionModel.selectFirst()
            fillFooterTableDetailBalances()
        }
    }

    /**
     * Показать карточку варианта упаковки.
     */
    private fun showCardProdPackWindow() {
        windowControllerCardProdPack =
            NewWindow(windowControllerCardProdPack).openWindow(CARD_PROD_PACK_VIEW, TITLE_CARD_PROD_PACK, null)
        val cardProdPackController = windowControllerCardProdPack.controller as CardProdPackViewController
        cardProdPackController.fillTopCardProdPackByProdId(getSelectItemBalances().packid)
    }

    /**
     * Экспорт в Эксель файл.
     */
    private fun exportInExcel() {
        balanceMainWindow.isDisable = true
        exportExcelUtil.excelWriter(balancesTable, "по остаткам")
        balanceMainWindow.isDisable = false
    }

    /**
     * Установить дату время остатков.
     *
     * @param dateTimeBalance Остатки на дату время.
     */
    private fun setDateTimeBalances(dateTimeBalance: String?) {
        if (dateTimeBalance != null) {
            val ldt = LocalDateTime.parse(dateTimeBalance, currentDateTimeFormat)
            dateTimeOfPeriodBalancesViewController.setTemplateDateTime(
                ldt.year, ldt.monthValue, ldt.dayOfMonth, ldt.hour, ldt.minute
            )
        }
    }

    /**
     * Открыть выбранный элемент двойным щелчком мыши.
     *
     * @param dateTimeBalance Событие при нажатии на мышку.
     * @param callback Остатки.
     */
    private fun onClickMouse(dateTimeBalance: String?, callback: ((BalancesModel) -> Unit)?) {
        balancesTable.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2 && getSelectItemBalances() != null) {
                fillTableDetailsBalances(dateTimeBalance)
                try {
                    if (detailsBalancesList.count() == 1) {
                        callback!!.invoke(getSelectItemBalances())
                        helperWithScene.closeStage(ev)
                    }
                } catch (eNPE: NullPointerException) {
                    logger.warn(ALL_GOOD_WORK_NEXT)
                }
            }
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 1) {
                fillTableDetailsBalances(dateTimeBalance)
            }
        }

        detailsBalancesTable.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                callback!!.invoke(getSelectItemDetailsBalances())
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Открыть выбранный элемент с помощью клавиши ввода.
     *
     * @param dateTimeBalance Событие при нажатии на клавишу ввода.
     * @param callback Остатки.
     */
    private fun onClickEnterKey(dateTimeBalance: String?, callback: ((BalancesModel) -> Unit)?) {
        balancesTable.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER && getSelectItemBalances() != null) {
                fillTableDetailsBalances(dateTimeBalance)
                if (detailsBalancesList.count() == 1) {
                    callback!!.invoke(getSelectItemBalances())
                    helperWithScene.closeStage(ev)
                }
            }
            if (ev.code == KeyCode.UP || ev.code == KeyCode.DOWN) {
                fillTableDetailsBalances(dateTimeBalance)
            }
        }

        detailsBalancesTable.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER && detailsBalancesTable.selectionModel.selectedItem != null) {
                callback!!.invoke(getSelectItemDetailsBalances())
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Получить выбранный элемент: "Остатки".
     */
    private fun getSelectItemBalances() = balancesTable.selectionModel.selectedItem

    /**
     * Получить выбранный элемент: "Остатки".
     */
    private fun getSelectItemDetailsBalances() = detailsBalancesTable.selectionModel.selectedItem

    /**
     * Заполнить подвал таблицы "Остатки".
     */
    private fun fillFooterTableBalances() {
        rowCountLabel.text = HelperUtil().getThousandSeparator(balancesTable.items.count().toString())
        sumKolLabel.text = HelperUtil().getThousandSeparator(balancesTable.items.sumOf { it.balances }.toString())
        sumPiecesLabel.text = HelperUtil().getThousandSeparator(balancesTable.items.sumOf { it.pieces }.toString())
    }

    /**
     * Заполнить подвал таблицы "Детализированными остатками".
     */
    private fun fillFooterTableDetailBalances() {
        rowDetailCountLabel.text = HelperUtil().getThousandSeparator(detailsBalancesTable.items.count().toString())
        sumDetailKolLabel.text =
            HelperUtil().getThousandSeparator(detailsBalancesTable.items.sumOf { it.balances }.toString())
        sumDetailPiecesLabel.text =
            HelperUtil().getThousandSeparator(detailsBalancesTable.items.sumOf { it.pieces }.toString())
    }

    /**
     * Сбросить фильтр.
     */
    private fun resetFilter() {
        artSearchTF.text = ""
        konsnameSearchTF.text = ""
        colorSearchTF.text = ""
        rowsSearchTF.text = ""
        couSearchTF.text = ""
        dwhSearchTF.text = ""
        dtcreateSearchTF.text = ""
        locnameSearchTF.text = ""
    }
}