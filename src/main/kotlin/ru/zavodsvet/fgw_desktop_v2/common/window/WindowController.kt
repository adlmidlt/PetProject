package ru.zavodsvet.fgw_desktop_v2.common.window

import javafx.stage.Stage

/**
 * Контролер окон.
 */
data class WindowController(
    /**
     * Сцена.
     */
    var stage: Stage? = null,

    /**
     * Контроллер.
     */
    var controller: Any? = null
) {
    constructor(): this(null, null)
}