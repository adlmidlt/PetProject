package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper.cardSpec

/**
 * Модель: "Карточка спецификации".
 */
data class CardSpecificationModel(
    /**
     * ИД операции.
     */
    var operid: Int,

    /**
     * ИД спецификации.
     */
    var spoperid: Int,

    /**
     * Номер операции.
     */
    val oper: Int,

    /**
     * ИД варианта упаковки.
     */
    var packid: Int,

    /**
     * Артикул.
     */
    var art: String,

    /**
     * Конструкторское наименование.
     */
    var name: String,

    /**
     * Цвет.
     */
    var color: String,

    /**
     * Рядность.
     */
    var rows: Int,

    /**
     * Кол-во в ряду.
     */
    var cou: Int,

    /**
     * ИД Откуда.
     */
    var locfromid: String,

    /**
     * ИД Куда.
     */
    var loctoid: String,

    /**
     * Откуда.
     */
    var locfrom: String,
    /**
     * Куда.
     */
    var locto: String,

    /**
     * Дата производства.
     */
    var dtcreate: String,

    /**
     * Дата окончания срока годности.
     */
    var dtexpiry: String,

    /**
     * Кол-во пакет поддонов. (п/п)
     */
    var kol: Int,

    /**
     * Существует ордер или нет.
     */
    var existord: Int,

    /**
     * Флаг для прихода или списания: если false = Приход, true = Списание.
     */
    var direct: Boolean,

    /**
     * Остатки на Дату время.
     */
    var dtbal: String?,

    ) {
    constructor() : this(0, 0, 0, 0, "", "", "", 0, 0, "", "", "", "", "", "", 0, 0, false, null)
}