package ru.zavodsvet.fgw_desktop_v2.controller.menuBar

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.MenuItem
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.analitycs.balances.BalancesViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.analitycs.turnoverSheet.TurnoverSheetViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.kdName.KDNameViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.LocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.ProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.vidOper.VidOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.AllOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.manufacturingProduct.ManufacturingProductViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport.TestReportViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.service.ChangePasswordViewController
import ru.zavodsvet.fgw_desktop_v2.dao.login.AuthDaoImpl
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Контроллер меню бара.
 */
class MenuBarViewController : Initializable {
    /**
     * Пункт меню операции: "Все операции".
     */
    @FXML
    private lateinit var allOperItem: MenuItem

    /**
     * Пункт меню операции: "Производство продукции".
     */
    @FXML
    private lateinit var manufacturingProductItem: MenuItem

    /**
     * Пункт меню операции: "Протокол испытаний".
     */
    @FXML
    private lateinit var testReportItem: MenuItem

    /**
     * Пункт меню аналитики: "Остатки".
     */
    @FXML
    private lateinit var balancesItem: MenuItem

    /**
     * Пункт меню аналитики: "Оборотная ведомость".
     */
    @FXML
    private lateinit var turnoverSheetItem: MenuItem


    /**
     * Пункт меню справочника: "Участки хранения".
     */
    @FXML
    private lateinit var locStoreItem: MenuItem

    /**
     * Пункт меню справочника: "Варианты упаковки".
     */
    @FXML
    private lateinit var prodPackItem: MenuItem

    /**
     * Пункт меню справочника: "Конструкторские наименования".
     */
    @FXML
    private lateinit var kdNameItem: MenuItem

    /**
     * Пункт меню справочника: "Виды операции".
     */
    @FXML
    private lateinit var vidOperItem: MenuItem

    /**
     * Пункт меню сервиса: "Изменить пароль".
     */
    @FXML
    private lateinit var changePasswdItem: MenuItem

    /**
     * Реализация объекта доступа к данным авторизации сотрудника.
     */
    private var authDaoImpl = AuthDaoImpl()

    /**
     * Текущая дата в формате ("yyyy-MM-dd").
     */
    private var currentDate = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now())

    private var windowControllerAllOper = WindowController()
    private var windowControllerManufacturingProduct = WindowController()
    private var windowControllerTestReport = WindowController()

    private var windowControllerBalances = WindowController()
    private var windowControllerTurnoverSheet = WindowController()

    private var windowControllerLocStore = WindowController()
    private var windowControllerKDName = WindowController()
    private var windowControllerVidOper = WindowController()
    private var windowControllerProdPack = WindowController()

    private var windowControllerChangePasswd = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        allOperItem.setOnAction { openMenuItemAllOper() }
        manufacturingProductItem.setOnAction { openMenuItemManufacturingProduct() }
        testReportItem.setOnAction { openMenuItemTestReport() }

        balancesItem.setOnAction { openMenuItemBalances() }
        turnoverSheetItem.setOnAction { openMenuItemTurnoverSheet() }

        locStoreItem.setOnAction { openMenuItemLocStore() }
        prodPackItem.setOnAction { openMenuItemProdPack() }
        kdNameItem.setOnAction { openMenuItemKDName() }
        vidOperItem.setOnAction { openMenuItemVidOper() }

        changePasswdItem.setOnAction { openMenuItemChangePasswd() }
    }

    /**
     * Открыть пункт меню: "Все операции".
     */
    private fun openMenuItemAllOper() {
        windowControllerAllOper =
            NewWindow(windowControllerAllOper).openWindow(ALL_OPERATION_VIEW, TITLE_ALL_OPERATION, Modality.NONE)
        val allOperController = windowControllerAllOper.controller as AllOperationViewController
        allOperController.fillTableOperByFilter()
    }

    /**
     * Открыть пункт меню: "Производство продукции".
     */
    private fun openMenuItemManufacturingProduct() {
        windowControllerManufacturingProduct = NewWindow(windowControllerManufacturingProduct).openWindow(
            MANUFACTURING_PRODUCT_VIEW, TITLE_MANUFACTURING_PRODUCT, Modality.NONE
        )
        val manufacturingProductController =
            windowControllerManufacturingProduct.controller as ManufacturingProductViewController

        val startDateTime = "$currentDate 00:00"
        val endDateTime = "$currentDate 23:59"
        manufacturingProductController.fillTableManufacturingProduct(startDateTime, endDateTime, null)
    }

    /**
     * Открыть пункт меню: "Протокол испытаний".
     */
    private fun openMenuItemTestReport() {
        windowControllerTestReport =
            NewWindow(windowControllerTestReport).openWindow(TEST_REPORT_VIEW, TITLE_TEST_REPORT, Modality.NONE)
        val testReportController = windowControllerTestReport.controller as TestReportViewController
        testReportController.fillFilterTableTestReport()
    }

    /**
     * Открыть пункт меню: "Остатки".
     */
    private fun openMenuItemBalances() {
        windowControllerBalances =
            NewWindow(WindowController(null, null)).openWindow(BALANCES_VIEW, TITLE_BALANCES, Modality.NONE)
        val balancesController = windowControllerBalances.controller as BalancesViewController
        balancesController.fillTableBalances()
    }

    /**
     * Открыть пункт меню: "Оборотная ведомость".
     */
    private fun openMenuItemTurnoverSheet() {
        windowControllerTurnoverSheet =
            NewWindow(windowControllerTurnoverSheet).openWindow(TURNOVER_SHEET_VIEW, TITLE_TURNOVER_SHEET, Modality.NONE)
        windowControllerTurnoverSheet.stage?.scene?.stylesheets?.add("css/tree_table_view.css")
        val turnoverSheetController = windowControllerTurnoverSheet.controller as TurnoverSheetViewController
        val startDateTime = "$currentDate 00:00"
        val endDateTime = "$currentDate 23:59"
        turnoverSheetController.fillTableTurnoverSheet(startDateTime, endDateTime)
    }

    /**
     * Открыть пункт меню: "Участки хранения".
     */
    private fun openMenuItemLocStore() {
        windowControllerLocStore = NewWindow(windowControllerLocStore).openWindow(LOC_STORE_VIEW, TITLE_LOC_STORE, Modality.NONE)
        val locStoreController = windowControllerLocStore.controller as LocStoreViewController
        locStoreController.fillTreeTableLocStore()
    }

    /**
     * Открыть пункт меню: "Варианты упаковки".
     */
    private fun openMenuItemProdPack() {
        windowControllerProdPack = NewWindow(windowControllerProdPack).openWindow(PROD_PACK_VIEW, TITLE_PROD_PACK, Modality.NONE)
        val prodPackController = windowControllerProdPack.controller as ProdPackViewController
        prodPackController.fillTableProdPack()
    }

    /**
     * Открыть пункт меню: "Конструкторские наименования".
     */
    private fun openMenuItemKDName() {
        windowControllerKDName = NewWindow(windowControllerKDName).openWindow(KD_NAME_VIEW, TITLE_KD_NAME, Modality.NONE)
        val kdNameController = windowControllerKDName.controller as KDNameViewController
        kdNameController.fillTableKDName()
    }

    /**
     * Открыть пункт меню: "Виды операции".
     */
    private fun openMenuItemVidOper() {
        windowControllerVidOper =
            NewWindow(windowControllerVidOper).openWindow(VID_OPERATION_VIEW, TITLE_VID_OPERATION, Modality.NONE)
        windowControllerVidOper.stage?.isResizable = false
        val vidOperController = windowControllerVidOper.controller as VidOperationViewController
        vidOperController.fillTableVidOper()
    }

    /**
     * Открыть пункт меню: "Изменить пароль".
     */
    private fun openMenuItemChangePasswd() {
        windowControllerChangePasswd =
            NewWindow(windowControllerChangePasswd).openWindow(CHANGE_PASSWD_VIEW, TITLE_CHANGE_PASSWORD, Modality.NONE)
        val changePasswdController = windowControllerChangePasswd.controller as ChangePasswordViewController
        changePasswdController.confirmPasswd { confirm ->
            if (confirm) {
                val newPasswd = changePasswdController.getInputPasswd()
                authDaoImpl.setNewPasswd(auth.tabnum, newPasswd)
            }
        }
    }
}