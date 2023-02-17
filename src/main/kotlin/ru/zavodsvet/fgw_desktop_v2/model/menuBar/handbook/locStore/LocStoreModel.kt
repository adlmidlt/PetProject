package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.locStore

/**
 * Модель: "Участка хранения".
 */
class LocStoreModel(
    /**
     * ИД Участка хранения.
     */
    var id: Int,

    /**
     * Родитель ИД Участка хранения.
     */
    var parid: Int,

    /**
     * Наименование участка хранения.
     */
    var name: String,

    /**
     * Комментарий.
     */
    var comm: String,

    /**
     * Полезная площадь.
     */
    var area: Double,

    /**
     * Возможный процент использования.
     */
    var limit: Double,

    /**
     * Крытая площадка.
     */
    var cover: Int,

    /**
     * Наличие ЖД путей.
     */
    var rzd: Int,

    /**
     * Архивная запись.
     */
    var archive: Int,

    /**
     * Заполняемость склада.
     */
    var fill: Double,

    ) {
    constructor() : this(0, 0, "", "", 0.0, 0.0, 0, 0, 0, 0.0)

    override fun toString(): String {
        return "LocStore(id=$id, parid=$parid, name='$name', comm='$comm', area=$area, limit=$limit, cover=$cover, rzd=$rzd, archive=$archive, fill=$fill)"
    }
}