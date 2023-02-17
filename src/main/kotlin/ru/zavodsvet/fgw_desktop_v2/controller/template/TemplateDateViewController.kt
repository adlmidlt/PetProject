package ru.zavodsvet.fgw_desktop_v2.controller.template

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TextField
import ru.zavodsvet.fgw_desktop_v2.common.*
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.currentDateFormat
import ru.zavodsvet.fgw_desktop_v2.common.GlobalVariable.dateNow
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.time.DateTimeException
import java.time.LocalDate
import java.util.*

/**
 * Контроллер окна: "Шаблон даты".
 */
class TemplateDateViewController : Initializable {
    /**
     * Поле: Год.
     */
    @FXML
    private lateinit var yearTF: TextField

    /**
     * Поле: Месяц.
     */
    @FXML
    private lateinit var monthValueTF: TextField

    /**
     * Поле: День.
     */
    @FXML
    private lateinit var dayOfMonthTF: TextField

    /**
     * Валидация текстового поля
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        initNowDate()
        getTemplateDate()
    }

    /**
     * Инициализировать текущую дату в поля.
     */
    fun initNowDate() {
        yearTF.text = dateNow.year.toString()
        monthValueTF.text = dateNow.month.value.toString()
        dayOfMonthTF.text = dateNow.dayOfMonth.toString()

        if (dateNow.month.value < TWO_DIGIT_NUMBER) monthValueTF.text = "0${dateNow.month.value}"
        if (dateNow.dayOfMonth < TWO_DIGIT_NUMBER) dayOfMonthTF.text = "0${dateNow.dayOfMonth}"
    }

    /**
     * Установить шаблон: Дата.
     *
     * @param year Год.
     * @param month Месяц.
     * @param day День.
     */
    fun setTemplateDate(year: Int, month: Int, day: Int) {
        yearTF.text = year.toString()
        monthValueTF.text = month.toString()
        dayOfMonthTF.text = day.toString()

        if (month < TWO_DIGIT_NUMBER) monthValueTF.text = "0${month}"
        if (day < TWO_DIGIT_NUMBER) dayOfMonthTF.text = "0${day}"
    }

    /**
     * Заполнить распарсенный шаблон дата.
     *
     * @param date Дата.
     */
    fun fillParseTemplateDate(date: String) {
        val parseDateTime = LocalDate.parse(date, currentDateFormat)
        setTemplateDate(parseDateTime.year, parseDateTime.monthValue, parseDateTime.dayOfMonth)
    }

    /**
     * Получить шаблон: Дата.
     */
    fun getTemplateDate() =
        "${getValueTFYear(yearTF)}-${getValueTFMonthOfValue(monthValueTF)}-${getValueTFDayOfMonth(dayOfMonthTF)}"

    /**
     * Добавить 0 перед однозначным числом.
     *
     * @param focus Свойства фокуса текстового поля.
     * @param tf Текстовое поле.
     */
    fun addZeroBeforeSingleDigit(focus: Boolean, tf: TextField) {
        if (!focus && tf.text.length == 1) tf.text = "0${tf.text[0]}"
    }

    /**
     * Выделить текстовое поле в фокусе.
     *
     * @param tf Тип текстового поля.
     */
    fun highlightTextInField(tf: TextField) {
        Platform.runLater {
            tf.apply {
                if (isFocused && text.isNotEmpty()) {
                    selectAll()
                }
            }
        }
    }

    /**
     * Фокус на текстовое поле: Год. Получить
     *
     * @param tf Текстовое поле.
     */
    private fun getValueTFYear(tf: TextField): String {
        tf.apply {
            focusedProperty().addListener { _, _, _ ->
                highlightTextInField(this)
                textProperty().addListener { _, _, newValue -> yearTFValidation(this, newValue) }
            }
        }

        return tf.text
    }

    /**
     * Фокус на текстовое поле: Месяц.
     *
     * @param tf Текстовое поле.
     */
    private fun getValueTFMonthOfValue(tf: TextField): String {
        tf.apply {
            focusedProperty().addListener { _, _, newFocus ->
                addZeroBeforeSingleDigit(newFocus, this)
                highlightTextInField(this)
                textProperty().addListener { _, _, newValue -> monthOfValueTFValidation(this, newValue) }
            }
        }

        return tf.text
    }

    /**
     * Фокус на текстовое поле: День.
     *
     * @param tf Текстовое поле.
     */
    private fun getValueTFDayOfMonth(tf: TextField): String {
        tf.apply {
            focusedProperty().addListener { _, _, newFocus ->
                addZeroBeforeSingleDigit(newFocus, this)
                highlightTextInField(this)
                textProperty().addListener { _, _, newValue -> dayOfMonthTFValidation(this, newValue) }
            }
        }

        return tf.text
    }


    /**
     * Валидация поля: Год.
     *
     * @param tf Текстовое поле.
     * @param newValue Новое значение.
     */
    private fun yearTFValidation(tf: TextField, newValue: String) {
        textFieldValidation.correctInputOfFourDigits(tf, newValue)

        tf.apply {
            try {
                if (text.toInt() < MIN_YEAR || text.toInt() > MAX_YEAR) {
                    dateNow.withYear(text.toInt())
                    tooltip.text = NOT_CORRECT_YEAR
                    style = BORDER_AN_INVALID_FIELD
                } else {
                    style = null
                    tooltip.text = FORMAT_YEAR
                }
            } catch (eNum: NumberFormatException) {
                tooltip.text = NOT_CORRECT_YEAR
                style = BORDER_AN_INVALID_FIELD
            } catch (exDateTime: DateTimeException) {
                tooltip.text = NOT_CORRECT_YEAR
                style = BORDER_AN_INVALID_FIELD
            }
        }
    }

    /**
     * Валидация поля: Месяц.
     *
     * @param tf Текстовое поле.
     * @param newValue Новое значение.
     */
    private fun monthOfValueTFValidation(tf: TextField, newValue: String) {
        textFieldValidation.correctInputOfTwoDigits(tf, newValue)

        tf.apply {
            try {
                dateNow.withMonth(newValue.toInt())
                style = null
                text = newValue
                tooltip.text = FORMAT_MONTH
            } catch (eNum: NumberFormatException) {
                tooltip.text = NOT_CORRECT_MONTH_OF_YEAR
                style = BORDER_AN_INVALID_FIELD
            } catch (exDateTime: DateTimeException) {
                tooltip.text = NOT_CORRECT_MONTH_OF_YEAR
                style = BORDER_AN_INVALID_FIELD
            }
        }
    }

    /**
     * Валидация поля: День.
     *
     * @param tf Текстовое поле.
     * @param newValue Новое значение.
     */
    private fun dayOfMonthTFValidation(tf: TextField, newValue: String) {
        textFieldValidation.correctInputOfTwoDigits(tf, newValue)

        tf.apply {
            try {
                dateNow.withDayOfMonth(newValue.toInt())
                style = null
                text = newValue
                tooltip.text = FORMAT_DAY
            } catch (eNum: NumberFormatException) {
                tooltip.text = NOT_CORRECT_DAY_OF_MONTH
                style = BORDER_AN_INVALID_FIELD
            } catch (exDateTime: DateTimeException) {
                tooltip.text = NOT_CORRECT_DAY_OF_MONTH
                style = BORDER_AN_INVALID_FIELD
            }
        }
    }


}