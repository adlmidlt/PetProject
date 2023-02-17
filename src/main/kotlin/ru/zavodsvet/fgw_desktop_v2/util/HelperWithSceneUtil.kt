package ru.zavodsvet.fgw_desktop_v2.util

import javafx.event.Event
import javafx.scene.Node
import javafx.stage.Stage

/**
 * Помощник со сценой.
 */
class HelperWithSceneUtil {
    /**
     * Закрыть сцену после нажатия на кнопку.
     *
     * @param event Событие нажатие кнопки.
     */
    fun closeStage(event: Event) {
        val source: Node = event.source as Node
        val stage = source.scene.window as Stage

        stage.close()
    }
}