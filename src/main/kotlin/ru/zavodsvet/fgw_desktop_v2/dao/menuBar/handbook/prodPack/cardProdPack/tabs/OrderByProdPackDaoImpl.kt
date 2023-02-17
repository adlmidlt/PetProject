package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.prodPack.cardProdPack.tabs

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_ORDERS_PALLETS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.cardProdPack.tabs.OrderByProdPackModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

class OrderByProdPackDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(OrderByProdPackDaoImpl::class.java)

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
     * Список ордеров варианта упаковки.
     */
    private val ordersPalletsList: ObservableList<OrderByProdPackModel> = FXCollections.observableArrayList()

    /**
     * Получить список ордеров по паллетам.
     *
     * @param packid Вариант упаковки.
     * @param locid Участок хранения.
     * @param ordDtn Начальная дата для фильтра ордеров.
     * @param ordDtk Конечная дата для фильтра ордеров.
     */
    fun getOrdersPallets(
        packid: Int?, locid: Int?, ordDtn: String?, ordDtk: String?
    ): ObservableList<OrderByProdPackModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_ORDERS_PALLETS)
            if (packid != null) pstm.setInt(1, packid) else pstm.setNull(1, Types.INTEGER)
            if (locid != null) pstm.setInt(2, locid) else pstm.setNull(2, Types.INTEGER)
            if (ordDtn != null) pstm.setString(3, ordDtn) else pstm.setNull(3, Types.DATE)
            if (ordDtk != null) pstm.setString(4, ordDtk) else pstm.setNull(4, Types.DATE)
            val rs = pstm.executeQuery()

            ordersPalletsList.removeAll(ordersPalletsList)
            while (rs.next()) ordersPalletsList.addAll(getOrderProdPack(rs))

            logger.info(
                loggStoredProcedure(
                    SV_SHOW_ORDERS_PALLETS,
                    "1_packid={$packid}, 2_locid={$locid}, 3_ord_dtn={$ordDtn}, 4_ord_dtk={$ordDtk}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getOrdersPallets", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getOrdersPallets", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getOrdersPallets")
        }

        return ordersPalletsList
    }

    /**
     * Получить ордер варианта упаковки.
     *
     * @param rs Набор данных.
     */
    private fun getOrderProdPack(rs: ResultSet) = OrderByProdPackModel(
        operid = rs.getInt("operid"),
        vidoper = rs.getString("vidoper"),
        oprcreate = rs.getString("oprcreate"),
        dtord = rs.getString("dtord"),
        locname = rs.getString("locname"),
        dtcreate = rs.getString("dtcreate"),
        kol = rs.getInt("kol"),
        pieces = rs.getInt("pieces"),
        balances = rs.getInt("balances"),
        pbalances = rs.getInt("pbalances"),
    )
}