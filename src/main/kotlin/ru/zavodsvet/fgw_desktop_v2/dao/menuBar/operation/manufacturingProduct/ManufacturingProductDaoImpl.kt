package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.manufacturingProduct

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_GIF_TICKET_BY_ID
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_PACK_PALLETS_ACTIONS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.manufacturingProduct.ManufacturingProductModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.io.InputStream
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Объект доступа к данным операций "Производство продукции".
 */
class ManufacturingProductDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ManufacturingProductDaoImpl::class.java)

    /**
     * Объект, представляющий предварительно скомпилированный оператор SQL. (Для работы с параметрами.(?))
     */
    private lateinit var pstm: PreparedStatement

    /**
     * Обработчик исключений.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Обработчик ресурсов.
     */
    private var resourcesHandler = ResourcesHandlerUtil()

    /**
     * Представляет входной поток байтов.
     */
    private var inputStream: InputStream? = null

    /**
     * Список производства продукции.
     */
    private val manufacturingProductList: ObservableList<ManufacturingProductModel> =
        FXCollections.observableArrayList()

    /**
     * Показать журнал действия упаковки/разупаковки п/п.
     *
     * @param dtn Интервал дат/времени.
     * @param dtk Интервал дат/времени.
     * @param locid ИД участка хранения.
     */
    fun showPackPalletsActions(dtn: String?, dtk: String?, locid: Int?): ObservableList<ManufacturingProductModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_PACK_PALLETS_ACTIONS)
            if (dtn != null) pstm.setString(1, dtn) else pstm.setNull(1, Types.DATE)
            if (dtk != null) pstm.setString(2, dtk) else pstm.setNull(2, Types.DATE)
            if (locid != null) pstm.setInt(3, locid) else pstm.setNull(3, Types.INTEGER)

            val rs = pstm.executeQuery()
            manufacturingProductList.removeAll(manufacturingProductList)

            while (rs.next()) manufacturingProductList.addAll(getManufacturingProduct(rs))

            logger.info(
                loggStoredProcedure(SV_SHOW_PACK_PALLETS_ACTIONS, "1_dtn={$dtn}, 2_dtk={$dtk}, 3_locid={$locid}")
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "showPackPalletsActions", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "showPackPalletsActions", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "showPackPalletsActions")
        }

        return manufacturingProductList
    }

    /**
     * Получить GIF этикетки по её ИД.
     */
    fun getGifTicketById(id: Int): InputStream? {
        try {
            pstm = conn.prepareStatement(SV_GET_GIF_TICKET_BY_ID)
            pstm.setInt(1, id)
            val rs = pstm.executeQuery()

            if (rs.next()) {
                val blob = rs.getBlob(1)
                if (blob != null) inputStream = blob.binaryStream else return null
            }

            logger.info(loggStoredProcedure(SV_GET_GIF_TICKET_BY_ID, "1_id={$id}"))

            return inputStream
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getGifTicketById", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElemWithExitProcess(
                eNPE, "getGifTicketById", "${eNPE.message}\n$INFO_FOR_IT"
            )
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getGifTicketById")
        }

        return null
    }

    /**
     * Получить продукт производства.
     *
     * @param rs Набор данных.
     */
    private fun getManufacturingProduct(rs: ResultSet) = ManufacturingProductModel(
        art = rs.getString("art"),
        prod = rs.getString("prod"),
        barcode = rs.getString("barcode"),
        dtact = rs.getString("dtact"),
        dtcreate = rs.getString("dtcreate"),
        action = rs.getString("action"),
        fio = rs.getString("fio"),
        locpack = rs.getString("locpack"),
        locstore = rs.getString("locstore"),
        packId = rs.getInt("packid"),
        locstoreid = rs.getInt("locstoreid"),
        operid = rs.getInt("operid"),
        ticketid = rs.getInt("ticketid")
    )
}