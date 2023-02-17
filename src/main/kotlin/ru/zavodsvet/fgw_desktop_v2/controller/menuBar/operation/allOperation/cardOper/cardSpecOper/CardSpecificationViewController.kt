package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.cardSpecOper

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.stage.Modality
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateFormat
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.analitycs.balances.BalancesViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.LocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.prodPack.ProdPackViewController
import ru.zavodsvet.fgw_desktop_v2.controller.template.TemplateDateViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.allOperation.cardOper.cardSpec.CardSpecificationDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances.BalancesModel
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.ProdPackModel
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper.cardSpec.CardSpecificationModel
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeParseException
import java.util.*

/**
 * Контроллер окна: "Карточка спецификации".
 */
class CardSpecificationViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardSpecificationViewController::class.java)

    /**
     * Главное окно "Карточка спецификации".
     */
    @FXML
    private lateinit var cardSpecMainWindow: AnchorPane

    /**
     * Конструкторское наименование(вариант упаковки).
     */
    @FXML
    private lateinit var packnameLabel: Label

    /**
     * Артикул.
     */
    @FXML
    private lateinit var artLabel: Label

    /**
     * Рядность.
     */
    @FXML
    private lateinit var rowsLabel: Label

    /**
     * Кол-во изделий.
     */
    @FXML
    private lateinit var couLabel: Label

    /**
     * Цвет.
     */
    @FXML
    private lateinit var colorLabel: Label

    /**
     * Кнопка выбора: "Варианта упаковки".
     */
    @FXML
    private lateinit var selectionBtn: Button

    /**
     * Окно сцены: "Шаблон даты".
     */
    @FXML
    private lateinit var templateDateView: AnchorPane

    /**
     * Контроллер окна шаблона: "Дата время".
     */
    @FXML
    private lateinit var templateDateViewController: TemplateDateViewController

    /**
     * Поле: "Кол-во п/п".
     */
    @FXML
    private lateinit var kolTF: TextField

    /**
     * Поле: "Кол-во изделий".
     */
    @FXML
    private lateinit var piecesTF: TextField

    /**
     * Кнопка: "Сбросить "Кол-во п/п" на старое значение".
     */
    @FXML
    private lateinit var resetKolOnOldBtn: Button

    /**
     * "ОТКУДА".
     */
    @FXML
    private lateinit var fromtoLabel: Label

    /**
     * Поле: "ОТКУДА".
     */
    @FXML
    private lateinit var fromtoTF: TextField

    /**
     * Кнопка выбора: "ОТКУДА".
     */
    @FXML
    private lateinit var selectionFromtoBtn: Button

    /**
     * "КУДА".
     */
    @FXML
    private lateinit var loctoLabel: Label

    /**
     * Поле: "КУДА".
     */
    @FXML
    private lateinit var loctoTF: TextField

    /**
     * Кнопка выбора: "КУДА".
     */
    @FXML
    private lateinit var selectionLoctoBtn: Button

    /**
     * Кнопка: "Добавить".
     */
    @FXML
    private lateinit var okBtn: Button

    /**
     * Кнопка: "Отмена".
     */
    @FXML
    private lateinit var cancelBtn: Button

    /**
     * Карточка спецификации.
     */
    private var cardSpec: CardSpecificationModel = CardSpecificationModel()

    /**
     * Реализация объекта доступа к данным: "Карточка спецификации".
     */
    private var cardSpecDaoImpl = CardSpecificationDaoImpl()

    /**
     * Оповещение о событии.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Валидация текстового поля.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    private var windowControllerProdPack = WindowController()
    private var windowControllerLocStore = WindowController()
    private var windowControllerDirectDialog = WindowController()
    private var windowControllerBalances = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        piecesTF.isDisable = true
        fromtoTF.isDisable = true
        loctoTF.isDisable = true

        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        resetKolOnOldBtn.setOnAction { kolTF.text = cardSpec.kol.toString() }

        kolTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputNumber(kolTF, newValue)
            editPiecesTF(newValue)
        }
    }

    /**
     * Инициализация карточки спецификации при открытии.
     *
     * @param operId ИД операции.
     * @param numOper Номер операции.
     * @param dateTimeBalance Остатки на дату время.
     * @param callback Обратный вызов.
     */
    fun initCardSpec(operId: Int, numOper: Int, dateTimeBalance: String, callback: (Boolean) -> Unit) {
        clearCardSpec()
        cardSpec = CardSpecificationModel()

        if (cardSpec.dtbal == null) cardSpec.dtbal = dateTimeBalance

        if (numOper == REGRADING || numOper == REPACKING) {
            openDirectDialogForSelectionDebitOrArrival(numOper)
        } else {
            showCardSpecByNumOper(numOper)
        }

        showCardSpecByNumOper(numOper)
        cardSpec.operid = operId
        templateDateViewController.getTemplateDate()

        confirm(callback)
    }

    /**
     * Редактировать: "Карточку спецификации" по ИД спецификации.
     *
     * @param specId ИД спецификации.
     * @param callback Нажатие кнопки.
     */
    fun editCardSpecByIdSpec(specId: Int, callback: (Boolean) -> Unit) {
        fillTopCardSpec(specId)
        okBtn.setOnAction { ev ->
            try {
                val fillDate = templateDateViewController.getTemplateDate()
                LocalDate.parse(fillDate, currentDateFormat)

                cardSpecDaoImpl.updSpOperEditV2(
                    spoper = cardSpec.spoperid,
                    objid = cardSpec.packid,
                    layer = fillDate,
                    locfrom = cardSpec.locfromid.toInt(),
                    locto = cardSpec.loctoid.toInt(),
                    kol = kolTF.text.toInt()
                )
                callback(true)
                helperWithScene.closeStage(ev)
            } catch (eNFE: NumberFormatException) {
                logger.error(eNFE.message)
                exceptionHandler.printStackTraceElem(eNFE, "updateCardSpecByIdSpec", "${eNFE.message}")
                eventAlert.alertError(WRONG_FORMAT_DATA)
            } catch (eDTP: DateTimeParseException) {
                logger.error(eDTP.message)
                exceptionHandler.printStackTraceElem(eDTP, "updateCardSpecByIdSpec", "${eDTP.message}")
                eventAlert.alertError(WRONG_FORMAT_DATA)
            }
        }
        cancelBtn.setOnAction { ev -> helperWithScene.closeStage(ev) }
    }

    /**
     * Подтверждение операции добавление карточки спецификации.
     *
     * @param callback Обратный вызов.
     */
    private fun confirm(callback: (Boolean) -> Unit) {
        okBtn.setOnAction { ev -> onClickAddCardSpecBtn(ev, callback) }
        cancelBtn.setOnAction { ev -> helperWithScene.closeStage(ev) }
    }

    /**
     * Редактировать поле: "Изделия".
     *
     * @param newValue Новое значение.
     */
    private fun editPiecesTF(newValue: String) {
        if (newValue == "" || newValue == "-") {
            piecesTF.text = "0"
        } else {
            try {
                piecesTF.text = "${newValue.toInt() * cardSpec.rows * cardSpec.cou}"
            } catch (eNFE: NumberFormatException) {
                logger.warn(eNFE.message)
            }
        }
    }

    /**
     * Открыть окно выбора: "Приход" или "Списание".
     */
    private fun openDirectDialogForSelectionDebitOrArrival(numOper: Int) {
        openDirectDialogWindow().dialogWindowChoice { direct ->
            if (direct) cardSpec.direct = true
            showCardSpecByNumOper(numOper)
        }
    }

    /**
     * Открыть окно выбора при: "Пересортице".
     */
    private fun openDirectDialogWindow(): DirectDialogViewController {
        windowControllerDirectDialog = NewWindow(windowControllerDirectDialog).openWindow(
            DIRECT_DIALOG_VIEW, TITLE_ADD_SPEC, Modality.APPLICATION_MODAL
        )

        return windowControllerDirectDialog.controller as DirectDialogViewController
    }

    /**
     * Показать Карточку спецификации по номеру(типу) операции.
     *
     * @param numOper Номер операции.
     */
    private fun showCardSpecByNumOper(numOper: Int) {
        when (numOper) {
            ARRIVAL -> openCardSpecArrival()
            MOVEMENT -> openCardSpecMovement()
            DEBIT, SALE -> openCardSpecDebitAndSale()
            INVENTORY -> openCardSpecInventory()
            REGRADING, REPACKING -> openCardSpecRegradingAndRepacking()
        }
    }

    /**
     * Открыть карточку спецификации: "Приход".
     */
    private fun openCardSpecArrival() {
        fromtoLabel.isVisible = false
        fromtoTF.isVisible = false
        selectionFromtoBtn.isVisible = false
        templateDateView.isDisable = false
        loctoTF.isDisable = true

        selectionBtn.setOnAction { "".onClickSelectProdPackBtn() }
        cardSpec.locfromid = "0"
        selectionLoctoBtn.setOnAction { TITLE_LOC_TO.onClickSelectLocStoreBtn() }
    }

    /**
     * Открыть карточку спецификации: "Перемещение".
     */
    private fun openCardSpecMovement() {
        selectionLoctoBtn.setOnAction { TITLE_LOC_TO.onClickSelectLocStoreBtn() }
        selectionFromtoBtn.setOnAction { TITLE_FROM_TO.onClickSelectBalancesBtn(null) }
        selectionBtn.setOnAction { "".onClickSelectBalancesBtn(cardSpec.dtbal) }
    }

    /**
     * Открыть карточку спецификации: "Списание" и "Продажа".
     */
    private fun openCardSpecDebitAndSale() {
        loctoLabel.isVisible = false
        loctoTF.isVisible = false
        selectionLoctoBtn.isVisible = false

        selectionFromtoBtn.setOnAction { TITLE_FROM_TO.onClickSelectBalancesBtn(null) }
        selectionBtn.setOnAction { TITLE_FROM_TO.onClickSelectBalancesBtn(cardSpec.dtbal) }
    }

    /**
     * Открыть карточку спецификации: "Инвентаризация".
     */
    private fun openCardSpecInventory() {
        fromtoLabel.isVisible = false
        fromtoTF.isVisible = false
        selectionFromtoBtn.isVisible = false
        templateDateView.isDisable = false

        loctoLabel.text = "Где: "
        selectionBtn.setOnAction { "".onClickSelectProdPackBtn() }
        selectionLoctoBtn.setOnAction { TITLE_WHERE.onClickSelectLocStoreBtn() }
    }

    /**
     * Получить Операцию Пересортица и Перетарка.
     */
    private fun openCardSpecRegradingAndRepacking() {
        loctoLabel.text = "Где: "
        fromtoLabel.isVisible = false
        fromtoTF.isVisible = false
        selectionFromtoBtn.isVisible = false

        if (cardSpec.direct) {
            selectionBtn.setOnAction { TITLE_FOR_DEBIT.onClickSelectBalancesForDebit() }
            selectionLoctoBtn.setOnAction { TITLE_FOR_DEBIT.onClickSelectBalancesForDebit() }
        } else {
            templateDateView.isDisable = false
            selectionBtn.setOnAction { TITLE_FOR_ARRIVAL.onClickSelectProdPackBtn() }
            selectionLoctoBtn.setOnAction { TITLE_WHERE.onClickSelectLocStoreBtn() }
        }
    }

    /**
     * При нажатии кнопки выбрать вариант упаковки.
     */
    private fun String.onClickSelectProdPackBtn() {
        windowControllerProdPack = NewWindow(windowControllerProdPack).openWindow(
            PROD_PACK_VIEW, TITLE_PROD_PACK, Modality.APPLICATION_MODAL
        )
        val prodPackController = windowControllerProdPack.controller as ProdPackViewController
        prodPackController.sendSelectedItemProdPack { prodPack -> fillCardSpecWithSelectedItemFromTableProdPack(prodPack) }
    }

    /**
     * При нажатии кнопки выбрать участок хранения.
     */
    private fun String.onClickSelectLocStoreBtn() {
        windowControllerLocStore = NewWindow(windowControllerLocStore).openWindow(
            LOC_STORE_VIEW, TITLE_LOC_STORE, Modality.APPLICATION_MODAL
        )
        val locStoreController = windowControllerLocStore.controller as LocStoreViewController
        locStoreController.sendSelectedItemLocStore { selectItemLocStore ->
            loctoTF.text = "${selectItemLocStore.name} ${selectItemLocStore.comm}"
            cardSpec.loctoid = selectItemLocStore.id.toString()
        }
    }

    /**
     * При нажатии кнопки выбрать остатки.
     *
     * @param dateTimeBalance Остатки на дату.
     */
    private fun String.onClickSelectBalancesBtn(dateTimeBalance: String?) {
        windowControllerBalances = NewWindow(windowControllerBalances).openWindow(
            BALANCES_VIEW, TITLE_BALANCES, Modality.APPLICATION_MODAL
        )
        val balancesController = windowControllerBalances.controller as BalancesViewController
        balancesController.sendSelectedItemBalances(dateTimeBalance, ({ balance ->
            fillCardSpecWithSelectedItemFromTableBalance(balance)
            fromtoTF.text = balance.locname
            cardSpec.locfromid = balance.locid.toString()
        }))
    }

    /**
     * При наэатии кнопки выбрать остатки для списания.
     */
    private fun String.onClickSelectBalancesForDebit() {
        windowControllerBalances = NewWindow(windowControllerBalances).openWindow(
            BALANCES_VIEW, TITLE_BALANCES, Modality.APPLICATION_MODAL
        )
        val balancesController = windowControllerBalances.controller as BalancesViewController
        balancesController.sendSelectedItemBalances(cardSpec.dtbal, ({ balance ->
            fillCardSpecWithSelectedItemFromTableBalance(balance)
            loctoTF.text = "${balance.locname} ${balance.comm}"
            cardSpec.loctoid = balance.locid.toString()
        }))
    }

    /**
     * Заполнить карточку спецификации выбранным элементом из таблицы: "Варианты упаковки".
     *
     * @param prodPack Вариант упаковки.
     */
    private fun fillCardSpecWithSelectedItemFromTableProdPack(prodPack: ProdPackModel) {
        packnameLabel.text = prodPack.packname
        artLabel.text = prodPack.art
        rowsLabel.text = prodPack.rows.toString()
        couLabel.text = prodPack.cou.toString()
        colorLabel.text = prodPack.color
        cardSpec.packid = prodPack.id

        kolTF.textProperty().addListener { _, _, newValue ->
            if (newValue == "" || newValue == "-") piecesTF.text = "0" else piecesTF.text =
                "${newValue.toInt() * prodPack.rows * prodPack.cou}"
        }
        piecesTF.isDisable = true
    }

    /**
     * Заполнить карточку спецификации выбранным элементом из таблицы: "Остатки".
     *
     * @param balance Остатки.
     */
    private fun fillCardSpecWithSelectedItemFromTableBalance(balance: BalancesModel) {
        packnameLabel.text = balance.konsname
        artLabel.text = balance.art
        rowsLabel.text = balance.rows.toString()
        couLabel.text = balance.cou.toString()
        colorLabel.text = balance.color
        templateDateViewController.fillParseTemplateDate(balance.dtcreate)

        kolTF.promptText = "Макс. ${balance.balances}"
        piecesTF.text = "0"

        cardSpec.rows = balance.rows
        cardSpec.cou = balance.cou
        cardSpec.packid = balance.packid
    }

    /**
     * При нажатии на кнопку: "ОК".
     *
     * @param ev Событие.
     * @param callback Обратный вызов.
     */
    private fun onClickAddCardSpecBtn(ev: ActionEvent, callback: (Boolean) -> Unit) {
        try {
            cardSpec.name = packnameLabel.text
            cardSpec.art = artLabel.text
            cardSpec.rows = rowsLabel.text.toInt()
            cardSpec.cou = couLabel.text.toInt()
            cardSpec.color = colorLabel.text

            cardSpec.dtcreate = templateDateViewController.getTemplateDate()
            LocalDate.parse(cardSpec.dtcreate, currentDateFormat)

            cardSpec.kol = kolTF.text.toInt()
            if (cardSpec.direct && cardSpec.kol > 0) cardSpec.kol = -cardSpec.kol
            cardSpec.locfrom = fromtoTF.text
            cardSpec.locto = loctoTF.text

            cardSpecDaoImpl.addSpOperV2(cardSpec)
            callback(true)
            helperWithScene.closeStage(ev)
        } catch (eNFE: NumberFormatException) {
            logger.error(eNFE.message)
            exceptionHandler.printStackTraceElem(eNFE, "", "${eNFE.message}")
            eventAlert.alertWarning(WRONG_FORMAT_DATA)
        } catch (eDTP: DateTimeParseException) {
            logger.error(eDTP.message)
            exceptionHandler.printStackTraceElem(eDTP, "onClickAddCardSpecBtn", "${eDTP.message}")
            eventAlert.alertWarning(WRONG_FORMAT_DATA)
        }
    }

    /**
     * Заполнить шапку: "Карточки спецификации".
     *
     * @param specId ИД спецификации.
     */
    private fun fillTopCardSpec(specId: Int) {
        cardSpec = cardSpecDaoImpl.getSpOperBySpOperID(specId)

        packnameLabel.text = cardSpec.name
        artLabel.text = cardSpec.art
        rowsLabel.text = cardSpec.rows.toString()
        couLabel.text = cardSpec.cou.toString()
        colorLabel.text = cardSpec.color
        templateDateViewController.fillParseTemplateDate(cardSpec.dtcreate)
        kolTF.text = cardSpec.kol.toString()
        kolTF.promptText = "Макс. ${cardSpec.kol}"
        piecesTF.text = (cardSpec.kol * cardSpec.rows * cardSpec.cou).toString()
        fromtoTF.text = cardSpec.locfrom
        loctoTF.text = cardSpec.locto

        showCardSpecByNumOper(cardSpec.oper)
        isOnExistOrder()
    }

    /**
     * Проверка на существование ордера, если нет, то блокировать всё окно.
     */
    private fun isOnExistOrder() {
        cardSpecMainWindow.isDisable = cardSpec.existord == 1
    }

    /**
     * Очистить: "Карточку спецификации".
     */
    private fun clearCardSpec() {
        packnameLabel.text = ""
        artLabel.text = ""
        rowsLabel.text = ""
        couLabel.text = ""
        colorLabel.text = ""
        kolTF.text = ""
        fromtoTF.text = ""
        loctoTF.text = ""
    }
}

