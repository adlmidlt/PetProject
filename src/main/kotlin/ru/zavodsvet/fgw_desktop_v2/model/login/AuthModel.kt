package ru.zavodsvet.fgw_desktop_v2.model.login

/**
 * Авторизация сотрудника ТЛК(товарно-логистический комплекс).
 */
data class AuthModel(
    /**
     * ТН.
     */
    var tabnum: Int,

    /**
     * ФИО сотрудника.
     */
    var fio: String,

    /**
     * ТН сотрудника.
     */
    var perif: Int,
) {
    constructor() : this(0, "", 0)

    override fun toString() = "AuthModel(tabnum=$tabnum, fio='$fio', perif=$perif)"
}

