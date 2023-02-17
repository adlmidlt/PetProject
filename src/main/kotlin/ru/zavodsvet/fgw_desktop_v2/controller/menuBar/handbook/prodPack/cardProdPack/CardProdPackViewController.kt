package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.control.Tab
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.tabs.BalanceByProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.cardProdPack.tabs.OrderByProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.prodPack.ProdPackDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.ProdPackModel
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Карточка варианта упаковки".
 */
class CardProdPackViewController : Initializable {
    /**
     * Артикул варианта упаковки.
     */
    @FXML
    private lateinit var artLabel: Label

    /**
     * Конструкторское наименование варианта упаковки.
     */
    @FXML
    private lateinit var packnameLabel: Label

    /**
     * Цвет бутылки варианта упаковки.
     */
    @FXML
    private lateinit var colorLabel: Label

    /**
     * Габариты варианта упаковки.
     */
    @FXML
    private lateinit var dwhLabel: Label

    /**
     * Рядность варианта упаковки.
     */
    @FXML
    private lateinit var rowsLabel: Label

    /**
     * Кол-во изделий варианта упаковки.
     */
    @FXML
    private lateinit var couLabel: Label

    /**
     * Кол-во изделий всего.
     */
    @FXML
    private lateinit var totalCouLabel: Label

    /**
     * Таб окна Остатки по варианту упаковки.
     */
    @FXML
    private lateinit var balanceByProdPackTab: Tab

    /**
     * Главное окно Остатков по вариантам упаковки.
     */
    @FXML
    private lateinit var balanceByProdPackView: AnchorPane

    /**
     * Контроллер таба: "Остатки по варианту упаковки".
     */
    @FXML
    private lateinit var balanceByProdPackViewController: BalanceByProdPackViewController

    /**
     * Таб окна Ордера по варианту упаковки.
     */
    @FXML
    private lateinit var orderByProdPackTab: Tab

    /**
     * Главное окно Ордер по вариантам упаковки.
     */
    @FXML
    private lateinit var orderByProdPackView: AnchorPane

    /**
     * Контроллер таба: "Ордер варианта упаковки".
     */
    @FXML
    private lateinit var orderByProdPackViewController: OrderByProdPackViewController

    /**
     * Вариант упаковки.
     */
    private var prodPack = ProdPackModel()

    /**
     * Объект доступа к данным справочника "Варианта упаковки".
     */
    private var prodPackDaoImpl = ProdPackDaoImpl()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        balanceByProdPackTab.content = balanceByProdPackView
        orderByProdPackTab.content = orderByProdPackView
    }

    /**
     * Заполнить шапку: "Карточки варианта упаковки".
     *
     * @param idProdPack ИД варианта упаковки.
     */
    fun fillTopCardProdPackByProdId(idProdPack: Int) {
        prodPack = prodPackDaoImpl.getProdPackByIdProd(idProdPack)

        artLabel.text = prodPack.art
        packnameLabel.text = prodPack.packname
        colorLabel.text = prodPack.color
        dwhLabel.text = prodPack.dwh
        rowsLabel.text = prodPack.rows.toString()
        couLabel.text = prodPack.cou.toString()
        totalCouLabel.text = (prodPack.rows * prodPack.cou).toString()

        balanceByProdPackViewController.fillTableBalanceByProdPack(prodPack.id)
        orderByProdPackViewController.fillTableOrderByProdPack(prodPack.id)
    }
}