package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation.cardOper

/**
 * Модель: "Карточка операции".
 */
data class CardOperationModel(
    /**
     * ИД спецификации.
     */
    val spoperid: Int,
    /**
     * Артикул.
     */
    val art: String,
    /**
     * Конструкторское наименование.
     */
    val kdname: String,
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
     * Откуда.
     */
    var locfrom: String,
    /**
     * Куда.
     */
    var locto: String,

    /**
     * Масса пакет поддонов. (п/п)
     */
    val massa: Int,
    /**
     * Кол-во пакет поддонов. (п/п)
     */
    var kol: Int,
    /**
     * Дата производства.
     */
    var dtcreate: String,

    /**
     * Дата окончания срока годности.
     */
    var dtexpiry: String,

    /**
     * ИД варианта упаковки.
     */
    var packid: Int,
    /**
     * ИД участка хранения КУДА.
     */
    var loctoid: Int,
    /**
     * Кол-во изделий.
     */
    var pieces: Int,
) {
    override fun toString(): String {
        return "CardOperationModel(spoperid=$spoperid, art='$art', kdname='$kdname', color='$color', rows=$rows, cou=$cou, dwh='$dwh', locfrom='$locfrom', locto='$locto', kol=$kol, dtcreate='$dtcreate', dtexpiry='$dtexpiry', packid=$packid, loctoid=$loctoid, pieces=$pieces)"
    }
}