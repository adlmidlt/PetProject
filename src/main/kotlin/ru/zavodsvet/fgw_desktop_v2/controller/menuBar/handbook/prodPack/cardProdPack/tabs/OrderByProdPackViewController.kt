package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.tabs

import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.TableView
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.CARD_OPERATION_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_CARD_OPERATION
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.CardOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionDateOfPeriodViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.prodPack.cardProdPack.tabs.OrderByProdPackDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.cardProdPack.tabs.OrderByProdPackModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil
import java.net.URL
import java.time.LocalDate
import java.util.*

class OrderByProdPackViewController : Initializable {
    /**
     * Сцена окна: "Шаблон дата".
     */
    @FXML
    private lateinit var selectionDateOfPeriodView: AnchorPane

    /**
     * Контроллер окна: "Шаблон выбора периода даты"
     */
    @FXML
    private lateinit var selectionDateOfPeriodViewController: SelectionDateOfPeriodViewController

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
     * Таблица ордера варианта упаковки.
     */
    @FXML
    private lateinit var orderProdPackTable: TableView<OrderByProdPackModel>

    /**
     * Контекстное меню перейти в операцию.
     */
    @FXML
    private lateinit var goToOperMenuItem: MenuItem

    /**
     * Сумма Кол-ва изделий.
     */
    @FXML
    private lateinit var sumPiecesLabel: Label

    /**
     * Кол-во строк.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Список ордеров варианта упаковки.
     */
    private lateinit var orderByProdPackList: ObservableList<OrderByProdPackModel>

    /**
     * Объект доступа к данным ордерам варианта упаковки.
     */
    private var orderByProdPackDaoImpl = OrderByProdPackDaoImpl()

    /**
     * ИД выбранного элемента в таблице.
     */
    private var selectedItemId: Int = 0

    private var locStore = 0

    /**
     * Системная дата минус 30 дней.
     */
    private var minus30DaysFromLocalDate = LocalDate.now().minusDays(30)

    // Контроллеры окон.
    private var windowControllerCardOper = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        fillFilterOrderByProdPack()
        goToOperMenuItem.setOnAction { showCardOperWindow() }
    }

    private fun fillFilterOrderByProdPack() {
        selectionLocStoreViewController.openWindowSelectLocStore()
        applyFilterBtn.setOnAction { fillTableOrderByProdPack(locStore) }
    }

    /**
     * Заполнить таблицу: "Ордера варианта упаковки".
     *
     * @param packid ИД варианта упаковки.
     */
    fun fillTableOrderByProdPack(packid: Int) {
        saveBookMark()
        orderProdPackTable.items.removeAll()

        selectionDateOfPeriodViewController.setStartDate(
            minus30DaysFromLocalDate.year,
            minus30DaysFromLocalDate.monthValue,
            minus30DaysFromLocalDate.dayOfMonth
        )

        orderByProdPackList = orderByProdPackDaoImpl.getOrdersPallets(
            packid,
            selectionLocStoreViewController.getLocStoreId(),
            selectionDateOfPeriodViewController.getStartDate(),
            selectionDateOfPeriodViewController.getEndDate()
        )

        this.locStore = packid

        orderProdPackTable.items = orderByProdPackList
        loadBookMark()
        fillFooterTableOrderByProdPack()
    }

    /**
     * Открыть окно по выбранному элементу через контекстное меню.
     */
    private fun showCardOperWindow() {
        windowControllerCardOper = NewWindow(windowControllerCardOper).openWindow(
            CARD_OPERATION_VIEW, TITLE_CARD_OPERATION, Modality.APPLICATION_MODAL
        )
        val cardOperController = windowControllerCardOper.controller as CardOperationViewController
        cardOperController.fillTopCardOper(getSelectedItemOrderByProdPack().operid)
    }

    /**
     * Получить выбранный элемент: "Ордер по варианту упаковки".
     */
    private fun getSelectedItemOrderByProdPack() = orderProdPackTable.selectionModel.selectedItem

    /**
     * Запоминает текущую позицию строки в таблице.
     */
    private fun saveBookMark() {
        if (orderProdPackTable.selectionModel.selectedItem != null) {
            selectedItemId = getSelectedItemOrderByProdPack().operid
        }
    }

    /**
     * Загружает текущую позицию строки в таблице.
     */
    private fun loadBookMark() {
        orderProdPackTable.items.filter { it.operid == selectedItemId }.forEach { orderProdPackTable.selectionModel.select(it) }
    }

    /**
     * Заполнить подвал таблицы: "Операции".
     */
    private fun fillFooterTableOrderByProdPack() {
        rowCountLabel.text = orderProdPackTable.items.count().toString()
        sumPiecesLabel.text = HelperUtil().getThousandSeparator(orderProdPackTable.items.sumOf { it.pieces }.toString())
    }
}