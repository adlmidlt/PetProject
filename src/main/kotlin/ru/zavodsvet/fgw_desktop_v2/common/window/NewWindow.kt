package ru.zavodsvet.fgw_desktop_v2.common.window

import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.stage.Modality
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggCreatedWindow
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.logoCompany

/**
 * Новое окно.
 */
class NewWindow(
    private var windows: WindowController
) {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(NewWindow::class.java)

    /**
     * Открыть окно(сцену).
     *
     * @param pathToView Путь до сцены.
     * @param title Название окна.
     * @param modality Модальность окна.
     */
    fun openWindow(pathToView: String, title: String, modality: Modality?): WindowController {
        if (windows.controller == null) {
            windows.stage = Stage()
            windows.stage?.initModality(modality)
            windows.controller = pathToView.createWindow(windows.stage!!, title)
        } else {
            windows.stage!!.close()
            windows.stage!!.show()
        }
        closeWindow(windows.stage!!)

        return WindowController(windows.stage, windows.controller)
    }

    /**
     * Создать окно.
     *
     * @param stage Сцена окна.
     * @param titleWindow Название окна.
     */
    private fun String.createWindow(stage: Stage, titleWindow: String): Any {
        val fxmlLoader = FXMLLoader(this@NewWindow::class.java.getResource(this))
        val scene = Scene(fxmlLoader.load())
        val newController: Any = fxmlLoader.getController()

        logoCompany(stage)
        stage.title = titleWindow
        stage.scene = scene
        stage.show()

        logger.info(loggCreatedWindow(newController))

        return newController
    }

    /**
     * Закрыть окно.
     *
     * @param stage Сцена окна.
     */
    private fun closeWindow(stage: Stage) {
        stage.addEventHandler(KeyEvent.KEY_RELEASED) { event: KeyEvent ->
            if (KeyCode.ESCAPE == event.code) {
                stage.close()
                windows.controller = null
                windows.stage = null
            }
        }
        stage.setOnCloseRequest {
            stage.close()
            windows.controller = null
            windows.stage = null
        }
    }
}