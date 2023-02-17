package ru.zavodsvet.fgw_desktop_v2.util

import javafx.scene.control.TableView
import javafx.stage.FileChooser
import org.apache.logging.log4j.LogManager
import org.apache.poi.hssf.usermodel.*
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import ru.zavodsvet.fgw_desktop_v2.common.FILE_SUCCESS_SAVE
import ru.zavodsvet.fgw_desktop_v2.common.NOT_FOUND_FILE_OR_OPEN_NOW
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ExportExcelUtil<S> {
    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(ExportExcelUtil::class.java)

    /**
     * Рабочая книга.
     */
    private val workbook: HSSFWorkbook = HSSFWorkbook()

    /**
     * Создать новый лист в excel.
     */
    private var hssfSheet: HSSFSheet = workbook.createSheet()

    /**
     * Создать новую строку в Excel.
     */
    private var hssfRow: HSSFRow = hssfSheet.createRow(0)

    /**
     * Создать стиль шрифта.
     */
    private lateinit var hssfFont: HSSFFont

    /**
     * Создать стиль ячейки.
     */
    private var hssfCellStyle: HSSFCellStyle = workbook.createCellStyle()

    /**
     * Форматер даты время.
     */
    private var formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")

    /**
     * Форматирование даты.
     */
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    /**
     * Выбор сохранения файла.
     */
    private var fileChooser = FileChooser()

    /**
     * Матюгатор.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Записать в эксель.
     */
    fun excelWriter(table: TableView<S>, nameExportExcelReport: String) {
        fileChooser.title = "Сохранить файл ..."
        fileChooser.initialFileName = "Отчет по $nameExportExcelReport ${LocalDateTime.now().format(formatDateTime)}"
        fileChooser.extensionFilters.add(FileChooser.ExtensionFilter("Эксель формат", "*.xls"))
        // выгрузка файла.
        try {
            val file = fileChooser.showSaveDialog(null)
            if (file != null) {
                fillHeaderOfTable(table)
                fillTableWithData(table)
                writeWorkbook(file.absolutePath)
                eventAlert.alertInfo(FILE_SUCCESS_SAVE)
            }
        } catch (eIOE: IOException) {
            logger.error(eIOE.message)
        }
    }

    /**
     * Заполнить шапку таблицы.
     *
     * @param table Таблица.
     */
    private fun fillHeaderOfTable(table: TableView<S>) {
        // Пробегаемся по колонкам и записываем названия в шапку эксель таблицы.
        for (numColumn in 0 until table.columns.size) {
            hssfRow.createCell(numColumn).setCellValue(table.columns[numColumn].text)

            // Навешиваем стили ячеек.
            hssfRow.getCell(numColumn).setCellStyle(createCellStyleForTableHead())
        }
    }

    /**
     * Заполнить таблицу данными.
     *
     * @param table Таблица.
     */
    private fun fillTableWithData(table: TableView<S>) {
        // Пробегаемся по таблице с данными и записываем их в таблицу эксель.
        for (indRow in 0 until table.items.size) {
            // Создавать строку начиная с 1, т.к. на нулевой строке находится шапка эксель таблицы.
            hssfRow = hssfSheet.createRow(indRow + 1)

            for (numColumn in 0 until table.columns.size) {
                isCellOnType(table, numColumn, indRow)

                // Регулирует ширину столбца в соответствии с содержимым.
                hssfSheet.autoSizeColumn(numColumn)
            }
        }
    }

    /**
     * Фильтр для итогов. Но он не работает. Из-за него формируются криво строки в эксель.
     */
    fun fillFooterTotal(table: TableView<S>) {
        hssfRow = hssfSheet.createRow(table.items.size + 1)

        val createCell0 = hssfRow.createCell(0)
        createCell0.setCellValue("ИТОГО:")
        createCell0.setCellStyle(createCellStyleForTableHead())

        val createCell8 = hssfRow.createCell(8)
        createCell8.cellFormula = "SUM(I2:I${table.items.size})"
        createCell8.setCellStyle(createCellStyleForTableHead())

        val createCell9 = hssfRow.createCell(9)
        createCell9.cellFormula = "SUM(J2:J${table.items.size})"
        createCell9.setCellStyle(createCellStyleForTableHead())
    }

    /**
     * Проверка ячеек на тип данных, перед записью в таблицу.
     *
     * @param table Таблица.
     * @param numColumn Номер колонки в таблице.
     * @param indRow индекс строки в таблице.
     */
    private fun isCellOnType(table: TableView<S>, numColumn: Int, indRow: Int) {
        // Возвращает фактическое значение для ячейки с заданным индексом строки.
        var cellData = table.columns[numColumn].getCellData(indRow)
        try {
            // Создаёт новые ячейки в строке и возвращает её.
            val createCell = hssfRow.createCell(numColumn)

            // Смотрим пустая ли ячейка.
            if (cellData != null) {
                when (cellData) {
                    is LocalDateTime -> {
                        cellData = formatDateWithLocalDateTime(cellData)
                        createCell.setCellValue(cellData)
                    }
                    is Double -> createCell.setCellValue(cellData)
                    is Boolean -> createCell.setCellValue(cellData)
                    is Number -> createCell.setCellValue(cellData.toDouble())
                    is String -> createCell.setCellValue(cellData.toString())
                }
//                createCell.setCellValue(cellData.toString())
            } else {
                createCell.setCellValue("")
            }
        } catch (eIllegalArgument: IllegalArgumentException) {
            println("columnIndex < 0 или больше 255, максимальное количество столбцов, поддерживаемое двоичным форматом Excel (.xls).")
        }
    }

    /**
     * Записать данные в эксель.
     *
     * @param file Путь до файла.
     */
    @Throws(IOException::class)
    private fun writeWorkbook(file: String) {
        try {
            val fileOut = FileOutputStream(file)
            workbook.write(fileOut)
            fileOut.close()
        } catch (eFileNotFount: FileNotFoundException) {
            eventAlert.alertWarning(NOT_FOUND_FILE_OR_OPEN_NOW)
        }
    }

    /*---------------------------СТИЛИ ДЛЯ ТАБЛИЦЫ-------------------------*/
    /**
     * Создать стиль ячейки для данных таблицы.
     */
    private fun createCellStyleForTableData(): HSSFCellStyle {
        hssfCellStyle = workbook.createCellStyle()

        hssfCellStyle.borderTop = BorderStyle.THIN
        hssfCellStyle.borderLeft = BorderStyle.THIN
        hssfCellStyle.borderRight = BorderStyle.THIN
        hssfCellStyle.borderBottom = BorderStyle.THIN
        hssfCellStyle.alignment = HorizontalAlignment.CENTER

        return hssfCellStyle
    }

    /**
     * Создать стиль ячейки для шапки таблицы.
     */
    private fun createCellStyleForTableHead(): HSSFCellStyle {
        hssfCellStyle = workbook.createCellStyle()
        hssfFont = createFontStyleForTableHead()
        hssfCellStyle.setFont(hssfFont)
        hssfCellStyle.borderTop = BorderStyle.THIN
        hssfCellStyle.borderLeft = BorderStyle.THIN
        hssfCellStyle.borderRight = BorderStyle.THIN
        hssfCellStyle.borderBottom = BorderStyle.THIN
        hssfCellStyle.alignment = HorizontalAlignment.CENTER

        return hssfCellStyle
    }

    /**
     * Создать стиль шрифта для шапки таблицы.
     */
    private fun createFontStyleForTableHead(): HSSFFont {
        hssfFont = workbook.createFont()
        hssfFont.bold = true

        return hssfFont
    }

    /**
     * Форматирование даты с местной датой и временем.
     *
     * @param dateTime Дата и время.
     */
    private fun formatDateWithLocalDateTime(dateTime: LocalDateTime): String =
        LocalDateTime.of(dateTime.year, dateTime.month.value, dateTime.dayOfMonth, dateTime.hour, dateTime.minute)
            .format(formatter)

}