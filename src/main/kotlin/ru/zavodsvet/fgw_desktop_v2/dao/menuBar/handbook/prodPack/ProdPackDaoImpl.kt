package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.prodPack

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_PROD_PACK
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_PROD_PACK_BY_ID
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.prodPack.ProdPackModel
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Реализация объекта доступа к данным справочника: "Варианты упаковки".
 */
class ProdPackDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ProdPackDaoImpl::class.java)

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
     * Список вариантов упаковки.
     */
    private val prodPackList: ObservableList<ProdPackModel> = FXCollections.observableArrayList()

    /**
     * Вариант упаковки.
     */
    private var prodPack = ProdPackModel()

    /**
     * Получить список не архивных вариантов упаковки продукции.
     */
    fun getAllProdPackList(): ObservableList<ProdPackModel> {
        try {
            pstm = conn.prepareStatement(SV_GET_PROD_PACK)
            val rs = pstm.executeQuery()

            prodPackList.removeAll(prodPackList)
            while (rs.next()) prodPackList.addAll(getProdPack(rs))

            logger.info(loggStoredProcedure(SV_GET_PROD_PACK, ""))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getAllProdPackList", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getAllProdPackList", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getAllProdPackList")
        }

        return prodPackList
    }

    /**
     * Получить вариант упаковки по ИД.
     *
     * @param idprod ИДВариант упаковки.
     */
    fun getProdPackByIdProd(idprod: Int): ProdPackModel {
        try {
            pstm = conn.prepareStatement(SV_GET_PROD_PACK_BY_ID)
            pstm.setInt(1, idprod)
            val rs = pstm.executeQuery()

            prodPackList.removeAll(prodPackList)
            while (rs.next()) prodPack = getProdPack(rs)

            logger.info(loggStoredProcedure(SV_GET_PROD_PACK_BY_ID, "1_idprod={$idprod}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "getProdPackByIdProd", "${eSQL.message}")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "getProdPackByIdProd", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "getProdPackByIdProd")
        }

        return prodPack
    }

    /**
     * Получить вариант упаковки.
     *
     * @param rs Набор данных.
     */
    private fun getProdPack(rs: ResultSet) = ProdPackModel(
        id = rs.getInt("id"),
        art = rs.getString("art"),
        packname = rs.getString("packname"),
        color = rs.getString("color"),
        rows = rs.getInt("rows"),
        cou = rs.getInt("cou"),
        dwh = rs.getString("dwh"),
    )
}