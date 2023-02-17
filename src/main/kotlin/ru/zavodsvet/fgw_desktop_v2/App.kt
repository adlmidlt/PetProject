package ru.zavodsvet.fgw_desktop_v2

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.APP_START
import ru.zavodsvet.fgw_desktop_v2.common.APP_STOP
import ru.zavodsvet.fgw_desktop_v2.common.AUTH_VIEW
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateTime
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.logoCompany
import ru.zavodsvet.fgw_desktop_v2.common.TITLE_AUTH_TLK
import ru.zavodsvet.fgw_desktop_v2.config.DBConfig

class App : Application() {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(App::class.java)

    override fun start(primaryStage: Stage) {
        DBConfig().dbConnection()

        val fxmlLoader = FXMLLoader(App::class.java.getResource(AUTH_VIEW))
        val scene = Scene(fxmlLoader.load())
        logoCompany(primaryStage)
        primaryStage.title = TITLE_AUTH_TLK
        primaryStage.scene = scene
        primaryStage.show()
        logger.info(APP_START)

        primaryStage.setOnCloseRequest {
            loggStoredProcedure("MAIN_VIEW", "Выход из программы={$currentDateTime}")
            DBConfig().dbDisconnect()
            logger.info(APP_STOP)
            Platform.exit()
        }
    }
}

fun main() {
    Application.launch(App::class.java)
}