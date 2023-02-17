package ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.stage.Modality
import ru.zavodsvet.fgw_desktop_v2.common.EMPLOYEE_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_EMPLOYEE
import ru.zavodsvet.fgw_desktop_v2.common.window.NewWindow
import ru.zavodsvet.fgw_desktop_v2.common.window.WindowController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.employee.EmployeeViewController
import java.net.URL
import java.util.*

/**
 * Контроллер окна: "Выбор сотрудника".
 */
class SelectionEmployeeViewController : Initializable {
    /**
     * Поле: "Сотрудник".
     */
    @FXML
    private lateinit var employeeTF: TextField

    /**
     * Кнопка: "Выбрать сотрудника".
     */
    @FXML
    private lateinit var selectEmployeeBtn: Button

    /**
     * Кнопка: "Стереть поле сотрудник".
     */
    @FXML
    private lateinit var clearEmployeeTFBtn: Button

    /**
     * ИД сотрудника.
     */
    private var employeeId: Int? = null

    private var windowControllerEmployee = WindowController()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        employeeTF.isDisable = true
    }

    /**
     * Открыть окно: "Выбрать сотрудника".
     */
    fun openWindowSelectVidOper() {
        selectEmployeeBtn.setOnAction { onClickSelectEmployeeBtn() }
        clearEmployeeTFBtn.setOnAction { onClickClearEmployeeTFBtn() }
    }

    /**
     * Получить ИД сотрудника.
     */
    fun getEmployeeId() = employeeId

    /**
     * При нажатии кнопки выбрать вид операции.
     */
    private fun onClickSelectEmployeeBtn() {
        windowControllerEmployee = NewWindow(windowControllerEmployee).openWindow(
            EMPLOYEE_VIEW, TITLE_EMPLOYEE, Modality.APPLICATION_MODAL
        )
        val employeeController = windowControllerEmployee.controller as EmployeeViewController
        employeeController.sendSelectedItemVidOper { selectItemEmployee ->
            employeeTF.text = selectItemEmployee.fio
            employeeId = selectItemEmployee.id
        }
    }

    /**
     * При нажатии кнопки очистить поле сотрудник.
     */
    private fun onClickClearEmployeeTFBtn() {
        employeeTF.text = ""
        employeeId = null
    }
}