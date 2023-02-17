package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.cardTestReport.cardSpTestReport

/**
 * Модель: "Спецификация протокола испытаний".
 */
data class SpecificationTestReportModel(
    /**
     * ИД.
     */
    var id: Int,

    /**
     * ИД конструкторского наименования.
     */
    var prodid: Int,

    /**
     * Конструкторское наименование.
     */
    var name: String,

    /**
     * Кол-во месяцев (срок годности продукции).
     */
    var extend: Int,

    /**
     * С даты производства.
     */
    var dtn: String,

    /**
     * По дату производства.
     */
    var dtk: String,
) {
    constructor() : this(0, 0, "", 0, "", "")
}

