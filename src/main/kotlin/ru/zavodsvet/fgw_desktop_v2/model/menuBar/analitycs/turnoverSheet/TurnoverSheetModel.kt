package ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.turnoverSheet

/**
 * Модель: "Оборотная ведомость".
 */
data class TurnoverSheetModel(
    /**
     * ИД продукции. (Конструкторское наименование).
     */
    var prodid: Int,

    /**
     * ИД варианта упаковки.
     */
    var packid: Int,

    /**
     * Конструкторское наименование.
     */
    var konsname: String,

    /**
     * Артикул.
     */
    var art: String,

    /**
     * Дата создания Варианта упаковки.
     */
    var dtcreate: String,

    /**
     * Номер пакет поддона.
     */
    var numpart: Int,

    /**
     * Комментарии.
     */
    var comm: String,

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
     * Длина Ширина Глубина.
     */
    var dwh: String,

    /**
     * ИД Участка хранения КУДА.
     */
    var locid: Int,

    /**
     * Название участка хранения. КУДА
     */
    var locname: String,

    /**
     * Входной остаток паллет.
     */
    var balin: Int,

    /**
     * Приход паллета.
     */
    var balprih: Int,

    /**
     * Расход паллета.
     */
    var balrash: Int,

    /**
     * Исходные остатки паллет.
     */
    var balout: Int,

    /**
     * Входной остаток изделий.
     */
    var balinp: Int,

    /**
     * Приход изделий.
     */
    var balprihp: Int,

    /**
     * Расход изделий.
     */
    var balrashp: Int,

    /**
     * Исходящие остатки изделий.
     */
    var baloutp: Int,
) {
    override fun toString(): String {
        return "[TurnoverSheet(prodid=$prodid, packid=$packid, konsname='$konsname', art='$art', dtcreate='$dtcreate', numpart=$numpart, comm='$comm', color='$color', rows=$rows, cou=$cou, dwh='$dwh', locid=$locid, locname='$locname', balin=$balin, balprih=$balprih, balrash=$balrash, balout=$balout, balinp=$balinp, balprihp=$balprihp, balrashp=$balrashp, baloutp=$baloutp)]"
    }
}