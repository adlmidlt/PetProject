package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.analitycs.turnoverSheet

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ProgressBar
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionDateTimeOfPeriodViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionKDNameViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.turnoverSheet.TurnoverSheetDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.turnoverSheet.TurnoverSheetModel
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Оборотная ведомость".
 */
class TurnoverSheetViewController : Initializable {
    /**
     * Главное окно "Оборотная ведомость".
     */
    @FXML
    private lateinit var turnoverSheetMainWindow: AnchorPane

    /**
     * Окно сцены: "Шаблон даты".
     */
    @FXML
    private lateinit var selectionDateTimeOfPeriodView: AnchorPane

    /**
     * Контроллер окна: "Шаблон даты".
     */
    @FXML
    private lateinit var selectionDateTimeOfPeriodViewController: SelectionDateTimeOfPeriodViewController

    /**
     * Сцена окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameView: AnchorPane

    /**
     * Контроллер окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameViewController: SelectionKDNameViewController

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
     * Кнопка применить фильтр для "Оборотная ведомость".
     */
    @FXML
    private lateinit var applyFilterBtn: Button

    /**
     * Кнопка для раскрытия всей "Оборотной ведомости".
     */
    @FXML
    private lateinit var revealAllBtn: Button

    /**
     * Прогресс бар.
     */
    @FXML
    private lateinit var revealAllBtnProgressBar: ProgressBar

    /**
     * Таблица "Оборотная ведомость" в виде дерева.
     */
    @FXML
    private lateinit var turnoverSheetTreeTable: TreeTableView<TurnoverSheetModel>

    /**
     * Корень пункта дерева "Оборотная ведомость".
     */
    @FXML
    private lateinit var turnoverSheetTreeItemRoot: TreeItem<TurnoverSheetModel>

    /**
     * Список пунктов дерева "Оборотная ведомость".
     */
    private var turnoverSheetList: ObservableList<TreeItem<TurnoverSheetModel>> = FXCollections.observableArrayList()

    /**
     * Текущий индекс элемента.
     */
    private var currentIndexItem = 0

    /**
     * Реализация объекта доступа к данным аналитики: "Оборотная ведомость".
     */
    private var turnoverSheetDaoImpl = TurnoverSheetDaoImpl()

    /**
     * Создать поток.
     */
    private lateinit var thread: Thread

    /**
     * Текущая сцена "Оборотной ведомости."
     */
    private var currentStage = Stage()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        turnoverSheetTreeTable.isShowRoot = false
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        selectionKDNameViewController.openWindowSelectKDName()
        selectionLocStoreViewController.openWindowSelectLocStore()
        applyFilterBtn.setOnAction { onClickApplyFilter() }
        revealAllBtn.setOnAction { ev -> onClickRevealAll(ev) }

        turnoverSheetTreeTable.setOnMouseClicked { onSingleClickMouse() }
        turnoverSheetTreeTable.setOnKeyPressed { ev -> onClickEnterKey(ev) }
    }

    /**
     * Заполнить таблицу: "Оборотная ведомость".
     *
     * @param startDateTime Дата время начала.
     * @param endDateTime Дата время окончания.
     */
    fun fillTableTurnoverSheet(startDateTime: String?, endDateTime: String?) {
        turnoverSheetList.removeAll()

        selectionDateTimeOfPeriodViewController.setStartDateTime(startDateTime!!)
        selectionDateTimeOfPeriodViewController.setEndDateTime(endDateTime!!)

        turnoverSheetList = turnoverSheetDaoImpl.findAllOborotPallets(
            startDateTime,
            endDateTime,
            selectionLocStoreViewController.getLocStoreId(),
            selectionKDNameViewController.getKDNameId(),
            null,
        )
        turnoverSheetTreeItemRoot.children.addAll(turnoverSheetList)
    }

    /**
     * При одиночном щелчке мыши.
     */
    private fun onSingleClickMouse() {
        if (getSelectItemTurnoverSheet() != null) {
            setSelectItemTurnoverSheet(getSelectItemTurnoverSheet())
        }
    }

    /**
     * При нажатии клавиши ввода.
     *
     * @param ev Событие.
     */
    private fun onClickEnterKey(ev: KeyEvent) {
        if (ev.code == KeyCode.ENTER && getSelectItemTurnoverSheet() != null) {
            setSelectItemTurnoverSheet(getSelectItemTurnoverSheet())
        }
    }

    /**
     * При нажатии на кнопку применить.
     */
    private fun onClickApplyFilter() {
        turnoverSheetTreeItemRoot.children.removeAll(turnoverSheetTreeItemRoot.children)
        fillTableTurnoverSheet(
            selectionDateTimeOfPeriodViewController.getStartDateTime(),
            selectionDateTimeOfPeriodViewController.getEndDateTime()
        )
    }

    /**
     * При нажатии на кнопку раскрыть все.
     */
    private fun onClickRevealAll(event: ActionEvent) {
        // Получаем текущую сцену при нажатии на кнопку.
        currentStage = (event.source as Node).scene.window as Stage
        // Создаем тело потока.
        thread = Thread {
            // При выполнении потока блокируем текущее окно.
            turnoverSheetMainWindow.isDisable = true
            // Пробегаемся по корню пунктов таблицы и получаем индексы.
            for ((ind, item) in turnoverSheetTreeItemRoot.children.withIndex()) {
                // Если поток прерван, то остановить выполнение потока.
                if (thread.isInterrupted) break
                // Текущий индекс элемента присваиваем индекс полученный из корня пункта таблицы.
                currentIndexItem = ind
                // Заполнения прогресс бара при выполнении операции.
                revealAllBtnProgressBar.progress =
                    currentIndexItem.toDouble() / (turnoverSheetTreeItemRoot.children.count() - 1)
                // Добавляем метод, который имитирует нажатием мышки на элемент.
                setSelectItemTurnoverSheet(item)
                // Пробегаемся по под корню пунктов таблицы.
                item.children.forEach { item2 -> setSelectItemTurnoverSheet(item2) }
            }
            // После выполнения потока возвращаем все по умолчанию.
            revealAllBtnProgressBar.progress = 0.0
            turnoverSheetMainWindow.isDisable = false
        }
        // Запускает поток.
        thread.start()
        // Останавливаем поток при нажатии на крестик в окне.
        currentStage.setOnCloseRequest {
            // Прервать поток.
            thread.interrupt()
            currentStage.close()
        }
        // Останавливаем поток при нажатии на Escape.
        currentStage.addEventHandler(KeyEvent.KEY_RELEASED) { event ->
            if (KeyCode.ESCAPE == event.code) {
                // Прервать поток.
                thread.interrupt()
                currentStage.close()
            }
        }
    }

    /**
     * Установить выбранный элемент: "Оборотная ведомость".
     *
     * @param turnoverSheetSelectedItem Оборотная ведомость выбранного элемента.
     */
    private fun setSelectItemTurnoverSheet(turnoverSheetSelectedItem: TreeItem<TurnoverSheetModel>) {
        turnoverSheetSelectedItem.isExpanded = !turnoverSheetSelectedItem.isExpanded
        if (turnoverSheetSelectedItem.children.isNullOrEmpty()) {
            if (turnoverSheetSelectedItem.value.packid == 0) {
                turnoverSheetList = turnoverSheetDaoImpl.findAllOborotPallets(
                    selectionDateTimeOfPeriodViewController.getStartDateTime(),
                    selectionDateTimeOfPeriodViewController.getEndDateTime(),
                    selectionLocStoreViewController.getLocStoreId(),
                    turnoverSheetSelectedItem.value.prodid,
                    null
                )
                turnoverSheetSelectedItem.children.addAll(turnoverSheetList)
                turnoverSheetSelectedItem.isExpanded = true

            }
            if (turnoverSheetSelectedItem.value.packid != 0 && turnoverSheetSelectedItem.value.dtcreate.isEmpty()) {
                turnoverSheetList = turnoverSheetDaoImpl.findAllOborotPallets(
                    selectionDateTimeOfPeriodViewController.getStartDateTime(),
                    selectionDateTimeOfPeriodViewController.getEndDateTime(),
                    selectionLocStoreViewController.getLocStoreId(),
                    turnoverSheetSelectedItem.value.prodid,
                    turnoverSheetSelectedItem.value.packid
                )
                turnoverSheetSelectedItem.children.addAll(turnoverSheetList)
                turnoverSheetSelectedItem.isExpanded = true
            }
        }
    }

    /**
     * Получить выбранный элемент: "Оборотная ведомость".
     */
    private fun getSelectItemTurnoverSheet() = turnoverSheetTreeTable.selectionModel.selectedItem
}