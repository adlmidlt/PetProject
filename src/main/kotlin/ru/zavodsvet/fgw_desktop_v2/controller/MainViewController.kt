package ru.zavodsvet.fgw_desktop_v2.controller

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.auth
import java.net.URL
import java.util.*

/**
 * Контроллер главного окна.
 */
class MainViewController : Initializable {
    /**
     * Логотип.
     */
    @FXML
    private lateinit var logoImageView: ImageView

    /**
     * Подвал внизу программы отображает ФИО с табельным номером.
     */
    @FXML
    private lateinit var footerFIOWithTNLabel: Label

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        logoImageView.image = Image("img/logo.png")
        footerFIOWithTNLabel.text = "${auth.fio} (${auth.perif})"
    }
}