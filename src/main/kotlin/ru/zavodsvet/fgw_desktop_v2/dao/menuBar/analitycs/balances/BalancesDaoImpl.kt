package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.balances

import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_BALANCES_BY_PALLETS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.balances.BalancesModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Реализация объекта доступа к данным аналитики: "Остатки".
 */
class BalancesDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(BalancesDaoImpl::class.java)

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
     * Показать остатков пакет-поддонов.
     *
     * @param prodid ID продукции (конс.наим.).
     * @param packid ID варианта упаковки.
     * @param layer Разрез хранения.
     * @param locid Участок хранения.
     * @param dtbal Дата/время, на которые рассчитываются остатки.
     * @param grpackid По вариантам упаковки.
     * @param grlayer По разрезам хранения.
     * @param grlocid По участкам хранения.
     * @param grchild По дочерним участкам хранения.
     */
    fun findAllBalancesPallets(
        dataList: ObservableList<BalancesModel>?,
        prodid: Int?,
        packid: Int?,
        layer: String?,
        locid: Int?,
        dtbal: String?,
        grpackid: Int,
        grlayer: Int,
        grlocid: Int,
        grchild: Int,
    ) {
        try {
            pstm = conn.prepareStatement(SV_SHOW_BALANCES_BY_PALLETS)

            if (prodid != null) pstm.setInt(1, prodid) else pstm.setNull(1, Types.INTEGER)
            if (packid != null && packid > 0) pstm.setInt(2, packid) else pstm.setNull(2, Types.INTEGER)
            if (!layer.isNullOrEmpty()) pstm.setString(3, layer) else pstm.setNull(3, Types.VARCHAR)
            if (locid != null && locid > 0) pstm.setInt(4, locid) else pstm.setNull(4, Types.INTEGER)
            if (dtbal != null) pstm.setString(5, dtbal.toString()) else pstm.setNull(5, Types.DATE)
            pstm.setInt(6, grpackid)
            pstm.setInt(7, grlayer)
            pstm.setInt(8, grlocid)
            pstm.setInt(9, grchild)
            val rs = pstm.executeQuery()

            while (rs.next()) dataList!!.addAll(getBalancesPallets(rs))

            logger.info(
                loggStoredProcedure(
                    SV_SHOW_BALANCES_BY_PALLETS,
                    "1_prodid={$prodid}, 2_packid={$packid}, 3_layer={$layer}, 4_locid={$locid}, 5_dtbal={$dtbal}, 6_grpackid={$grpackid}, 7_grlayer={$grlayer}, 8_grlocid={$grlocid}, 9_grchild={$grchild}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "findAllBalancesPallets", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "findAllBalancesPallets", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "findAllBalancesPallets")
        }
    }

    /**
     * Получить остатки пакет-поддонов.
     *
     * @param rs Набор данных.
     */
    private fun getBalancesPallets(rs: ResultSet) = BalancesModel(
        prodid = rs.getInt("prodid"),
        packid = rs.getInt("packid"),
        art = rs.getString("art"),
        konsname = rs.getString("konsname"),
        color = rs.getString("color"),
        rows = rs.getInt("rows"),
        cou = rs.getInt("cou"),
        dwh = rs.getString("dwh"),
        dtcreate = rs.getString("dtcreate"),
        numpart = rs.getInt("numpart"),
        comm = rs.getString("comm"),
        locid = rs.getInt("locid"),
        locname = rs.getString("locname"),
        balances = rs.getInt("balances"),
        pieces = rs.getInt("pieces"),
    )
}