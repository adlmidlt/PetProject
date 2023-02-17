package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.vidOper

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_VID_OPER
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.vidOper.VidOperationModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным справочника: "Вид операции".
 */
class VidOperationDaoImpl {

    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(VidOperationDaoImpl::class.java)

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
     * Список видов операции.
     */
    private val vidOperList: ObservableList<VidOperationModel> = FXCollections.observableArrayList()

    /**
     * Получить список видов операции.
     */
    fun getAllVidOper(): ObservableList<VidOperationModel> {
        try {
            pstm = conn.prepareStatement(SV_GET_VID_OPER)
            val rs = pstm.executeQuery()

            vidOperList.removeAll(vidOperList)
            while (rs.next()) vidOperList.addAll(getVidOper(rs))

            logger.info(loggStoredProcedure(SV_GET_VID_OPER, ""))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getAllVidOper", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getAllVidOper", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getAllVidOper")
        }

        return vidOperList
    }

    /**
     * Получить вид операции.
     *
     * @param rs Набор данных.
     */
    private fun getVidOper(rs: ResultSet) = VidOperationModel(
        kod = rs.getInt("kod"),
        name = rs.getString("name")
    )
}