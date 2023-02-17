package ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore.tabs

import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.conn
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.loggStoredProcedure
import ru.zavodsvet.fgw_desktop_v2.common.INFO_FOR_IT
import ru.zavodsvet.fgw_desktop_v2.dao.SV_ADD_LOC_STORE_MUL
import ru.zavodsvet.fgw_desktop_v2.dao.SV_GET_PNG_LOC_BY_ID
import ru.zavodsvet.fgw_desktop_v2.dao.SV_SET_PNG_LOC_BY_ID
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.ResourcesHandlerUtil
import java.io.FileInputStream
import java.io.InputStream
import java.sql.PreparedStatement
import java.sql.SQLException

/**
 * Реализация доступа к объектам данных: "Схем участка хранения".
 */
class SchemaLocStoreDaoImpl {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(SchemaLocStoreDaoImpl::class.java)

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
     * Обновить схему участка хранения.
     *
     * @param id ИД участка хранения.
     * @param fileInputStream Входной байтовый поток.
     */
    fun updSchemaLocStore(id: Int, fileInputStream: FileInputStream) {
        try {
            pstm = conn.prepareStatement(SV_SET_PNG_LOC_BY_ID)
            pstm.setInt(1, id)
            pstm.setBinaryStream(2, fileInputStream, fileInputStream.available())
            pstm.execute()

            logger.info(loggStoredProcedure(SV_SET_PNG_LOC_BY_ID, "1_png{$id}, 2_id={png}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "updSchemaLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "updSchemaLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "updSchemaLocStore")
        }
    }

    /**
     * Добавить несколько участков хранения.
     *
     * @param parid ИД родителя.
     * @param kol Кол-во под участков.
     */
    fun addMulLocStore(parid: Int, kol: Int) {
        try {
            pstm = conn.prepareStatement(SV_ADD_LOC_STORE_MUL)
            pstm.setInt(1, parid)
            pstm.setInt(2, kol)
            pstm.execute()

            logger.info(loggStoredProcedure(SV_ADD_LOC_STORE_MUL, "1_kodobj={0}, 2_parid={$parid}, 3_kol={$kol}"))
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "addMulLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "addMulLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "addMulLocStore")
        }
    }

    /**
     * Показать схему участка хранения.
     *
     * @param id ИД участка хранения.
     */
    fun showSchemaLocStore(id: Int): InputStream? {
        try {
            pstm = conn.prepareStatement(SV_GET_PNG_LOC_BY_ID)
            pstm.setInt(1, id)
            val rs = pstm.executeQuery()

            if (rs.next()) {
                val blob = rs.getBlob(1)
                if (blob != null) inputStream = blob.binaryStream else return null
            }

            logger.info(loggStoredProcedure(SV_GET_PNG_LOC_BY_ID, "1_id={$id}"))

            return inputStream
        } catch (eSQL: SQLException) {
            logger.error(eSQL.message)
            exceptionHandler.printStackTraceElem(eSQL, "showSchemaLocStore", "${eSQL.message}\n$INFO_FOR_IT")
        } catch (eNPE: NullPointerException) {
            logger.error(eNPE.message)
            exceptionHandler.printStackTraceElem(eNPE, "showSchemaLocStore", "${eNPE.message}\n$INFO_FOR_IT")
        } finally {
            resourcesHandler.freeingDBResource(pstm, "showSchemaLocStore")
        }

        return null
    }
}