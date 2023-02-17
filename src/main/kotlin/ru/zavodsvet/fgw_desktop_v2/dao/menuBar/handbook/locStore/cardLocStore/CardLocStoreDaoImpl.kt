package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore

import javafx.scene.control.TreeItem
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_EDIT_LOC_STORE
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_LOC_STORE_PALLETS
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.LocStoreDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.locStore.LocStoreModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным: "Карточки участка хранения".
 */
class CardLocStoreDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardLocStoreDaoImpl::class.java)

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
     * Участок хранения п/п.
     */
    private var locStorePallets: TreeItem<LocStoreModel> = TreeItem<LocStoreModel>()

    /**
     * Реализация объекта доступа к данным справочника: "Участки хранения".
     */
    private var locStoreDaoImpl = LocStoreDaoImpl()

    /**
     * Получить карточку участка хранения п/п.
     *
     * @param locid Ид участка хранения.
     */
    fun getLocStorePallets(locid: Int): TreeItem<LocStoreModel> {
        try {
            pstm = conn.prepareStatement(SV_GET_LOC_STORE_PALLETS)
            pstm.setInt(1, locid)
            val rs = pstm.executeQuery()

            while (rs.next()) locStorePallets = locStoreDaoImpl.getLocStore(rs)

            logger.info(loggStoredProcedure(SV_GET_LOC_STORE_PALLETS, "1_locid={$locid}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getLocStorePallets", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getLocStorePallets", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getLocStorePallets")
        }

        return locStorePallets
    }

    /**
     * Редактирование участка хранения по его ИД.
     *
     *  @param id ИД записи.
     *  @param field Имя поля для обновления данных.
     *  @param value Новое значения поля.
     */
    fun editLocStore(id: Int, field: String, value: String) {
        try {
            pstm = conn.prepareStatement(SV_EDIT_LOC_STORE)
            pstm.setInt(1, id)
            pstm.setString(2, field)
            pstm.setString(3, value)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_EDIT_LOC_STORE, "1_id={$id}, 2_field={$field}, 3_value={$value}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "editLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "editLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "editLocStore")
        }
    }
}