package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack

/**
 * Модель: "Вариант упаковки".
 */
class ProdPackModel(
    /**
     * ИД.
     */
    var id: Int,
    /**
     * Артикул.
     */
    var art: String,
    /**
     * Конструкторское наименование.
     */
    var packname: String,
    /**
     * Цвет.
     */
    var color: String,
    /**
     * Рядность.
     */
    var rows: Int,
    /**
     * Количество в ряду.
     */
    var cou: Int,
    /**
     * Глубина Ширина Высота.
     */
    var dwh: String

) {
    constructor() : this(0, "", "", "", 0, 0, "")

    override fun toString(): String {
        return "ProdPackModel(id=$id, art='$art', packname='$packname', color='$color', rows=$rows, cou=$cou, dwh='$dwh')"
    }
}