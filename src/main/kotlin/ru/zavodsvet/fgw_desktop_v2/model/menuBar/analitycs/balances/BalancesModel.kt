package ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances

/**
 * Модель: "Остатки".
 */
data class BalancesModel(
    /**
     * ИД продукта.
     */
    var prodid: Int,
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
    var konsname: String,
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
     * Глубина Ширина Высота.
     */
    var dwh: String,
    /**
     * Дата создания.
     */
    var dtcreate: String,
    /**
     * Номер пакет поддона.
     */
    var numpart: Int,
    /**
     * Комментарий.
     */
    var comm: String,
    /**
     * ИД участка хранения.
     */
    var locid: Int,
    /**
     * Наименование участка хранения.
     */
    var locname: String,
    /**
     * Остатки палет поддонов.
     */
    var balances: Int,
    /**
     * Кол-во изделий.
     */
    var pieces: Int,
) {
    override fun toString(): String {
        return "Balance(prodid=$prodid, packid=$packid, art='$art', konsname='$konsname', color='$color', rows=$rows, cou=$cou, dwh='$dwh', dtcreate='$dtcreate', numpart=$numpart, comm='$comm', locid=$locid, locname='$locname', balances=$balances, pieces=$pieces)"
    }
}