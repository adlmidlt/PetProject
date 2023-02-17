package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.tabs

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.balances.BalancesDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances.BalancesModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil
import java.net.URL
import java.util.*

/**
 * Контроллер таба "Остатки по участку хранения".
 */
class BalanceByLocStoreViewController : Initializable {
    /**
     * Таблица
     */
    @FXML
    private lateinit var balanceByLocStoreTable: TableView<BalancesModel>

    /**
     * Объект доступа к данным аналитики "Остатки"
     */
    private var balancesLocStoreDaoImpl = BalancesDaoImpl()

    /**
     * Список остатков по варианту упаковки.
     */
    private lateinit var balancesByLocStoreList: ObservableList<BalancesModel>

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
     * Таблица "Остатки по участку хранения".
     */
    @FXML
    private lateinit var balanceDetailByLocStoreTable: TableView<BalancesModel>

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
     * Список Детализированных "Остатков".
     */
    private lateinit var balancesDetailByLocStoreList: ObservableList<BalancesModel>

    //ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        balanceByLocStoreTable.setOnMouseClicked { fillDetailTableBalanceByLocStore() }
        openSelectedItemUsingKeyCode()
    }

    fun fillTableBalanceByLocStore(locId: Int) {
        balanceByLocStoreTable.items.removeAll()
        balancesByLocStoreList = FXCollections.observableArrayList()
        balancesLocStoreDaoImpl.findAllBalancesPallets(
            balancesByLocStoreList,
            null,
            null,
            null,
            locId,
            null,
            1,
            1,
            1,
            1
        )

        balanceByLocStoreTable.items = balancesByLocStoreList
        balanceByLocStoreTable.selectionModel.selectFirst()
        fillFooterBalanceByLocStore()
    }

    private fun fillDetailTableBalanceByLocStore() {
        balanceDetailByLocStoreTable.items.clear()
        balancesDetailByLocStoreList = FXCollections.observableArrayList()

        val balanceByLocStoreSelectItem = balanceByLocStoreTable.selectionModel.selectedItem
        if (balanceByLocStoreSelectItem != null) {
            balancesLocStoreDaoImpl.findAllBalancesPallets(
                balancesDetailByLocStoreList,
                balanceByLocStoreSelectItem.prodid,
                balanceByLocStoreSelectItem.packid,
                balanceByLocStoreSelectItem.dtcreate,
                balanceByLocStoreSelectItem.locid,
                null,
                0,
                0,
                0,
                0
            )
            balanceDetailByLocStoreTable.items = balancesDetailByLocStoreList
            balanceDetailByLocStoreTable.selectionModel.selectFirst()
            fillFooterDetailBalanceByLocStore()
        }
    }

    /**
     * Открыть выбранный элемент с помощью клавиш.
     */
    private fun openSelectedItemUsingKeyCode() {
        balanceByLocStoreTable.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.UP || ev.code == KeyCode.DOWN) fillDetailTableBalanceByLocStore()
        }
    }

    /**
     * Заполнить подвал таблицы "Остатки".
     */
    private fun fillFooterBalanceByLocStore() {
        rowCountLabel.text = HelperUtil().getThousandSeparator(balanceByLocStoreTable.items.count().toString())
        sumKolLabel.text =
            HelperUtil().getThousandSeparator(balanceByLocStoreTable.items.sumOf { it.balances }.toString())
        sumPiecesLabel.text =
            HelperUtil().getThousandSeparator(balanceByLocStoreTable.items.sumOf { it.pieces }.toString())
    }

    /**
     * Заполнить подвал таблицы "Детализированными остатками".
     */
    private fun fillFooterDetailBalanceByLocStore() {
        rowDetailCountLabel.text =
            HelperUtil().getThousandSeparator(balanceDetailByLocStoreTable.items.count().toString())
        sumDetailKolLabel.text =
            HelperUtil().getThousandSeparator(balanceDetailByLocStoreTable.items.sumOf { it.balances }.toString())
        sumDetailPiecesLabel.text =
            HelperUtil().getThousandSeparator(balanceDetailByLocStoreTable.items.sumOf { it.pieces }.toString())
    }
}