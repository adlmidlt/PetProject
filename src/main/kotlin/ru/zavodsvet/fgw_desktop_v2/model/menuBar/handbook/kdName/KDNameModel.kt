package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.kdName

/**
 * Модель: "Конструкторское наименование".
 */
data class KDNameModel(
    /**
     * ИД Конструкторского наименования.
     */
    var id: Int,

    /**
     * Конструкторское наименование.
     */
    var name: String,

    /**
     * Комментарии.
     */
    var comm: String,

    /**
     * Является архивом.
     */
    var archive: Boolean,
) {
    override fun toString(): String {
        return "KDNameModel(id=$id, name='$name', comm='$comm', archive=$archive)"
    }
}