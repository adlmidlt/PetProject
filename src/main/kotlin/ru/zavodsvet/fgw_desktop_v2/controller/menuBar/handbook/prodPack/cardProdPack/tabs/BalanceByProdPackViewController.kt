package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.tabs

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.control.TableView
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.balances.BalancesDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances.BalancesModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil

/**
 * Контроллер таба: "Остатки по варианту упаковки".
 */
class BalanceByProdPackViewController {
    /**
     * Таблица: "Остатки".
     */
    @FXML
    private lateinit var balanceTable: TableView<BalancesModel>

    /**
     * Объект доступа к данным аналитики^ "Остатки".
     */
    private var balanceDaoImpl = BalancesDaoImpl()

    /**
     * Список остатков по варианту упаковки.
     */
    private lateinit var balanceList: ObservableList<BalancesModel>

    /**
     * Рядность.
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

    fun fillTableBalanceByProdPack(packId: Int) {
        balanceTable.items.removeAll()
        balanceList = FXCollections.observableArrayList()
        balanceDaoImpl.findAllBalancesPallets(balanceList, null, packId, null, null, null, 0, 0, 0, 1)

        balanceTable.items = balanceList
        fillFooterBalance()
    }

    /**
     * Заполнить подвал таблицы "Остатки".
     */
    private fun fillFooterBalance() {
        rowCountLabel.text = HelperUtil().getThousandSeparator(balanceTable.items.count().toString())
        sumKolLabel.text = HelperUtil().getThousandSeparator(balanceTable.items.sumOf { it.balances }.toString())
        sumPiecesLabel.text = HelperUtil().getThousandSeparator(balanceTable.items.sumOf { it.pieces }.toString())
    }
}