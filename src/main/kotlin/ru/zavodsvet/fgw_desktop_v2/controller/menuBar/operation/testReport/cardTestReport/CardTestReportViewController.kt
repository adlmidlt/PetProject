package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport.cardTestReport

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
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.CARD_SP_TEST_REPORT_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_CARD_SP_TEST_REPORT
import ru.zavodsvet.fgw_desktop_v2.common.WRONG_FORMAT_DATA
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport.cardTestReport.cardSpTestReport.CardSpTestReportViewController
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.TestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport.CardTestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport.cardSpTestReport.CardSpTestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.TestReportModel
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.cardTestReport.cardSpTestReport.SpecificationTestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CardTestReportViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardTestReportViewController::class.java)

    /**
     * Номер документа протокола испытаний.
     */
    @FXML
    private lateinit var numberDocTestReportTF: TextField

    /**
     * Шаблон окна Дата.
     */
    @FXML
    private lateinit var templateDateDocView: AnchorPane

    /**
     * Контроллер окна шаблона Даты.
     */
    @FXML
    private lateinit var templateDateDocViewController: TemplateDateViewController

    /**
     * Шаблон окна Дата.
     */
    @FXML
    private lateinit var templateDateTestReportView: AnchorPane

    /**
     * Контроллер окна шаблона Даты.
     */
    @FXML
    private lateinit var templateDateTestReportViewController: TemplateDateViewController

    /**
     * Кнопка сохранить изменения карточки протокола испытания.
     */
    @FXML
    private lateinit var saveCardTestReportBtn: Button

    /**
     * Создать операцию карточки протокола испытания.
     */
    @FXML
    private lateinit var createOperTestReportBtn: Button

    /**
     * Таблица спецификации операции.
     */
    @FXML
    private lateinit var spTestReportTable: TableView<SpecificationTestReportModel>

    /**
     * Контекстное меню добавить.
     */
    @FXML
    private lateinit var addContextMenu: MenuItem

    /**
     * Контекстное меню обновить.
     */
    @FXML
    private lateinit var updContextMenu: MenuItem

    /**
     * Контекстное меню удалить.
     */
    @FXML
    private lateinit var delContextMenu: MenuItem

    /**
     * Кол-во строк в таблице.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Список спецификации протокола испытания.
     */
    private var spTestReportList: ObservableList<SpecificationTestReportModel> = FXCollections.observableArrayList()

    /**
     * Протокол испытаний.
     */
    private var testReport = TestReportModel()

    /**
     * Реализация объекта доступа к данным операций "Протоколы операции".
     */
    private var testReportDao = TestReportDaoImpl()

    /**
     * Реализация объекта доступа к данным операций "Карточке протокола операции".
     */
    private var cardTestReportDao = CardTestReportDaoImpl()

    /**
     * Реализация объекта доступа к данным "Спецификации карточки протокола испытаний".
     */
    private var cardSpTestReportDao = CardSpTestReportDaoImpl()

    /**
     * Форматирование даты.
     */
    private var dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * ИД выбранного элемента в таблице.
     */
    private var selectedItemId: Int = 0

    /**
     * ИД документа для обновления таблицы.
     */
    private var refreshByIdDoc = 0

    /**
     * Матюгатор спрашивает (Да или Нет).
     */
    private var alertConfirm = Alert(Alert.AlertType.CONFIRMATION)

    /**
     * Утилита для обработки ошибок.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Матюгатор.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Помощник со сценой.
     */
    private var helperScene = HelperWithSceneUtil()

    private var cardSpTestReportWindowController = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        addContextMenu.setOnAction { addCardSpTestReportContextMenu() }
        delContextMenu.setOnAction { delCardSpTestReportContextMenu() }
        updContextMenu.setOnAction { fillTableSpTestReport(refreshByIdDoc) }

        spTestReportTable.setOnMouseClicked { ev -> openSelectedItemByDoubleClickingMouse(ev) }
        spTestReportTable.setOnKeyPressed { ev ->
            openSelectedItemUsingEnterKey(ev)
            deleteSelectedItemUsingDeleteKey(ev)
        }
    }

    /**
     * Заполнить шапку карточки протокола испытаний.
     *
     * @param idDoc Ид документа.
     */
    fun fillTopCardTestReport(idDoc: Int) {
        testReport = testReportDao.getTestReportById(idDoc)

        numberDocTestReportTF.text = testReport.num
        templateDateDocViewController.fillParseTemplateDate(testReport.ddoc)
        templateDateTestReportViewController.fillParseTemplateDate(testReport.dtest)
        refreshByIdDoc = testReport.id
        fillTableSpTestReport(testReport.id)
    }

    /**
     * Заполнить таблицу спецификациями протокола испытания.
     *
     * @param idDoc ИД документа.
     */
    private fun fillTableSpTestReport(idDoc: Int) {
        saveBookMark()
        spTestReportTable.items.removeAll()
        spTestReportList = cardTestReportDao.findAllSpTestReportByIdDoc(idDoc)

        spTestReportTable.items = spTestReportList
        loadBookMark()
        fillFooterBalance()
    }

    /**
     * Обновить карточку протокола испытаний.
     *
     * @param idDoc ИД документа.
     */
    fun updCardTestReport(idDoc: Int, callback: (Boolean) -> Unit) {
        fillTopCardTestReport(idDoc)
        saveCardTestReportBtn.setOnAction { ev ->
            try {
                cardTestReportDao.updTestReport(
                    docid = refreshByIdDoc,
                    num = numberDocTestReportTF.text,
                    ddoc = templateDateDocViewController.getTemplateDate(),
                    dtest = templateDateTestReportViewController.getTemplateDate(),
                )
                helperScene.closeStage(ev)
                callback(true)
            } catch (eNFE: NullPointerException) {
                logger.error(eNFE.message)
                exceptionHandler.printStackTraceElem(eNFE, "updCardTestReport", "${eNFE.message}")
                eventAlert.alertError(WRONG_FORMAT_DATA)
            }
        }
    }

    /**
     * Добавить карточку спецификациями протокола испытания. (Контекст меню).
     */
    private fun addCardSpTestReportContextMenu() {
        openWindowSpCardTestReportWindow().addCardSpTestReport(refreshByIdDoc) { save ->
            if (save) fillTableSpTestReport(refreshByIdDoc)
        }
    }

    /**
     * Удалить карточку спецификациями протокола испытания. (Контекст меню).
     */
    private fun delCardSpTestReportContextMenu() {
        val spTestReport = spTestReportTable.selectionModel.selectedItem
        if (spTestReport != null) {
            showConfirmDeleteSpTestReport(spTestReport)
        }
    }

    /**
     * Открыть выбранный элемент с помощью клавиши ввода.
     *
     * @param keyEvent Событие при нажатии на интер.
     */
    private fun openSelectedItemUsingEnterKey(keyEvent: KeyEvent) {
        if (keyEvent.code == KeyCode.ENTER && getSelectedItemCardSpTestReport() != null) {
            openWindowSpCardTestReportWindow().updCardSpTestReport(getSelectedItemCardSpTestReport().id) { save ->
                if (save) fillTableSpTestReport(refreshByIdDoc)
            }
        }
    }

    /**
     * Удаление при нажатии на клавиатуре клавиши "delete".
     *
     * @param keyEvent Событие на клавишу "delete".
     */
    private fun deleteSelectedItemUsingDeleteKey(keyEvent: KeyEvent) {
        if (keyEvent.code == KeyCode.DELETE && getSelectedItemCardSpTestReport() != null) {
            showConfirmDeleteSpTestReport(getSelectedItemCardSpTestReport())
        }
    }

    /**
     * Показать диалоговое окно с подтверждением удалением спецификации.
     *
     * @param spTestReport "Спецификация протокола испытаний".
     */
    private fun showConfirmDeleteSpTestReport(spTestReport: SpecificationTestReportModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить спецификацию протокола испытаний.\""
        alertConfirm.headerText = "Вы уверены что хотите удалить ИД спецификации: ${spTestReport.id}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            cardSpTestReportDao.delCardSpTestReport(spTestReport.id)
            fillTableSpTestReport(refreshByIdDoc)

            logger.info("Спецификация ${spTestReport.id} удалена.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Открыть выбранный элемент двойным щелчком мыши.
     *
     * @param mouseEvent Событие при нажатии на мышку.
     */
    private fun openSelectedItemByDoubleClickingMouse(mouseEvent: MouseEvent) {
        if (mouseEvent.button.equals(MouseButton.PRIMARY) && mouseEvent.clickCount == 2 && getSelectedItemCardSpTestReport() != null) {
            openWindowSpCardTestReportWindow().updCardSpTestReport(getSelectedItemCardSpTestReport().id) { save ->
                if (save) fillTableSpTestReport(refreshByIdDoc)
            }
        }
    }

    /**
     * Открыть карточку спецификации протокола испытаний.
     */
    private fun openWindowSpCardTestReportWindow(): CardSpTestReportViewController {
        cardSpTestReportWindowController = NewWindow(cardSpTestReportWindowController).openWindow(
            CARD_SP_TEST_REPORT_VIEW,
            TITLE_CARD_SP_TEST_REPORT,
            Modality.NONE
        )
        return cardSpTestReportWindowController.controller as CardSpTestReportViewController
    }

    /**
     * Заполнить подвал таблицы "Остатки".
     */
    private fun fillFooterBalance() {
        rowCountLabel.text = spTestReportTable.items.count().toString()
    }

    /**
     * Запоминает текущую позицию строки в таблице.
     */
    private fun saveBookMark() {
        if (spTestReportTable.selectionModel.selectedItem != null) {
            selectedItemId = spTestReportTable.selectionModel.selectedItem.id
        }
    }

    /**
     * Загружает текущую позицию строки в таблице.
     */
    private fun loadBookMark() {
        spTestReportTable.items
            .filter { it.id == selectedItemId }
            .forEach { spTestReportTable.selectionModel.select(it) }
    }

    /**
     * Получить выбранный элемент: "Карточки спецификации протокола испытаний".
     */
    private fun getSelectedItemCardSpTestReport() = spTestReportTable.selectionModel.selectedItem
}