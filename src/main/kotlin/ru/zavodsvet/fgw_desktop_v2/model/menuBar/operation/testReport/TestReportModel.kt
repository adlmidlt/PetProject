package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport

/**
 * Модель: "Протокол испытаний".
 */
data class TestReportModel(
    /**
     * ИД.
     */
    var id: Int,

    /**
     * Номер документа.
     */
    var num: String,

    /**
     * Дата документа.
     */
    var ddoc: String,

    /**
     * Дата испытаний.
     */
    var dtest: String,

    /**
     * ИД операции.
     */
    var operid: Int,
) {
    constructor() : this(0, "", "", "", 0)
}