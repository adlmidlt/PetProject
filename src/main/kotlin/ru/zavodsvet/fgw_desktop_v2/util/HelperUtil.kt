package ru.zavodsvet.fgw_desktop_v2.util

/**
 * Помощник.
 */
class HelperUtil {
    companion object {
        const val amountOfSymbolForSeparator = 3
    }

    /**
     * Получить разделитель тысяч. (123456789 => 123`456`789)
     *
     * @param totalSum Сумма.
     */
    fun getThousandSeparator(totalSum: String): String {
        var total = ""
        // Кол-во символов в строке.
        val count = totalSum.count()
        // Получаем символ(число) с которого начинается апостроф.
        var right = count % amountOfSymbolForSeparator
        // Кол-во апострофов.
        var amountOfApostrophes = count.div(amountOfSymbolForSeparator)

        // Если символ(число) с которого начинается апострофов равно 0
        if (right == 0) {
            // то уменьшаем кол-во апострофов.
            amountOfApostrophes--
            // Присваиваем символ(число) с которого начинается апостроф = кол-ву символов для разделения.
            right = amountOfSymbolForSeparator
        }
        // Начальное кол-во апострофов.
        var defaultAmountOfApostrophes = 0
        // Начинаем с первого символа.
        var left = 0
        // Пока начальное кол-во апострофов меньше кол-во всего апострофов.
        while (defaultAmountOfApostrophes < amountOfApostrophes) {
            // Происходить разделение числа апострофом.
            total += "${totalSum.substring(left, right)}`"
            // После того как первый апостроф разделил число, присваиваем число с которого надо начать отсчитывать 3 символа.
            left = right
            // Формируем строку с итоговым значением.
            right += amountOfSymbolForSeparator
            // Увеличиваем начальное кол-во апострофов на 1.
            defaultAmountOfApostrophes++
        }
        // Сформировываем конечную итоговую строку с разделителями.
        total += totalSum.substring(left, right)

        return total
    }
}