package ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.allOperation

/**
 * Модель: "Операции".
 */
data class OperationModel(
    /**
     * ИД.
     */
    var id: Int,

    /**
     * Вид операции.
     */
    var vidoper: String,

    /**
     * Дата создания операции.
     */
    var dtcreate: String,

    /**
     * Создал операцию.
     */
    var perfcreate: String,

    /**
     * Дата создания ордера.
     */
    var dtord: String?,

    /**
     * Табельный номер создателя.
     */
    var perford: String,

    /**
     * Количество изделий (бутылок).
     */
    var pieces: Int,

    /**
     * Номер операции.
     */
    var oper: Int,

    /**
     * Дата начала автоматизированного прихода.
     */
    var dtn: String?,

    /**
     * Номер привязанного пропуска.
     */
    var permit: String?

) {
    constructor() : this(0, "", "", "", "", "", 0, 0, null, null)

    override fun toString(): String {
        return "OperationModel(id=$id, vidoper='$vidoper', dtcreate='$dtcreate', perfcreate='$perfcreate', dtord=$dtord, perford='$perford', pieces=$pieces, oper=$oper, dtn=$dtn, permit=$permit)"
    }
}
