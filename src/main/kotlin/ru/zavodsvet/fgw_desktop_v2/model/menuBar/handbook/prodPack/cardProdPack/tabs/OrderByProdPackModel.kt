package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.cardProdPack.tabs

/**
 * Ордер по варианту упаковки.
 */
data class OrderByProdPackModel(
    /**
     * ИД операции.
     */
    var operid: Int,

    /**
     * Вид операции.
     */
    var vidoper: String,

    /**
     * Дата операции.
     */
    var oprcreate: String,

    /**
     * Дата ордера.
     */
    var dtord: String,

    /**
     * Участок хранения.
     */
    var locname: String,

    /**
     * Дата производства.
     */
    var dtcreate: String,

    /**
     * Кол-во п/п.
     */
    var kol: Int,

    /**
     * Кол-во изделий.
     */
    var pieces: Int,

    /**
     *  Остаток п/п.
     */
    var balances: Int,

    /**
     * Остаток изделий.
     */
    var pbalances: Int,
) {
    override fun toString(): String {
        return "OrderByProdPack(operid=$operid, vidoper='$vidoper', oprcreate='$oprcreate', dtord='$dtord', locname='$locname', dtcreate='$dtcreate', kol=$kol, pieces=$pieces, balances=$balances, pbalances=$pbalances)"
    }
}
