package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.CardOperationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.*
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.OperationDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.OperationModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Все операции".
 */
class AllOperationViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(AllOperationViewController::class.java)

    /**
     * Окно сцены: "Выбор вид операции".
     */
    @FXML
    private lateinit var selectionVidOperView: AnchorPane

    /**
     * Контроллер окна: "Выбор вид операции".
     */
    @FXML
    private lateinit var selectionVidOperViewController: SelectionVidOperViewController

    /**
     * Окно сцены: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameView: AnchorPane

    /**
     * Контроллер окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameViewController: SelectionKDNameViewController

    /**
     * Окно сцены: "Выбор варианта упаковки".
     */
    @FXML
    private lateinit var selectionProdPackView: AnchorPane

    /**
     * Контроллер окна: "Выбор варианта упаковки".
     */
    @FXML
    private lateinit var selectionProdPackViewController: SelectionProdPackViewController

    /**
     * Вкл/выкл даты создания операции.
     */
    @FXML
    private lateinit var dateCreationOperCheckBox: CheckBox

    /**
     * Окно сцены: "Шаблон выбора периода даты".
     */
    @FXML
    private lateinit var selectionDateOfPeriodOperView: AnchorPane

    /**
     * Контроллер окна: "Шаблон выбора периода даты".
     */
    @FXML
    private lateinit var selectionDateOfPeriodOperViewController: SelectionDateOfPeriodViewController

    /**
     * Окно сцены: "Выбор сотрудника".
     */
    @FXML
    private lateinit var selectionEmployeeOperView: AnchorPane

    /**
     * Контроллер окна: "Выбор сотрудника".
     */
    @FXML
    private lateinit var selectionEmployeeOperViewController: SelectionEmployeeViewController

    /**
     * Вкл/выкл даты создания ордера.
     */
    @FXML
    private lateinit var dateCreationOrderCheckBox: CheckBox

    /**
     * Окно сцены: "Шаблон выбора периода даты".
     */
    @FXML
    private lateinit var selectionDateOfPeriodOrderView: AnchorPane

    /**
     * Контроллер окна: "Шаблон выбора периода даты".
     */
    @FXML
    private lateinit var selectionDateOfPeriodOrderViewController: SelectionDateOfPeriodViewController

    /**
     * Окно сцены: "Выбор сотрудника".
     */
    @FXML
    private lateinit var selectionEmployeeOrderView: AnchorPane

    /**
     * Контроллер окна: "Выбор сотрудника".
     */
    @FXML
    private lateinit var selectionEmployeeOrderViewController: SelectionEmployeeViewController

    /**
     * Кнопка: "Применить фильтр".
     */
    @FXML
    private lateinit var applyFilterBtn: Button

    /**
     * Таблица: "Операции".
     */
    @FXML
    private lateinit var operTable: TableView<OperationModel>

    /**
     * Пункт контекстного меню добавить операцию: "Приход".
     */
    @FXML
    private lateinit var arrivalMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Перемещение".
     */
    @FXML
    private lateinit var movementMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Списание".
     */
    @FXML
    private lateinit var debitMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Продажа".
     */
    @FXML
    private lateinit var saleMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Инвентаризация".
     */
    @FXML
    private lateinit var inventoryMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Пересортица".
     */
    @FXML
    private lateinit var reGradingMenuItem: MenuItem

    /**
     * Пункт контекстного меню добавить операцию: "Перетарка".
     */
    @FXML
    private lateinit var rePackingMenuItem: MenuItem

    /**
     * Пункт контекстного меню: "Обновить все операции".
     */
    @FXML
    private lateinit var updAllOperMenuItem: MenuItem

    /**
     * Пункт контекстного меню: "Удалить операцию".
     */
    @FXML
    private lateinit var delOperMenuItem: MenuItem

    /**
     * Список операции.
     */
    private var operList: ObservableList<OperationModel> = FXCollections.observableArrayList()

    /**
     * Реализация объекта доступа к данным операций "Операции".
     */
    private var operDaoImp = OperationDaoImpl()

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
     * ИД выбранного элемента в таблице.
     */
    private var selectedItemId: Int = 0

    /**
     * Оповещатель спрашивает (Да или Нет).
     */
    private var alertConfirm = Alert(Alert.AlertType.CONFIRMATION)

    // Контролеры окон.
    private var windowControllerDateTimeDialog = WindowController()
    private var windowControllerCardOper = WindowController()

    /**
     * Инициализировать сцены.
     */
    private fun initAnchorPane() {
        selectionVidOperView
        selectionKDNameView
        selectionProdPackView
        selectionEmployeeOperView
        selectionEmployeeOrderView
    }

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        selectionDateOfPeriodOrderView.isDisable = true

        initAnchorPane()
        fillFilterAllOper()
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        contextMenuTableAddOper()

        operTable.setOnMouseClicked { ev -> onMouseClicked(ev) }
        operTable.setOnKeyPressed { kv -> onKeyPressed(kv) }
    }

    /**
     * Заполнить таблицу: "Операции по фильтру".
     */
    fun fillTableOperByFilter() {
        saveBookMark()
        operTable.items.removeAll()

        operList = operDaoImp.getAllOperationsPallets(
            selectionVidOperViewController.getVidOperKod(),
            selectionKDNameViewController.getKDNameId(),
            selectionProdPackViewController.getProdPackId(),
            selectionEmployeeOperViewController.getEmployeeId(),
            if (dateCreationOperCheckBox.isSelected) selectionDateOfPeriodOperViewController.getStartDate() else null,
            if (dateCreationOperCheckBox.isSelected) selectionDateOfPeriodOperViewController.getEndDate() else null,
            selectionEmployeeOrderViewController.getEmployeeId(),
            if (dateCreationOrderCheckBox.isSelected) selectionDateOfPeriodOrderViewController.getStartDate() else null,
            if (dateCreationOrderCheckBox.isSelected) selectionDateOfPeriodOrderViewController.getEndDate() else null,
        )
        operTable.items = operList
        operTable.selectionModel.selectFirst()
        loadBookMark()
        fillFooterTableOper()
    }

    /**
     * Заполнить фильтр: "Все операции".
     */
    private fun fillFilterAllOper() {
        selectionVidOperViewController.openWindowSelectVidOper()
        selectionKDNameViewController.openWindowSelectKDName()
        selectionProdPackViewController.openWindowSelectProdPack()

        dateCreationOperCheckBox.setOnAction {
            selectionDateOfPeriodOperView.isDisable = !dateCreationOperCheckBox.isSelected
        }
        selectionEmployeeOperViewController.openWindowSelectVidOper()

        dateCreationOrderCheckBox.setOnAction {
            selectionDateOfPeriodOrderView.isDisable = !dateCreationOrderCheckBox.isSelected
        }
        selectionEmployeeOrderViewController.openWindowSelectVidOper()

        applyFilterBtn.setOnAction { fillTableOperByFilter() }
    }

    /**
     * Контекстное меню таблицы: "Добавить операцию".
     */
    private fun contextMenuTableAddOper() {
        arrivalMenuItem.setOnAction { showDateTimeDialog(ARRIVAL) }
        movementMenuItem.setOnAction { showDateTimeDialog(MOVEMENT) }
        debitMenuItem.setOnAction { showDateTimeDialog(DEBIT) }
        saleMenuItem.setOnAction { showDateTimeDialog(SALE) }
        inventoryMenuItem.setOnAction { showDateTimeDialog(INVENTORY) }
        reGradingMenuItem.setOnAction { showDateTimeDialog(REGRADING) }
        rePackingMenuItem.setOnAction { showDateTimeDialog(REPACKING) }

        delOperMenuItem.setOnAction { delSelectedItem() }
        updAllOperMenuItem.setOnAction { fillTableOperByFilter() }
    }

    /**
     * Открыть диалоговое окно дата время по коду операции.
     *
     * @param numbOper Код операции.
     */
    private fun showDateTimeDialog(numbOper: Int) {
        windowControllerDateTimeDialog = NewWindow(windowControllerDateTimeDialog).openWindow(
            DATE_TIME_DIALOG_VIEW, TITLE_CREATE_OPERATION, Modality.APPLICATION_MODAL
        )
        val dateTimeDialogController = windowControllerDateTimeDialog.controller as DateTimeDialogViewController
        dateTimeDialogController.confirm { confirm ->
            val dateTime = dateTimeDialogController.getDateTime()
            if (confirm) {
                val operId = operDaoImp.addOper(numbOper, dateTime)
                if (operId != 0) {
                    showOperCardOperById(operId)
                    fillTableOperByFilter()
                }
            }
        }
    }

    /**
     * Открыть карточку операции по ИД операции.
     *
     * @param operId ИД операции.
     */
    private fun showOperCardOperById(operId: Int) {
        windowControllerCardOper =
            NewWindow(WindowController(null, null)).openWindow(CARD_OPERATION_VIEW, TITLE_CARD_OPERATION, Modality.NONE)
        val cardOperController = windowControllerCardOper.controller as CardOperationViewController
        cardOperController.fillTopCardOper(operId)
    }

    /**
     * Удалить выбранный элемент.
     */
    private fun delSelectedItem() {
        if (getSelectItemOper() != null) showConfirmOnDelOper(getSelectItemOper())
    }

    /**
     * При нажатии на клавишу Del.
     *
     * @param kv Ключ событие.
     */
    private fun onKeyPressed(kv: KeyEvent) {
        if (kv.code == KeyCode.DELETE && getSelectItemOper() != null) showConfirmOnDelOper(getSelectItemOper())
        if (kv.code == KeyCode.ENTER && getSelectItemOper() != null) showOperCardOperById(getSelectItemOper().id)
    }

    /**
     * При щелчке мыши.
     *
     * @param ev Событие мыши.
     */
    private fun onMouseClicked(ev: MouseEvent) {
        if (ev.clickCount == 2 && getSelectItemOper() != null) showOperCardOperById(getSelectItemOper().id)
    }


    /**
     * Показать диалоговое окно с подтверждением удаления операции.
     *
     * @param allOper Все операции.
     */
    private fun showConfirmOnDelOper(allOper: OperationModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить операцию\""
        alertConfirm.headerText = "Вы уверены, что хотите удалить операцию с ИД: ${allOper.id}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            operDaoImp.delOper(allOper.id)
            fillTableOperByFilter()

            logger.info("Операция ${allOper.id} удален.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Получить выбранный элемент: "Операции".
     */
    private fun getSelectItemOper() = operTable.selectionModel.selectedItem

    /**
     * Запоминает текущую позицию строки в таблице.
     */
    private fun saveBookMark() {
        if (operTable.selectionModel.selectedItem != null) {
            selectedItemId = operTable.selectionModel.selectedItem.id
        }
    }

    /**
     * Загружает текущую позицию строки в таблице.
     */
    private fun loadBookMark() {
        operTable.items.filter { it.id == selectedItemId }.forEach { operTable.selectionModel.select(it) }
    }

    /**
     * Заполнить подвал таблицы: "Операции".
     */
    private fun fillFooterTableOper() {
        rowCountLabel.text = operTable.items.count().toString()
        sumPiecesLabel.text = HelperUtil().getThousandSeparator(operTable.items.sumOf { it.pieces }.toString())
    }
}