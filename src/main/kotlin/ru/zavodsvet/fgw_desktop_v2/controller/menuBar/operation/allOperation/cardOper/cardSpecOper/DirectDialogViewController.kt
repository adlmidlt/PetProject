package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.allOperation.cardOper.cardSpecOper

import javafx.fxml.FXML
import javafx.scene.control.Button
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil

/**
 * Контроллер диалогового окна: "Выбора при пересортице".
 */
class DirectDialogViewController {
    /**
     * Кнопка выбора: "Для прихода".
     */
    @FXML
    private lateinit var forArrivalBtn: Button

    /**
     * Кнопка прихода: "Для списания".
     */
    @FXML
    private lateinit var forDebitBtn: Button

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Диалоговое окно выбора: "Приход" или "Списание".
     *
     * @param callback Выбор.
     */
    fun dialogWindowChoice(callback: ((Boolean) -> Unit)) {
        forArrivalBtn.setOnAction { ev ->
            callback(false)
            helperWithScene.closeStage(ev)
        }
        forDebitBtn.setOnAction { ev ->
            callback(true)
            helperWithScene.closeStage(ev)
        }
    }
}