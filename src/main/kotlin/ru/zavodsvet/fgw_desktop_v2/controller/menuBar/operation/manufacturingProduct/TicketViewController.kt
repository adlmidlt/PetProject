package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.manufacturingProduct

import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.manufacturingProduct.ManufacturingProductDaoImpl

/**
 * Контроллер окна: "Этикетка".
 */
class TicketViewController {
    /**
     * GIF этикетка упаковки продукта.
     */
    @FXML
    private lateinit var gifTicketProdPackImageView: ImageView

    /**
     * Объект доступа к данным операций "Производство продукции".
     */
    private var manufacturingProductDaoImpl = ManufacturingProductDaoImpl()

    /**
     * Показать GIF этикетку производства продукции.
     *
     * @param id ИД этикетки.
     */
    fun showGifTicketProdPack(id: Int) {
        val gifTicketById = manufacturingProductDaoImpl.getGifTicketById(id)
        if (gifTicketById != null) {
            gifTicketProdPackImageView.image = Image(gifTicketById)
        }
    }
}