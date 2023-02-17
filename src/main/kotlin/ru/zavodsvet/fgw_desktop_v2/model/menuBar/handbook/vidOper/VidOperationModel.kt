package ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.vidOper

/**
 * Модель: "Вид операции".
 */
data class VidOperationModel(
    /**
     * Код операции.
     */
    var kod: Int,

    /**
     * Название операции.
     */
    var name: String,
) {
    override fun toString(): String {
        return "VidOperationModel(kod=$kod, name='$name')"
    }
}