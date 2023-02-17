package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.kdName

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import javafx.scene.layout.HBox
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.kdName.KDNameDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.kdName.KDNameModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Конструкторских наименований".
 */
class KDNameViewController : Initializable {
    /**
     * Фильтр для поиска по Конструкторскому наименованию.
     */
    @FXML
    private lateinit var filterColumnsKDNameHBox: HBox

    /**
     * Поле для поиска по: "Конструкторским наименованиям"
     */
    @FXML
    private lateinit var kdNameSearchTF: TextField

    /**
     * Таблица: "Конструкторские наименования".
     */
    @FXML
    private lateinit var kdNameTable: TableView<KDNameModel>

    /**
     * Список конструкторских наименований.
     */
    private var kdNameList: ObservableList<KDNameModel> = FXCollections.observableArrayList()

    /**
     * Фильтр по таблице "Конструкторские наименования".
     */
    private lateinit var filterByKDName: FilteredList<KDNameModel>

    /**
     * Реализация объекта доступа к данным справочника: "Конструкторские наименования".
     */
    private var kdNameDaoImpl = KDNameDaoImpl()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        filterColumnsKDNameHBox.stylesheets.add("css/search.css")
    }

    /**
     * Заполнить таблицу: "Конструкторские наименования".
     */
    fun fillTableKDName() {
        resetFilter()
        kdNameTable.items.removeAll()

        kdNameList = kdNameDaoImpl.getAllKDNames()

        filterByKDName = FilteredList(kdNameList)
        kdNameList = filterByColumn(kdNameSearchTF, filterByKDName)

        kdNameTable.items = kdNameList
        kdNameTable.selectionModel.selectFirst()
    }

    /**
     * Отправить выбранный элемент: "Вид операции".
     *
     * @param callback Возвращает сущность.
     */
    fun sendSelectedItemKDName(callback: (KDNameModel) -> Unit) {
        fillTableKDName()

        onDoubleClickMouse(kdNameTable, callback)
        onClickEnterKey(kdNameTable, callback)
    }

    /**
     * При нажатии клавиши ввода.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onClickEnterKey(tableView: TableView<KDNameModel>, callback: (KDNameModel) -> Unit) {
        tableView.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER) {
                setSelectedItemKDName(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * При двойном щелчке мыши.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onDoubleClickMouse(tableView: TableView<KDNameModel>, callback: (KDNameModel) -> Unit) {
        tableView.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                setSelectedItemKDName(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Установить выбранный элемент: "Конструкторские наименования".
     *
     * @param callback Возвращает сущность.
     */
    private fun setSelectedItemKDName(callback: (KDNameModel) -> Unit) {
        if (getSelectedItemKDName() != null) {
            kdNameList.filter { it.id == getSelectedItemKDName().id }.forEach { callback.invoke(it) }
        }
    }

    /**
     * Получить выбранный элемент: "Конструкторской документации".
     */
    private fun getSelectedItemKDName() = kdNameTable.selectionModel.selectedItem

    /**
     * Фильтр по колонке.
     *
     * @param searchKeyword Поле для поиска.
     * @param filter Список для фильтра.
     */
    private fun filterByColumn(searchKeyword: TextField, filter: FilteredList<KDNameModel>): ObservableList<KDNameModel> {
        searchKeyword.textProperty().addListener { _, _, _ ->
            filter.setPredicate { kdName ->
                return@setPredicate (kdNameSearchTF.text.isEmpty() || kdName.name.lowercase()
                    .indexOf(kdNameSearchTF.text.lowercase()) > -1)
            }
            if (!kdNameTable.isFocused) {
                kdNameTable.selectionModel.selectFirst()
            }
        }

        return filter
    }
    /**
     * Сбросить фильтр.
     */
    private fun resetFilter() {
        kdNameSearchTF.text = ""
    }
}