package ru.zavodsvet.fgw_desktop_v2.util

import javafx.scene.control.TextField
import ru.zavodsvet.fgw_desktop_v2.common.BORDER_AN_INVALID_FIELD
import ru.zavodsvet.fgw_desktop_v2.common.NOT_CORRECT_FORMAT

/**
 * Валидация текстового поля.
 */
class TextFieldValidationUtil {
    /**
     * Корректность ввода символов без апострофов.
     *
     * @param textField Поле текста.
     * @param value Подставляется значение.
     */
    fun correctInputSymbolsWithoutApostrophes(textField: TextField, value: String) {
        value.map {
            textField.apply {
                text = (if (it != '\'') value else value.replace(it.toString(), ""))
            }
        }
    }

    /**
     * Корректность ввода цифр.
     *
     * @param textField Поле текста.
     * @param value Вводимое значение с клавиатуры.
     */
    fun correctInputNumber(textField: TextField, value: String) {
        value.map {
            textField.apply {
                text = (if (it.isDigit() || it == '-') value else value.replace(it.toString(), ""))
            }
        }
    }

    /**
     * Корректность ввода 4-х цифр.
     *
     * @param textField Поле текста.
     * @param value Вводимое значение с клавиатуры.
     */
    fun correctInputOfFourDigits(textField: TextField, value: String) {
        value.map {
            if (!it.isDigit()) {
                textField.text = value.replace(it.toString(), "")
            }
        }
        if (value.length > 4) {
            textField.text = value.substring(0, 4)
        }
    }

    /**
     * Корректность ввода 2-х цифр.
     *
     * @param textField Поле текста.
     * @param value Вводимое значение с клавиатуры.
     */
    fun correctInputOfTwoDigits(textField: TextField, value: String) {
        value.map {
            if (!it.isDigit()) {
                textField.text = value.replace(it.toString(), "")
            }
        }
        if (value.length > 2) {
            textField.text = value.substring(0, 2)
        }
    }

    /**
     * Корректность ввода вещественных чисел типа DOUBLE.
     *
     * @param textField Поле текста.
     * @param value Подставляется значение.
     */
    fun correctInputDoubleNumber(textField: TextField, value: String) {
        value.map {
            textField.apply {
                try {
                    if (it.isDigit() || it == '.' || it == '-') {
                        text.toDouble()
                        style = null
                    } else {
                        text = value.replace(it.toString(), "")
                        tooltip.text = NOT_CORRECT_FORMAT
                    }
                } catch (e: NumberFormatException) {
                    tooltip.text = NOT_CORRECT_FORMAT
                    style = BORDER_AN_INVALID_FIELD
                }
            }
        }
    }
}
