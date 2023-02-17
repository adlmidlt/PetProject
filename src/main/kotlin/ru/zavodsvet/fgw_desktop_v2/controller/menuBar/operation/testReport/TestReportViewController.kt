package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.CardOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport.cardTestReport.CardTestReportViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionDateOfPeriodViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionKDNameViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.TestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport.CardTestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.TestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна протоколы испытаний.
 */
class TestReportViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(TestReportViewController::class.java)

    /**
     * Окно фильтра конструкторской документации.
     */
    @FXML
    private lateinit var selectionKDNameView: AnchorPane

    /**
     * Контроллер окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameViewController: SelectionKDNameViewController

    /**
     * Чек бокс для вкл/выкл даты документов.
     */
    @FXML
    private lateinit var dateCreateDocCheckBox: CheckBox

    /**
     * Окно фильтра периода даты документа.
     */
    @FXML
    private lateinit var selectionDateOfPeriodDocView: AnchorPane

    /**
     * Контроллер окна фильтра периода даты документа.
     */
    @FXML
    private lateinit var selectionDateOfPeriodDocViewController: SelectionDateOfPeriodViewController

    /**
     * Чек бокс для вкл/выкл даты испытаний.
     */
    @FXML
    private lateinit var dateCreateReportCheckBox: CheckBox

    /**
     * Окно фильтра периода даты испытаний.
     */
    @FXML
    private lateinit var selectionDateOfPeriodTestReportView: AnchorPane

    /**
     * Контроллер окна фильтра периода даты испытаний.
     */
    @FXML
    private lateinit var selectionDateOfPeriodTestReportViewController: SelectionDateOfPeriodViewController

    /**
     * Кнопка применить фильтр для "Протоколы испытаний".
     */
    @FXML
    private lateinit var applyFilterBtn: Button

    /**
     * Таблица протокола испытаний.
     */
    @FXML
    private lateinit var testReportTable: TableView<TestReportModel>

    /**
     * Контекстное меню Добавить.
     */
    @FXML
    private lateinit var addContextMenu: MenuItem

    /**
     * Контекстное меню Удалить.
     */
    @FXML
    private lateinit var delContextMenu: MenuItem

    /**
     * Контекстное меню Обновить.
     */
    @FXML
    private lateinit var updContextMenu: MenuItem

    /**
     * Контекстное меню перейти в операцию.
     */
    @FXML
    private lateinit var goToOperContextMenu: MenuItem

    /**
     * Кол-во строк.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Реализация объекта доступа к данным операций "Протоколы операции".
     */
    private var testReportDao = TestReportDaoImpl()

    /**
     * Список протоколов испытаний.
     */
    private var testReportList: ObservableList<TestReportModel> = FXCollections.observableArrayList()

    /**
     * ИД выбранного элемента в таблице.
     */
    private var selectedItemId: Int = 0

    /**
     * Матюгатор спрашивает (Да или Нет).
     */
    private var alertConfirm = Alert(Alert.AlertType.CONFIRMATION)

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Обработчик ресурсов.
     */
    private var resourcesHandler = ResourcesHandlerUtil()

    /**
     * Матюгатор.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Помощник со сценой.
     */
    private var helperScene = HelperWithSceneUtil()

    /**
     * Реализация объекта доступа к данным операций "Карточке протокола операции".
     */
    private var cardTestReportDao = CardTestReportDaoImpl()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        selectionDateOfPeriodTestReportView.isDisable = true

        upperFilter()
        handlerEvents()
    }

    /**
     * Обработчик событий
     */
    private fun handlerEvents() {
        testReportTable.setOnMouseClicked { ev -> openSelectedItemByDoubleClickingMouse(ev) }
        goToOperContextMenu.setOnAction { openSelectedItemThroughContextMenu() }

        addContextMenu.setOnAction {
            val idDoc = testReportDao.addTestReport()
            openWindowCardTestReportWindow().fillTopCardTestReport(idDoc)
            openWindowCardTestReportWindow().updCardTestReport(idDoc) { save ->
                if (save) fillFilterTableTestReport()
            }
        }
        delContextMenu.setOnAction { delCardTestReportContextMenu() }
        updContextMenu.setOnAction { fillFilterTableTestReport() }

        testReportTable.setOnKeyPressed { ev ->
            openSelectedItemUsingEnterKey(ev)
            deleteSelectedItemUsingDeleteKey(ev)
        }
    }

    /**
     * Верхний фильтр.
     */
    private fun upperFilter() {
        selectionKDNameViewController.openWindowSelectKDName()
        dateCreateDocCheckBox.setOnAction {
            selectionDateOfPeriodDocView.isDisable = !dateCreateDocCheckBox.isSelected
        }
        dateCreateReportCheckBox.setOnAction {
            selectionDateOfPeriodTestReportView.isDisable = !dateCreateReportCheckBox.isSelected
        }

        applyFilterBtn.setOnAction { fillFilterTableTestReport() }
    }

    /**
     * Заполнить фильтр при открытии окна протоколы испытаний.
     *
     * @param ddocn Дата документа с ...
     * @param ddock Дата документа по ...
     * @param dtestn Дата испытаний с ...
     * @param dtestk Дата испытаний по ...
     * @param prodid ИД конструкторского наименования.
     */
    fun fillFilterTableTestReport() {
        saveBookMark()
        testReportTable.items.removeAll()
        testReportList = testReportDao.findAllTestReportsWithFilter(
            if (dateCreateDocCheckBox.isSelected) selectionDateOfPeriodDocViewController.getStartDate() else null,
            if (dateCreateDocCheckBox.isSelected) selectionDateOfPeriodDocViewController.getEndDate() else null,
            if (dateCreateReportCheckBox.isSelected) selectionDateOfPeriodTestReportViewController.getStartDate() else null,
            if (dateCreateReportCheckBox.isSelected) selectionDateOfPeriodTestReportViewController.getEndDate() else null,
            selectionKDNameViewController.getKDNameId()
        )
        testReportTable.items = testReportList
        loadBookMark()
        fillFooterBalance()
    }

    /**
     * Открыть выбранный элемент двойным щелчком мыши.
     *
     * @param mouseEvent Событие при нажатии на мышку.
     */
    private fun openSelectedItemByDoubleClickingMouse(mouseEvent: MouseEvent) {
        if (mouseEvent.button.equals(MouseButton.PRIMARY) && mouseEvent.clickCount == 2 && getSelectedItemTestReport() != null) {
            val cardTestReportController = openWindowCardTestReportWindow()
            cardTestReportController.updCardTestReport(getSelectedItemTestReport().id) { save ->
                if (save) fillFilterTableTestReport()
            }
        }
    }

    /**
     * Открыть выбранный элемент с помощью клавиши ввода.
     *
     * @param keyEvent Событие при нажатии на интер.
     */
    private fun openSelectedItemUsingEnterKey(keyEvent: KeyEvent) {
        if (keyEvent.code == KeyCode.ENTER && getSelectedItemTestReport() != null) {
            val cardTestReportController = openWindowCardTestReportWindow()
            cardTestReportController.updCardTestReport(getSelectedItemTestReport().id) { save ->
                if (save) fillFilterTableTestReport()
            }
        }
    }

    /**
     * Удалить карточку протокола испытания. (Контекст меню).
     */
    private fun delCardTestReportContextMenu() {
        if (getSelectedItemTestReport() != null) showConfirmDeleteTestReport(getSelectedItemTestReport())
    }

    /**
     * Удаление при нажатии на клавиатуре клавиши "delete".
     *
     * @param keyEvent Событие на клавишу "delete".
     */
    private fun deleteSelectedItemUsingDeleteKey(keyEvent: KeyEvent) {
        if (keyEvent.code == KeyCode.DELETE && getSelectedItemTestReport() != null) {
            showConfirmDeleteTestReport(getSelectedItemTestReport())
        }
    }

    /**
     * Показать диалоговое окно с подтверждением удалением карточки протокола испытания.
     *
     * @param testReport "Протокола испытаний".
     */
    private fun showConfirmDeleteTestReport(testReport: TestReportModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить протокол испытаний.\""
        alertConfirm.headerText = "Вы уверены что хотите удалить ИД спецификации: ${testReport.id}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            cardTestReportDao.delCardTestReport(testReport.id)
            fillFilterTableTestReport()

            logger.info("Спецификация ${testReport.id} удалена.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    private var cardTestReportWindowController = WindowController()
    private var cardOperWindowController = WindowController()

    /**
     * Открыть карточку протокола испытаний.
     *
     * @param idDoc ИД документа.
     */
    private fun openWindowCardTestReportWindow(): CardTestReportViewController {
        cardTestReportWindowController =
            NewWindow(cardTestReportWindowController).openWindow(CARD_TEST_REPORT_VIEW, TITLE_CARD_TEST_REPORT, null)

        return cardTestReportWindowController.controller as CardTestReportViewController
    }

    /**
     * Открыть окно по выбранному элементу через контекстное меню.
     */
    private fun openSelectedItemThroughContextMenu() {
        cardOperWindowController =
            NewWindow(cardTestReportWindowController).openWindow(CARD_OPERATION_VIEW, TITLE_CARD_OPERATION, null)
        val cardOperController = cardOperWindowController.controller as CardOperationViewController

        if (getSelectedItemTestReport().operid != 0) {
            cardOperController.fillTopCardOper(getSelectedItemTestReport().operid)
        } else {
            eventAlert.alertWarning(OPER_WITH_ZERO_NOT_EXIST)
            cardOperWindowController.stage!!.close()
        }
    }

    /**
     * Запоминает текущую позицию строки в таблице.
     */
    private fun saveBookMark() {
        if (testReportTable.selectionModel.selectedItem != null) {
            selectedItemId = testReportTable.selectionModel.selectedItem.id
        }
    }

    /**
     * Загружает текущую позицию строки в таблице.
     */
    private fun loadBookMark() {
        testReportTable.items
            .filter { it.id == selectedItemId }
            .forEach { testReportTable.selectionModel.select(it) }
    }

    /**
     * Заполнить подвал таблицы "Протоколы испытаний".
     */
    private fun fillFooterBalance() {
        rowCountLabel.text = testReportTable.items.count().toString()
    }

    /**
     * Получить выбранный элемент: "Протокола испытаний".
     */
    private fun getSelectedItemTestReport() = testReportTable.selectionModel.selectedItem
}