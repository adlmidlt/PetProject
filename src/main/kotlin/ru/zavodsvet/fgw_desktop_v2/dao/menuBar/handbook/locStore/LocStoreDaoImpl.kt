package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_ADD_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.dao.SV_DEL_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_SHOW_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.locStore.LocStoreModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным справочника: "Участки хранения".
 */
class LocStoreDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(LocStoreDaoImpl::class.java)

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
     * Список участков хранения.
     */
    private var locStoreTreeItemList: ObservableList<TreeItem<LocStoreModel>> = FXCollections.observableArrayList()

    /**
     * Вернуть список участков хранения.
     *
     * @param parid ИД родителя.
     * @param archive Является ли архивом.
     */
    fun getShowLocStore(parid: Int, archive: Int): ObservableList<TreeItem<LocStoreModel>> {
        try {
            pstm = conn.prepareStatement(SV_GET_SHOW_LOC_STORE)
            pstm.setInt(1, parid)
            pstm.setInt(2, archive)
            val rs = pstm.executeQuery()

            locStoreTreeItemList.removeAll(locStoreTreeItemList)
            while (rs.next()) locStoreTreeItemList.addAll(getLocStore(rs))

            logger.info(
                loggStoredProcedure(
                    SV_GET_SHOW_LOC_STORE, "1_kodobj={0}, 2_parid={$parid}, 3_archive={$archive}"
                )
            )
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElemWithExitProcess(eSQL, "getLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElemWithExitProcess(eNPE, "getLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getLocStore")
        }

        return locStoreTreeItemList
    }

    /**
     * Удалить участок хранения по ИД.
     *
     * @param locid ИД Участка хранения.
     */
    fun delLocStore(locid: Int) {
        try {
            pstm = conn.prepareStatement(SV_DEL_LOC_STORE)
            pstm.setInt(1, locid)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_DEL_LOC_STORE, "1_locid={$locid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "delLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "delLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "delLocStore")
        }
    }

    /**
     * Добавить новый склад и вернуть его ИД.
     *
     * @param parid ИД родителя.
     */
    fun addLocStore(parid: Int): Int {
        try {
            pstm = conn.prepareStatement(SV_ADD_LOC_STORE)
            pstm.setInt(1, parid)
            val rs = pstm.executeQuery()

            while (rs.next()) return rs.getInt("id")

            logger.info(loggStoredProcedure(SV_ADD_LOC_STORE, "1_kodobj={0}, 2_parid={$parid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addLocStore")
        }

        return 0
    }

    /**
     * Получить участок хранения.
     *
     * @param rs Набор данных.
     */
    fun getLocStore(rs: ResultSet) = TreeItem(
        LocStoreModel(
            id = rs.getInt("id"),
            parid = rs.getInt("parid"),
            name = rs.getString("name"),
            comm = rs.getString("comm"),
            area = rs.getDouble("area"),
            limit = rs.getDouble("limit"),
            cover = rs.getInt("cover"),
            rzd = rs.getInt("rzd"),
            archive = rs.getInt("archive"),
            fill = rs.getDouble("fill")
        )
    )
}