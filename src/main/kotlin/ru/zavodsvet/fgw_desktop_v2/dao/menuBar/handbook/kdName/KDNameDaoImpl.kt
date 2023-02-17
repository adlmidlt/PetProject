package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.kdName

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SHOW_KD_NAMES
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.kdName.KDNameModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным справочника: "Конструкторские наименования".
 */
class KDNameDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(KDNameDaoImpl::class.java)

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
     * Список конструкторских наименований.
     */
    private var kdNameList: ObservableList<KDNameModel> = FXCollections.observableArrayList()

    /**
     * Получить список конструкторских наименований.
     */
    fun getAllKDNames(): ObservableList<KDNameModel> {
        try {
            pstm = conn.prepareStatement(SV_SHOW_KD_NAMES)
            val rs = pstm.executeQuery()

            kdNameList.removeAll(kdNameList)
            while (rs.next()) kdNameList.addAll(getKDName(rs))

            logger.info(loggStoredProcedure(SV_SHOW_KD_NAMES, ""))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getAllKDNames", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getAllKDNames", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getAllKDNames")
        }

        return kdNameList
    }

    /**
     * Получить конструкторское наименование.
     *
     * @param rs Набор данных.
     */
    private fun getKDName(rs: ResultSet) = KDNameModel(
        id = rs.getInt("id"),
        name = rs.getString("name"),
        comm = rs.getString("comm"),
        archive = rs.getBoolean("archive")
    )
}