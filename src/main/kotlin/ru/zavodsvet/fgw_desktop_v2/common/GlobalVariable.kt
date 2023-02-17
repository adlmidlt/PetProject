package ru.zavodsvet.fgw_desktop_v2.common

import javafx.scene.image.Image
import javafx.stage.Stage
import ru.zavodsvet.fgw_desktop_v2.dao.login.EventEmployeeDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.login.AuthModel
import java.net.InetAddress
import java.sql.Connection
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Хранение глобальных переменных.
 */
object GlobalVariable {

    const val END_DAY = " 23:59"
    const val START_DAY = " 00:00"

    /**
     * Подключение к БД.
     */
    lateinit var conn: Connection

    /**
     * Дата сейчас.
     */
    var dateNow: LocalDate = LocalDate.now()

    /**
     * Дата время сейчас.
     */
    var dateTimeNow: LocalDateTime = LocalDateTime.now()

    /**
     * Текущая дата время.
     */
    var currentDateTime: String = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())

    /**
     * Формат текущего дата время.
     */
    var currentDateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Формат текущей даты.
     */
    var currentDateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    /**
     * Авторизация сотрудника ТЛК(товарно-логистический комплекс).
     */
    var auth = AuthModel()

    /**
     * Реализация объекта доступа к данным события сотрудника.
     */
    private var eventEmployeeDaoImpl = EventEmployeeDaoImpl()

    /**
     * Логотип компании.
     *
     * @param stage Сцена.
     */
    fun logoCompany(stage: Stage) {
        stage.icons.add(Image("img/logo.png"))
    }

    /**
     * Логирование хранимой процедуры.
     *
     * @param storedProcedure Хранимая процедура.
     * @param params Параметры хранимой процедуры.
     */
    fun loggStoredProcedure(storedProcedure: String, params: String): String {
        val queryWithoutParams = storedProcedure.replace("?", "", false).replace(",", "", false).replace("0", "", false)
        val loggEventEmployee = "${getNameHostPC()} | $queryWithoutParams $params"

        return "${eventEmployeeDaoImpl.addEventEmployee(loggEventEmployee)}"
    }

    /**
     * Логирование созданного окна.
     *
     * @param ignoredController Контроллер сцены.
     */
    fun loggCreatedWindow(ignoredController: Any) = "Окно [${ignoredController::class.java}] успешно запущено!"

    /**
     * Получить имя компьютера.
     */
    private fun getNameHostPC() = InetAddress.getLocalHost().hostName

}