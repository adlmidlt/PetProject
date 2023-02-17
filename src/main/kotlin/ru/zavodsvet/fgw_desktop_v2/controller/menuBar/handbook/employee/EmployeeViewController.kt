package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.employee

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
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.employee.EmployeeDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.employee.EmployeeModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Сотрудники".
 */
class EmployeeViewController : Initializable {
    /**
     * Фильтр для поиска по Сотрудникам.
     */
    @FXML
    private lateinit var filterColumnsEmployeeHBox: HBox

    /**
     * Поле для поиска по табельному номеру.
     */
    @FXML
    private lateinit var tabNumSearchTF: TextField

    /**
     * Поле для поиска по ФИО.
     */
    @FXML
    private lateinit var fioSearchTF: TextField

    /**
     * Таблица: "Сотрудники".
     */
    @FXML
    private lateinit var employeeTable: TableView<EmployeeModel>

    /**
     * Список сотрудников.
     */
    private var employeeList: ObservableList<EmployeeModel> = FXCollections.observableArrayList()

    /**
     * Реализация объекта доступа к данным справочника: "Сотрудники".

     */
    private var employeeDaoImpl = EmployeeDaoImpl()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Фильтр по таблице "Конструкторские наименования".
     */
    private lateinit var filterByEmployee: FilteredList<EmployeeModel>


    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        filterColumnsEmployeeHBox.stylesheets.add("css/search.css")
    }

    /**
     * Заполнить таблицу: "Сотрудники".
     * */
    private fun fillTableEmployee() {
        resetFilter()
        employeeTable.items.removeAll()

        employeeList = employeeDaoImpl.getAllEmployee()

        filterByEmployee = FilteredList(employeeList)
        employeeList = filterByColumn(tabNumSearchTF, filterByEmployee)
        employeeList = filterByColumn(fioSearchTF, filterByEmployee)

        employeeTable.items = employeeList
        employeeTable.selectionModel.selectFirst()
    }

    /**
     * Отправить выбранный элемент: "Сотрудники".
     *
     * @param callback Возвращает сущность.
     */
    fun sendSelectedItemVidOper(callback: (EmployeeModel) -> Unit) {
        fillTableEmployee()

        onDoubleClickMouse(employeeTable, callback)
        onClickEnterKey(employeeTable, callback)
    }

    /**
     * При нажатии клавиши ввода.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onClickEnterKey(tableView: TableView<EmployeeModel>, callback: (EmployeeModel) -> Unit) {
        tableView.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER) {
                setSelectedItemEmployee(callback)
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
    private fun onDoubleClickMouse(tableView: TableView<EmployeeModel>, callback: (EmployeeModel) -> Unit) {
        tableView.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                setSelectedItemEmployee(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Установить выбранный элемент: "Сотрудники".
     *
     * @param callback Возвращает сущность.
     */
    private fun setSelectedItemEmployee(callback: (EmployeeModel) -> Unit) {
        if (getSelectedItemEmployee() != null) {
            employeeList.filter { it.id == getSelectedItemEmployee().id }.forEach { callback.invoke(it) }
        }
    }

    /**
     * Получить выбранный элемент: "Сотрудник".
     */
    private fun getSelectedItemEmployee() = employeeTable.selectionModel.selectedItem

    /**
     * Фильтр по колонке.
     *
     * @param searchKeyword Поле для поиска.
     * @param filter Список для фильтра.
     */
    private fun filterByColumn(
        searchKeyword: TextField, filter: FilteredList<EmployeeModel>
    ): ObservableList<EmployeeModel> {
        searchKeyword.textProperty().addListener { _, _, _ ->
            filter.setPredicate { employee ->
                return@setPredicate (tabNumSearchTF.text.isEmpty() || employee.id.toString().lowercase()
                    .indexOf(tabNumSearchTF.text.lowercase()) > -1) && (fioSearchTF.text.isEmpty() || employee.fio.lowercase()
                    .indexOf(fioSearchTF.text.lowercase()) > -1)
            }
            if (!employeeTable.isFocused) {
                employeeTable.selectionModel.selectFirst()
            }
        }

        return filter
    }

    /**
     * Сбросить фильтр.
     */
    private fun resetFilter() {
        tabNumSearchTF.text = ""
        fioSearchTF.text = ""
    }
}