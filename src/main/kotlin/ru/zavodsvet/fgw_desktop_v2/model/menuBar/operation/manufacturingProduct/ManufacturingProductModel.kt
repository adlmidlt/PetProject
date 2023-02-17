package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.manufacturingProduct

/**
 * Модель: "Производство продукции"ю
 */
data class ManufacturingProductModel(
    /**
     * Артикул.
     */
    var art: String,

    /**
     * Наименование.
     */
    var prod: String,

    /**
     * Бар-код.
     */
    var barcode: String,

    /**
     * Дата совершенного действия.
     */
    var dtact: String,

    /**
     * Дата создания.
     */
    var dtcreate: String,

    /**
     * Действие.
     */
    var action: String,

    /**
     * ФИО.
     */
    var fio: String,

    /**
     * Линия упаковки.
     */
    var locpack: String,

    /**
     * Участок хранения.
     */
    var locstore: String,

    /**
     * ИД варианта упаковки.
     */
    var packId: Int,

    /**
     * ИД участка хранения.
     */
    var locstoreid: Int,

    /**
     * ИД операции
     */
    var operid: Int,

    /**
     * ИД этикетки продукта.
     */
    var ticketid: Int,
) {
    override fun toString(): String {
        return "PackingProd(art='$art', prod='$prod', barcode='$barcode', dtact='$dtact', dtcreate='$dtcreate', action='$action', fio='$fio', locpack='$locpack', locstore='$locstore', packId=$packId, locstoreid=$locstoreid, operid=$operid, ticketid=$ticketid)"
    }
}