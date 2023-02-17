package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.ProgressBarTreeTableCell
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseButton
import javafx.stage.Modality
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.CARD_LOC_STORE_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.QUANTITY_SUB_LOC_STORE_DIALOG_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_CARD_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.CardLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.QuantitySubLocStoreDialogViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.LocStoreDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore.CardLocStoreDaoImpl
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore.tabs.SchemaLocStoreDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.locStore.LocStoreModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Участки хранения".
 */
class LocStoreViewController : Initializable {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(LocStoreViewController::class.java)

    /**
     * Таблица: "Участок хранения".
     */
    @FXML
    private lateinit var locStoreTreeTable: TreeTableView<LocStoreModel>

    /**
     * Колонка: "Наименование участка хранения".
     */
    @FXML
    private lateinit var nameTreeColumn: TreeTableColumn<LocStoreModel?, String?>

    /**
     * Колонка: "Комментарии".
     */
    @FXML
    private lateinit var commTreeColumn: TreeTableColumn<LocStoreModel?, String?>

    /**
     * Прогресс в колонке: "Заполняемость склада".
     */
    @FXML
    private lateinit var locStoreProgressBarColumn: TreeTableColumn<LocStoreModel, Double>

    /**
     * Дочерний список участка хранения.
     */
    @FXML
    private lateinit var locStoreTreeItemRoot: TreeItem<LocStoreModel>

    /**
     * Контекстное меню раскрыть все.
     */
    @FXML
    private lateinit var revealAllLocStoreMenuItem: MenuItem

    /**
     * Является ли участок хранения архивом
     */
    @FXML
    private lateinit var isArchiveCheckMenuItem: CheckMenuItem

    /**
     * Контекстное меню "Карточка" участка хранения.
     */
    @FXML
    private lateinit var cardLocStoreMenuItem: MenuItem

    /**
     * Контекстное меню "Экспорт в эксель".
     */
    @FXML
    private lateinit var exportInExcelMenuItem: MenuItem

    /**
     * Контекстное меню удалить участок хранения.
     */
    @FXML
    private lateinit var deleteLocStoreMenuItem: MenuItem

    /**
     * Контекстное меню в рахив/из архива
     */
    @FXML
    private lateinit var inArchiveLocStoreMenuItem: MenuItem

    /**
     * Контекстное меню обновить список участков хранения.
     */
    @FXML
    private lateinit var updListLocStoreMenuItem: MenuItem

    /**
     * Добавить участок хранения на текущий уровень.
     */
    @FXML
    private lateinit var addOnCurrentLvlLocStoreMenuItem: MenuItem

    /**
     * Добавить участок хранения на предыдущий уровень.
     */
    @FXML
    private lateinit var addOnDownLvlLocStoreMenuItem: MenuItem

    /**
     * Контекстное меню на добавление нескольких под участков хранения.
     */
    @FXML
    private lateinit var addOnDownLvlALotLocStoreMenuItem: MenuItem

    /**
     * Список участков хранения.
     */
    private var locStoreTreeItemList: ObservableList<TreeItem<LocStoreModel>> = FXCollections.observableArrayList()

    /**
     * Реализация объекта доступа к данным справочника: "Участки хранения".
     */
    private var locStoreDaoImpl = LocStoreDaoImpl()

    /**
     * Реализация объекта доступа к данным: "Карточки участка хранения".
     */
    private var cardLocStoreDaoImpl = CardLocStoreDaoImpl()

    /**
     * Реализация доступа к объектам данных: "Схем участка хранения".
     */
    private var schemaLocStoreDaoImpl = SchemaLocStoreDaoImpl()

    /**
     * Является ли архивом по умолчанию нет.
     */
    private var isArchiveLocStore = 0

    /**
     * ИД выбранного элемента в таблице.
     */
    private var selectedItemId: Int = 0

    /**
     * Является архивом 1-ДА, 0-НЕТ.
     */
    private var isArchive = 0

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Оповещатель спрашивает (Да или Нет).
     */
    private var alertConfirm = Alert(Alert.AlertType.CONFIRMATION)

    private var windowControllerQuantitySubLocStore = WindowController()
    private var windowControllerCardLocStore = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        locStoreTreeTable.isShowRoot = false
        locStoreTreeTable.stylesheets.add("css/tree-table-view-loc-store.css")

        editCellInNameColumn()
        editCellInCommColumn()
        progressInCellInFillColumn()
        handlerEvents()
    }

    /**
     * Обработчик событий.
     */
    private fun handlerEvents() {
        locStoreTreeTable.setOnMouseClicked {
            if (locStoreTreeTable.selectionModel.selectedItem != null) {
                expandChildsByItem(locStoreTreeTable.selectionModel.selectedItem)
            }
        }
        locStoreTreeTable.setOnKeyPressed { ev -> onClickDelKey(ev) }
        contextMenuTableLocStore()
    }

    /**
     * Отправить выбранный элемент: "Участок хранения".
     *
     * @param callback Возвращает сущность.
     */
    fun sendSelectedItemLocStore(callback: (LocStoreModel) -> Unit) {
        fillTreeTableLocStore()

        onDoubleClickMouse(locStoreTreeTable, callback)
        onClickEnterKey(locStoreTreeTable, callback)
    }

    /**
     * Заполнить таблицу: "Участки хранения".
     */
    fun fillTreeTableLocStore() {
        saveBookMark()
        locStoreTreeItemRoot.children.removeAll(locStoreTreeItemRoot.children)
        locStoreTreeItemList.removeAll()

        locStoreTreeItemList = locStoreDaoImpl.getShowLocStore(0, isArchiveLocStore)
        locStoreTreeItemRoot.children.addAll(locStoreTreeItemList)
        loadBookMark()
    }

    /**
     * Контекстное меню таблицы: "Участок хранения".
     */
    private fun contextMenuTableLocStore() {
        updListLocStoreMenuItem.setOnAction { fillTreeTableLocStore() }
        inArchiveLocStoreMenuItem.setOnAction {
            isArchiveLocStore(getSelectedItemLocStore().value)
            reloadSelectedItem()
        }
        cardLocStoreMenuItem.setOnAction { showCardOperSelectedLocStore() }

        addOnCurrentLvlLocStoreMenuItem.setOnAction {
            showCardOperSelectedLocStoreByParent(getSelectedItemLocStore().value.parid)
            reloadSelectedItem()
        }

        addOnDownLvlLocStoreMenuItem.setOnAction {
            showCardOperSelectedLocStoreByParent(getSelectedItemLocStore().value.id)
            reloadSelectedItem()
        }

        addOnDownLvlALotLocStoreMenuItem.setOnAction {
            showQuantitySubLocStoreDialog(getSelectedItemLocStore().value.id)
            reloadSelectedItem()
        }

        deleteLocStoreMenuItem.setOnAction {
            showConfirmDeleteSpec(getSelectedItemLocStore().value)
        }

        isArchiveCheckMenuItem.setOnAction {
            isArchiveLocStore = if (isArchiveCheckMenuItem.isSelected) 1 else 0
            fillTreeTableLocStore()
        }

        revealAllLocStoreMenuItem.setOnAction {
            locStoreTreeItemRoot.children.forEach { item ->
                expandChildsByItem(item)
                item.children.forEach { item2 -> expandChildsByItem(item2) }
            }
        }
    }

    /**
     * Открыть Карточку операции выбранного участка хранения.
     */
    private fun showCardOperSelectedLocStore() {
        openCardLocStore().fillCardLocStoreByIdLocStore(getSelectedItemLocStore().value.id)
    }

    /**
     * Открыть Карточку операции выбранного участка хранения по родителю.
     *
     * @param parid ИД родителя участка хранения.
     */
    private fun showCardOperSelectedLocStoreByParent(parid: Int) {
        val locStoreId = locStoreDaoImpl.addLocStore(parid)
        openCardLocStore().fillCardLocStoreByIdLocStore(locStoreId)

        val locStore = TreeItem<LocStoreModel>()
        locStore.value = LocStoreModel()
        locStore.value.id = locStoreId
        val selectedItem = locStoreTreeTable.selectionModel.selectedItem
        if (selectedItem.value.id != parid) {
            selectedItem.parent.children.addAll(locStore)
        } else {
            selectedItem.children.addAll(locStore)
        }
        locStoreTreeTable.selectionModel.select(locStore)
    }

    /**
     * Открыть окно карточки участка хранения.
     */
    private fun openCardLocStore(): CardLocStoreViewController {
        windowControllerCardLocStore =
            NewWindow(windowControllerCardLocStore).openWindow(CARD_LOC_STORE_VIEW, TITLE_CARD_LOC_STORE, Modality.NONE)

        return windowControllerCardLocStore.controller as CardLocStoreViewController
    }

    /**
     * Показать кол-во под участков хранения.
     *
     * @param parId ИД родителя участка хранения.
     */
    private fun showQuantitySubLocStoreDialog(parId: Int) {
        windowControllerQuantitySubLocStore =
            NewWindow(windowControllerQuantitySubLocStore).openWindow(QUANTITY_SUB_LOC_STORE_DIALOG_VIEW, "", Modality.NONE)
        val quantitySubLocStoreController =
            windowControllerQuantitySubLocStore.controller as QuantitySubLocStoreDialogViewController
        quantitySubLocStoreController.confirm { confirm ->
            if (confirm) {
                val quantitySunLocStore = quantitySubLocStoreController.getQuantitySubLocStore()
                schemaLocStoreDaoImpl.addMulLocStore(parId, quantitySunLocStore)
            }
        }
    }

    /**
     * Перезагрузить выбранный элемент после какого либо действия.
     */
    private fun reloadSelectedItem() {
        val locStore = cardLocStoreDaoImpl.getLocStorePallets(locStoreTreeTable.selectionModel.selectedItem.value.id)
        locStoreTreeTable.selectionModel.selectedItem.value.name = locStore.value.name
        locStoreTreeTable.selectionModel.selectedItem.value.comm = locStore.value.comm
        locStoreTreeTable.selectionModel.selectedItem.value.archive = locStore.value.archive
        locStoreTreeTable.refresh()
    }

    /**
     * Удаление "Участка хранения" с помощью клавиши "Delete".
     *
     * @param keyEvent Событие на кнопку "Удалить".
     */
    private fun onClickDelKey(keyEvent: KeyEvent) {
        if (keyEvent.code == KeyCode.DELETE && getSelectedItemLocStore() != null) {
            showConfirmDeleteSpec(getSelectedItemLocStore().value)
        }
    }

    /**
     * Показать диалоговое окно с подтверждением удалением участка хранения.
     *
     * @param locStore "Участок хранения".
     */
    private fun showConfirmDeleteSpec(locStore: LocStoreModel) {
        alertConfirm.title = "Подтверждение операции \"Удалить участок хранения\""
        alertConfirm.headerText =
            "Вы уверены что хотите удалить Участок хранения: ${locStore.id}\nНазвание ${locStore.name} ?"

        val option = alertConfirm.showAndWait()
        if (option.get() == ButtonType.OK) {
            locStoreDaoImpl.delLocStore(locStore.id)
            locStoreTreeTable.selectionModel.selectedItem.parent.children.removeAll(locStoreTreeTable.selectionModel.selectedItem)

            logger.info("Участок хранения ${locStore.id} Название${locStore.name} удален.")
        } else if (option.get() == ButtonType.CANCEL) {
            alertConfirm.close()
        }
    }

    /**
     * Является ли архивным участок хранения.
     *
     * @param locStore Участок хранения.
     */
    private fun isArchiveLocStore(locStore: LocStoreModel) {
        if (locStore.archive == 0) {
            cardLocStoreDaoImpl.editLocStore(locStore.id, "archive", 1.toString())
        } else {
            cardLocStoreDaoImpl.editLocStore(locStore.id, "archive", 0.toString())
        }
    }

    /**
     * Редактирование ячейки в колонке Название участка хранения.
     */
    private fun editCellInNameColumn() {
        nameTreeColumn.setCellFactory {
            object : TreeTableCell<LocStoreModel?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item != null && !empty) {
                        text = item
                        isArchiveByName(item, locStoreTreeItemRoot.children)
                        style = if (isArchive == 1) "-fx-text-fill: silver;" else "-fx-text-fill: black;"
                    }
                }
            }
        }
    }

    /**
     * Проверяем на архив по названию участка хранения.
     *
     * @param name Название участка хранения.
     * @param locStoreList Список Участков хранения.
     */
    @Suppress("BooleanMethodIsAlwaysInverted")
    private fun isArchiveByName(name: String, locStoreList: ObservableList<TreeItem<LocStoreModel>>): Boolean {
        for (locStore in locStoreList) {
            if (name == locStore.value.name) {
                isArchive = locStore.value.archive
                return true
            }
            if (isArchiveByName(name, locStore.children)) {
                return true
            }
        }
        return false
    }

    /**
     * Редактирование ячейки в колонке Комментарии участка хранения.
     */
    private fun editCellInCommColumn() {
        commTreeColumn.setCellFactory {
            object : TreeTableCell<LocStoreModel?, String?>() {
                override fun updateItem(item: String?, empty: Boolean) {
                    super.updateItem(item, empty)
                    text = item
                    for (locStoreItem in locStoreTreeItemRoot.children) {
                        val locStore = locStoreItem.value
                        if (item == locStore.comm) {
                            style = if (locStore.archive == 1) "-fx-text-fill: silver;" else "-fx-text-fill: black;"

                        }
                    }
                }
            }
        }
    }

    /**
     * Прогресс в ячейки колонки Заполняемость в участках хранения.
     */
    private fun progressInCellInFillColumn() {
        locStoreProgressBarColumn.setCellFactory {
            object : ProgressBarTreeTableCell<LocStoreModel>() {
                override fun updateItem(item: Double?, empty: Boolean) {
                    super.updateItem(item, empty)
                    if (item == null || empty) {
                        text = null
                        graphic = null
                    } else {
                        text = (item * 100).toString() + "%"
                    }
                }
            }
        }
    }

    /**
     * При нажатии клавиши ввода.
     *
     * @param callback Возвращает сущность.
     * @param treeTableView Таблица сущности.
     */
    private fun onClickEnterKey(treeTableView: TreeTableView<LocStoreModel>, callback: (LocStoreModel) -> Unit) {
        treeTableView.setOnKeyPressed { ev ->
            expandChildsByItem(getSelectedItemLocStore())
            if (ev.code == KeyCode.ENTER) {
                callback.invoke(getSelectedItemLocStore().value)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * При двойном щелчке мыши.
     *
     * @param callback Возвращает сущность.
     * @param treeTableView Таблица сущности.
     */
    private fun onDoubleClickMouse(treeTableView: TreeTableView<LocStoreModel>, callback: (LocStoreModel) -> Unit) {
        treeTableView.setOnMouseClicked { ev ->
            expandChildsByItem(getSelectedItemLocStore())
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                callback.invoke(getSelectedItemLocStore().value)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Развернуть дочерние элементы по элементам.
     *
     * @param selectedItemLocStore Выбранный элемент участка хранения.
     */
    private fun expandChildsByItem(selectedItemLocStore: TreeItem<LocStoreModel>) {
        selectedItemLocStore.isExpanded = !selectedItemLocStore.isExpanded
        if (selectedItemLocStore.children.isEmpty()) {
            locStoreTreeItemList = locStoreDaoImpl.getShowLocStore(selectedItemLocStore.value.id, isArchiveLocStore)
            selectedItemLocStore.children.addAll(locStoreTreeItemList)
        }
    }

    /**
     * Получить выбранный элемент: "Участка хранения".
     */
    private fun getSelectedItemLocStore() = locStoreTreeTable.selectionModel.selectedItem

    /**
     * Запоминает текущую позицию строки в таблице.
     */
    private fun saveBookMark() {
        if (locStoreTreeTable.selectionModel.selectedItem != null) {
            selectedItemId = locStoreTreeTable.selectionModel.selectedItem.value.id
        }
    }

    /**
     * Загружает текущую позицию строки в таблице.
     */
    private fun loadBookMark() {
        locStoreTreeItemRoot.children.filter { it.value.id == selectedItemId }
            .forEach { locStoreTreeTable.selectionModel.select(it) }
    }
}