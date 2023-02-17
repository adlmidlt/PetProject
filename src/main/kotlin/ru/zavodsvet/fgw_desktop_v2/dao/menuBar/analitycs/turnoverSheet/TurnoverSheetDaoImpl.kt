package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.analitycs.turnoverSheet

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_OBOROT_PALLETS
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.analitycs.turnoverSheet.TurnoverSheetModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types

/**
 * Реализация объекта доступа к данным аналитики: "Оборотная ведомость".
 */
class TurnoverSheetDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(TurnoverSheetDaoImpl::class.java)

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
     * Список "Оборотной ведомости".
     */
    private var turnoverSheetList: ObservableList<TreeItem<TurnoverSheetModel>> = FXCollections.observableArrayList()

    /**
     * Найти все оборотные ведомости.
     *
     * @param dtn Дата/время начала временного интервала.
     * @param dtk Дата/время окончания временного интервала.
     * @param locid Фильтр по месту хранения.
     * @param prodid Фильтр по ID конс.наим.
     * @param packid Фильтр по ID варианта упаковки.
     */
    fun findAllOborotPallets(
        dtn: String?,
        dtk: String?,
        locid: Int?,
        prodid: Int?,
        packid: Int?,
    ): ObservableList<TreeItem<TurnoverSheetModel>> {
        try {
            // Индексы идут не по порядку, избежал дубликат кода.
            pstm = conn.prepareStatement(SV_SHOW_OBOROT_PALLETS)
            if (dtk != null) pstm.setString(2, dtk) else pstm.setNull(2, Types.DATE)
            if (dtn != null) pstm.setString(1, dtn) else pstm.setNull(1, Types.DATE)
            if (locid != null) pstm.setInt(3, locid) else pstm.setNull(3, Types.INTEGER)
            if (prodid != null) pstm.setInt(4, prodid) else pstm.setNull(4, Types.INTEGER)
            if (packid != null) pstm.setInt(5, packid) else pstm.setNull(5, Types.INTEGER)

            val rs = pstm.executeQuery()
            turnoverSheetList.removeAll(turnoverSheetList)
            while (rs.next()) turnoverSheetList.addAll(TreeItem(getTurnoverSheet(rs)))
            logger.info(
                loggStoredProcedure(
                    SV_SHOW_OBOROT_PALLETS,
                    "1_dtn={$dtn}, 2_dtk={$dtk}, 3_locid={$locid}, 4_prodid={$prodid}, 5_packid={$packid}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "showOborotPallets", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "showOborotPallets", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "showOborotPallets")
        }

        return turnoverSheetList
    }

    /**
     * Получить оборотную ведомость.
     *
     * @param rs Набор данных.
     */
    private fun getTurnoverSheet(rs: ResultSet) = TurnoverSheetModel(
        prodid = rs.getInt("prodid"),
        packid = rs.getInt("packid"),
        konsname = rs.getString("konsname"),
        art = rs.getString("art"),
        dtcreate = rs.getString("dtcreate"),
        numpart = rs.getInt("numpart"),
        comm = rs.getString("comm"),
        color = rs.getString("color"),
        rows = rs.getInt("rows"),
        cou = rs.getInt("cou"),
        dwh = rs.getString("dwh"),
        locid = rs.getInt("locid"),
        locname = rs.getString("locname"),
        balin = rs.getInt("balin"),
        balprih = rs.getInt("balprih"),
        balrash = rs.getInt("balrash"),
        balout = rs.getInt("balout"),
        balinp = rs.getInt("balinp"),
        balprihp = rs.getInt("balprihp"),
        balrashp = rs.getInt("balrashp"),
        baloutp = rs.getInt("baloutp"),
    )
}