package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.employee

/**
 * Модель: "Сотрудники".
 */
data class EmployeeModel(
    /**
     * ТН.
     */
    var id: Int,

    /**
     * ФИО.
     */
    var fio: String,
) {
    override fun toString(): String {
        return "Employee(id=$id, fio='$fio')"
    }
}