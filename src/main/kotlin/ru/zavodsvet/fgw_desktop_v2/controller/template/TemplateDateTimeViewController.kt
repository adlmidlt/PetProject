package ru.zavodsvet.fgw_desktop_v2.controller.template

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateTimeFormat
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.dateTimeNow
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*

/**
 * Контроллер окна шаблона: Дата время.
 */
class TemplateDateTimeViewController : Initializable {
    /**
     * Поле: Часы.
     */
    @FXML
    private lateinit var hoursTF: TextField

    /**
     * Поле: Минуты.
     */
    @FXML
    private lateinit var minutesTF: TextField

    /**
     * Сцена окна: Дата шаблона.
     */
    @FXML
    private lateinit var templateDateView: AnchorPane

    /**
     * Контроллер окна шаблона: Дата.
     */
    @FXML
    private lateinit var templateDateViewController: TemplateDateViewController

    /**
     * Валидация текстового поля
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initNowDateTime()
        getTemplateDateTime()
    }

    /**
     *  Инициализировать текущую дату время в поля.
     */
    private fun initNowDateTime() {
        templateDateViewController.initNowDate()
        hoursTF.text = dateTimeNow.hour.toString()
        minutesTF.text = dateTimeNow.minute.toString()

        if (dateTimeNow.hour < TWO_DIGIT_NUMBER) hoursTF.text = "0${dateTimeNow.hour}"
        if (dateTimeNow.minute < TWO_DIGIT_NUMBER) minutesTF.text = "0${dateTimeNow.minute}"
    }

    /**
     * Установить шаблон: Дата время.
     *
     * @param year Год.
     * @param month Месяц.
     * @param day День.
     * @param hours Часы.
     * @param minutes Минуты.
     */
    fun setTemplateDateTime(year: Int, month: Int, day: Int, hours: Int, minutes: Int) {
        templateDateViewController.setTemplateDate(year, month, day)
        hoursTF.text = hours.toString()
        minutesTF.text = minutes.toString()

        if (hours < TWO_DIGIT_NUMBER) hoursTF.text = "0${hours}"
        if (minutes < TWO_DIGIT_NUMBER) minutesTF.text = "0${minutes}"
    }

    /**
     * Заполнить распарсенный шаблон даты время.
     *
     * @param dateTime Дата время.
     */
    fun fillParseTemplateDateTime(dateTime: String) {
        val parseDateTime = LocalDateTime.parse(dateTime, currentDateTimeFormat)
        setTemplateDateTime(
            parseDateTime.year,
            parseDateTime.monthValue,
            parseDateTime.dayOfMonth,
            parseDateTime.hour,
            parseDateTime.minute,
        )
    }

    /**
     * Получить шаблон: Дата время.
     */
    fun getTemplateDateTime() =
        "${templateDateViewController.getTemplateDate()} ${getValueTFHoursOfDay(hoursTF)}:${getValueTFMinutesOfHour(minutesTF)}"

    /**
     * Фокус на текстовое поле: Часы.
     *
     * @param tf Текстовое поле.
     */
    private fun getValueTFHoursOfDay(tf: TextField): String {
        tf.apply {
            focusedProperty().addListener { _, _, newFocus ->
                templateDateViewController.addZeroBeforeSingleDigit(newFocus, this)
                templateDateViewController.highlightTextInField(this)
                textProperty().addListener { _, _, newValue -> hoursOfDayTFValidation(this, newValue) }
            }
        }

        return tf.text
    }

    /**
     * Фокус на текстовое поле: Минуты.
     *
     * @param tf Текстовое поле.
     */
    private fun getValueTFMinutesOfHour(tf: TextField): String {
        tf.apply {
            focusedProperty().addListener { _, _, newFocus ->
                templateDateViewController.addZeroBeforeSingleDigit(newFocus, this)
                templateDateViewController.highlightTextInField(this)
                textProperty().addListener { _, _, newValue -> minutesOfHourTFValidation(this, newValue) }
            }
        }

        return tf.text
    }

    /**
     * Валидация поля: Часы.
     *
     * @param tf    Текстовое поле.
     * @param newValue Новое значение.
     */
    private fun hoursOfDayTFValidation(tf: TextField, newValue: String) {
        textFieldValidation.correctInputOfTwoDigits(tf, newValue)
        tf.apply {
            try {
                dateTimeNow.withHour(newValue.toInt())
                style = null
                text = newValue
                tooltip.text = FORMAT_HOURS_OF_DAY
            } catch (eNum: NumberFormatException) {
                tooltip.text = NOT_CORRECT_HOURS
                style = BORDER_AN_INVALID_FIELD
            } catch (exDateTime: DateTimeException) {
                tooltip.text = NOT_CORRECT_MINUTES
                style = BORDER_AN_INVALID_FIELD
            }
        }
    }

    /**
     * Валидация поля: Минуты.
     *
     * @param tf    Текстовое поле.
     * @param newValue Новое значение.
     */
    private fun minutesOfHourTFValidation(tf: TextField, newValue: String) {
        textFieldValidation.correctInputOfTwoDigits(tf, newValue)
        tf.apply {
            try {
                dateTimeNow.withMinute(newValue.toInt())
                style = null
                text = newValue
                tooltip.text = FORMAT_MINUTES_OF_HOUR
            } catch (eNum: NumberFormatException) {
                tooltip.text = NOT_CORRECT_MINUTES
                style = BORDER_AN_INVALID_FIELD
            } catch (exDateTime: DateTimeException) {
                tooltip.text = NOT_CORRECT_MINUTES
                style = BORDER_AN_INVALID_FIELD
            }
        }
    }
}