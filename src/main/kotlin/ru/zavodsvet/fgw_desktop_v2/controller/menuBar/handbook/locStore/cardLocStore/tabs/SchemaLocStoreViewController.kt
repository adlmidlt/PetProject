package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.tabs

import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore.tabs.SchemaLocStoreDaoImpl
import java.io.FileInputStream

/**
 * Контроллер окна: "Схема участка хранения".
 */
class SchemaLocStoreViewController {
    /**
     * Добавить/обновить схему участка хранения.
     */
    @FXML
    private lateinit var loadImgBtn: Button

    /**
     * Картинка отображающая схему участка хранения.
     */
    @FXML
    private lateinit var pngSchemaLocStoreImageView: ImageView

    /**
     * Реализация доступа к объектам данным: "Схем участка хранения".
     */
    private var schemaLocStore = SchemaLocStoreDaoImpl()

    /**
     * Выбор файла.
     */
    private var fileChooser = FileChooser()

    /**
     * Показать схему участка хранения.
     *
     * @param id ИД участка хранения.
     */
    fun showSchemaLocStore(id: Int) {
        val schemaLocStore = schemaLocStore.showSchemaLocStore(id)
        if (schemaLocStore != null) {
            pngSchemaLocStoreImageView.image = Image(schemaLocStore)
        }
        loadSchemaLocStore(id)
    }

    /**
     * Загрузить схему участка хранения.
     *
     * @param id ИД участка хранения.
     */
    private fun loadSchemaLocStore(id: Int) {
        loadImgBtn.setOnAction {
            fileChooser.title = "Открыть файл"
            fileChooser.extensionFilters.addAll(FileChooser.ExtensionFilter("PNG", "*.png"))

            val choiceFile = fileChooser.showOpenDialog(loadImgBtn.scene.window)
            if (choiceFile != null) {
                val fileInputStream = FileInputStream(choiceFile)
                schemaLocStore.updSchemaLocStore(id, fileInputStream)
                pngSchemaLocStoreImageView.image = Image(fileInputStream)
                showSchemaLocStore(id)
                fileInputStream.close()
            }
        }
    }
}