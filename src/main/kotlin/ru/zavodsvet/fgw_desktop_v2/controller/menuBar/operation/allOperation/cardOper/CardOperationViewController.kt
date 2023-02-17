package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper

import javafx.collections.ObservableList
import javafx.event.Event
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
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.LocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.cardSpecOper.CardSpecificationViewController
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateTimeViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.OperationDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.cardOper.CardOperationDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.OperationModel
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper.CardOperationModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Карточка операции".
 */
class CardOperationViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardOperationViewController::class.java)

    /**
     * ИД.
     */
    @FXML
    private lateinit var idLabel: Label

    /**
     * Вид операции.
     */
    @FXML
    private lateinit var vidoperLabel: Label

    /**
     * Номер пропуска.
     */
    @FXML
    private lateinit var numberPermitLabel: Label

    /**
     * Дата создания операции.
     */
    @FXML
    private lateinit var dateCreationOperLabel: Label

    /**
     * Создатель операций.
     */
    @FXML
    private lateinit var employeeCreationOperLabel: Label

    /**
     * Дата создания ордера.
     */
    @FXML
    private lateinit var dateCreationOrderLabel: Label

    /**
     * Создатель ордера.
     */
    @FXML
    private lateinit var employeeCreationOrderLabel: Label

    /**
     * Пункт меню: "Привязать".
     */
    @FXML
    private lateinit var bindPermitMenuItem: MenuItem

    /**
     * Пункт меню: "Отвязать".
     */
    @FXML
    private lateinit var untiePermitMenuItem: MenuItem

    /**
     * Кнопка "Сформировать ордер".
     */
    @FXML
    private lateinit var orderBtn: Button

    /**
     * Окно сцены: "Шаблон даты время".
     */
    @FXML
    private lateinit var templateDateTimeView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты время"
     */
    @FXML
    private lateinit var templateDateTimeViewController: TemplateDateTimeViewController

    /**
     * Таблица "Спецификации операции".
     */
    @FXML
    private lateinit var specOperTable: TableView<CardOperationModel>

    /**
     * Колонка: "Откуда".
     */
    @FXML
    private lateinit var locfromColumn: TableColumn<CardOperationModel, String>

    /**
     * Колонка: "Куда".
     */
    @FXML
    private lateinit var loctoColumn: TableColumn<CardOperationModel, String>

    /**
     * Пункт меню: "Добавить спецификацию операции".
     */
    @FXML
    private lateinit var addSpecOperMenuItem: MenuItem

    /**
     * Пункт меню: "Удалить спецификацию операции".
     */
    @FXML
    private lateinit var deleteSpecOperMenuItem: MenuItem

    /**
     * Пункт меню: "Обновить спецификацию операции".
     */
    @FXML
    private lateinit var updSpecOperMenuItem: MenuItem

    /**
     * Кол-во строк.
     */
    @FXML
    private lateinit var rowCountLabel: Label

    /**
     * Сумма Количества п/п.
     */
    @FXML
    private lateinit var sumKolLabel: Label

    /**
     * Сумма Количества изделий.
     */
    @FXML
    private lateinit var sumPiecesLabel: Label

    /**
     * Общая сумма массы п/п.
     */
    @FXML
    private lateinit var sumMassaLabel: Label

    /**
     * Модель: "Операции".
     */
    private var oper = OperationModel()

    /**
     * Реализация объекта доступа к данным операций "Операции".
     */
    private var operDaoImpl = OperationDaoImpl()

    /**
     * Оповещатель спрашивает (Да или Нет).
     */
    private var alertConfirm = Alert(Alert.AlertType.CONFIRMATION)

    /**
     * Реализация объекта доступа к данным операций "Карточка операции".
     */
    private var cardOperImpl = CardOperationDaoImpl()

    /**
     * Реализация объекта доступа к данным карточки операции: "Спецификация операции".
     */
    private var specOperDaoImpl = CardOperationDaoImpl()

    /**
     * Список "Спецификации операции".
     */
    private lateinit var specOperList: ObservableList<CardOperationModel>

    // Контроллер окна.
    private var windowControllerBindPermitDialog = WindowController()
    private var windowControllerLocStore = WindowController()
    private var windowControllerSpecOper = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        templateDateTimeView.isDisable = true

        contextMenuNumberPermit()
        contextMenuTableSpecOper()

        specOperTable.setOnKeyPressed { kv -> onKeyPressed(kv) }
        specOperTable.setOnMouseClicked { ev -> onClickMouseBySelectedItem(ev) }
    }

    /**
     * Заполнить шапку карточку операции, по ИД операции.
     *
     * @param operId ИД операции.
     */
    fun fillTopCardOper(operId: Int) {
        oper = operDaoImpl.getOperByID(operId)

        idLabel.text = oper.id.toString()
        vidoperLabel.text = oper.vidoper
        numberPermitLabel.text = oper.permit
        dateCreationOperLabel.text = oper.dtcreate
        employeeCreationOperLabel.text = oper.perfcreate
        dateCreationOrderLabel.text = oper.dtord
        employeeCreationOrderLabel.text = oper.perford
        templateDateTimeViewController.fillParseTemplateDateTime(oper.dtn.toString())

        showTableSpecOperByNumOper(oper.oper)
        onClickOrderBtn(oper)
        fillTableSpecOper(operId)
    }

    /**
     * Показать вид таблицы "Спецификации операции" по номеру(виду) операции.
     *
     * @param numOper Вид операции.
     */
    private fun showTableSpecOperByNumOper(numOper: Int) {
        when (numOper) {
            ARRIVAL -> this.locfromColumn.isVisible = false
            DEBIT -> this.loctoColumn.isVisible = false
            SALE -> this.loctoColumn.isVisible = false
            INVENTORY, REGRADING, REPACKING -> {
                this.locfromColumn.isVisible = false
                this.loctoColumn.text = TITLE_WHERE
            }
        }
    }

    /**
     * Контекстное меню: "Пропуск".
     */
    private fun contextMenuNumberPermit() {
        addSpecOperMenuItem.setOnAction { openCardSpecForAdd(oper.oper) }
        bindPermitMenuItem.setOnAction { showBindPermitDialog(oper.id) }
        untiePermitMenuItem.setOnAction { showConfirmUnitePermit(oper) }
    }

    /**
     * По щелчку мыши по выбранному элементу.
     *
     * @param ev Событие.
     */
    private fun onClickMouseBySelectedItem(ev: Event) {
        if (ev is MouseEvent && ev.clickCount == 2 && getSelectedItemSpOper() != null) {
            openCardSpecForEdit(getSelectedItemSpOper())
        }
    }

    /**
     * Пункт контекстного меню: "Добавить".
     *
     * @param numOper Номер операции.
     */
    private fun openCardSpecForAdd(numOper: Int) {
        openCardSpecWindow().initCardSpec(
            oper.id,
            numOper,
            templateDateTimeViewController.getTemplateDateTime(),
            ({ confirm -> if (confirm) fillTableSpecOper(oper.id) })
        )
    }

    /**
     * Открыть карточку спецификации для редактирования.
     *
     * @param specOper Сущность спецификации операции.
     */
    private fun openCardSpecForEdit(specOper: CardOperationModel) {
        openCardSpecWindow().editCardSpecByIdSpec(
            specOper.spoperid, ({ confirm -> if (confirm) fillTableSpecOper(oper.id) })
        )
    }

    /**
     * Открыть окно: "Карточка спецификации".
     */
    private fun openCardSpecWindow(): CardSpecificationViewController {
        windowControllerSpecOper = NewWindow(windowControllerSpecOper).openWindow(
            CARD_SPECIFICATION_VIEW, TITLE_CARD_SPECIFICATION, Modality.APPLICATION_MODAL
        )
        return windowControllerSpecOper.controller as CardSpecificationViewController
    }

    /**
     * Пункт контекстного меню: "Привязать" пропуск.
     *
     * @param operId ИД операции.
     */
    private fun showBindPermitDialog(operId: Int) {
        windowControllerBindPermitDialog = NewWindow(windowControllerBindPermitDialog).openWindow(
            BIND_PERMIT_DIALOG_VIEW, TITLE_BIND_PERMIT, Modality.APPLICATION_MODAL
        )
        val bindPermitWindowController = windowControllerBindPermitDialog.controller as BindPermitDialogViewController
        bindPermitWindowController.confirm { confirm ->
            val barcodePermit = bindPermitWindowController.getBarcodePermit()
            val numberPermit = bindPermitWindowController.getNumberPermit()
            if (confirm) {
                cardOperImpl.addLinkOperWithAutoSkipPermit(operId, barcodePermit, numberPermit)
                fillTopCardOper(operId)
            }
        }
    }

    /**
     * При нажатии на кнопку "Создать"/"Удалить" ордер.
     *
     * @param oper Сущность операции.
     */
    private fun onClickOrderBtn(oper: OperationModel) {
        if (oper.dtord.isNullOrEmpty()) {
            orderBtn.text = TEXT_CREATE_ORDER
            orderBtn.setOnAction { showConfirmOnMakeOrder(oper) }
        } else {
            orderBtn.text = TEXT_DELETE_ORDER
            orderBtn.setOnAction { showConfirmOnDelOrder(oper) }
        }
    }

    /**
     * Показать диалоговое окно отвязки авто-пропуска.
     *
     * @param oper Сущность операции.
     */
    private fun showConfirmUnitePermit(oper: OperationModel) {
        alertConfirm.title = "Подтверждение операции \"Отвязать пропуск\""
        alertConfirm.headerText = "Вы уверены что хотите отвязать пропуск с №: ${oper.permit}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            cardOperImpl.unlinkOperWithAutoSkipPermitByOperId(oper.id)
            fillTopCardOper(oper.id)

            logger.info("Успешно отвязан пропуск с №: ${oper.permit}")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Показать диалоговое окно с подтверждением создания ордера.
     *
     * @param oper Сущность операции.
     */
    private fun showConfirmOnMakeOrder(oper: OperationModel) {
        alertConfirm.title = "Подтверждение операции \"Сформировать ордер\""
        alertConfirm.headerText = "Вы уверены что хотите сформировать ордер для ИД: ${oper.id}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            val fillDateTimeLayer = templateDateTimeViewController.getTemplateDateTime()
            cardOperImpl.makeOrderByOperId(oper.id, auth.tabnum, fillDateTimeLayer)
            fillTopCardOper(oper.id)

            logger.info(MAKE_ORDER_SUCCESS)
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Показать диалоговое окно с подтверждением удаления ордера.
     *
     * @param oper Сущность операции.
     */
    private fun showConfirmOnDelOrder(oper: OperationModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить ордер.\""
        alertConfirm.headerText = "Вы уверены что хотите удалить ордер с ИД: ${oper.id}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            cardOperImpl.delOrderByOperId(oper.id)
            fillTopCardOper(oper.id)

            logger.info("Ордер ${oper.id} удален.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    private var sumMassa = 0;
    /**
     * Заполнить таблицу: "Спецификации операции".
     */
    private fun fillTableSpecOper(operId: Int) {
        specOperTable.items.removeAll()
        specOperList = specOperDaoImpl.findAllSpOperByOperID(operId)

        if (specOperList.isEmpty() && oper.oper == 4) {
            windowControllerLocStore =
                NewWindow(windowControllerLocStore).openWindow(LOC_STORE_VIEW, TITLE_LOC_STORE, null)
            val locStoreController = windowControllerLocStore.controller as LocStoreViewController
            locStoreController.sendSelectedItemLocStore { selectItemLocStore ->
                specOperDaoImpl.fillSpByOperIdForInventory(operId, selectItemLocStore.id)
                specOperList = specOperDaoImpl.findAllSpOperByOperID(operId)
                specOperTable.items = specOperList
                fillFooterCardOper()
            }
        }

        specOperTable.items = specOperList
        fillFooterCardOper()
    }

    /**
     * Контекстное меню таблицы: "Спецификации операции".
     */
    private fun contextMenuTableSpecOper() {
        deleteSpecOperMenuItem.setOnAction {
            if (getSelectedItemSpOper() != null) showConfirmOnDelSpecOper(getSelectedItemSpOper())
        }
        updSpecOperMenuItem.setOnAction { fillTableSpecOper(oper.id) }
    }

    /**
     * При нажатии на клавишу Del.
     *
     * @param kv Ключ событие.
     */
    private fun onKeyPressed(kv: KeyEvent) {
        if (kv.code == KeyCode.DELETE && getSelectedItemSpOper() != null) showConfirmOnDelSpecOper(getSelectedItemSpOper())
        if (kv.code == KeyCode.ENTER && getSelectedItemSpOper() != null) openCardSpecForEdit(getSelectedItemSpOper())
    }

    /**
     * Показать диалоговое окно с подтверждением удалением спецификации.
     *
     * @param specOper "Спецификация операции".
     */
    private fun showConfirmOnDelSpecOper(specOper: CardOperationModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить спецификацию операции\""
        alertConfirm.headerText = "Вы уверены что хотите удалить ИД спецификации: ${specOper.spoperid}?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            specOperDaoImpl.delSpOperDel(specOper.spoperid)
            fillTableSpecOper(oper.id)

            logger.info("Спецификация ${specOper.spoperid} удалена.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Получить выбранный элемент: "Спецификация операции".
     */
    private fun getSelectedItemSpOper() = specOperTable.selectionModel.selectedItem

    /**
     * Заполнить подвал "Карточки операции".
     */
    private fun fillFooterCardOper() {
        sumPiecesLabel.text = HelperUtil().getThousandSeparator(specOperTable.items.sumOf { it.pieces }.toString())
        sumKolLabel.text = HelperUtil().getThousandSeparator(specOperTable.items.sumOf { it.kol }.toString())
        rowCountLabel.text = HelperUtil().getThousandSeparator(specOperTable.items.count().toString())
        sumMassaLabel.text = HelperUtil().getThousandSeparator(specOperTable.items.sumOf { it.massa }.toString())
    }
}