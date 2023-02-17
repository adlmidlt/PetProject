package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.operation.testReport.cardTestReport.cardSpTestReport

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.Spinner
import javafx.scene.layout.AnchorPane
import javafx.stage.Stage
import org.apache.logging.log4j.LogManager
import ru.zavodsvet.fgw_desktop_v2.common.WRONG_FORMAT_DATA
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionDateOfPeriodViewController
import ru.zavodsvet.fgw_desktop_v2.controller.selectionWindow.SelectionKDNameViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.operation.testReport.cardTestReport.cardSpTestReport.CardSpTestReportDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.operation.testReport.cardTestReport.cardSpTestReport.SpecificationTestReportModel
import ru.zavodsvet.fgw_desktop_v2.util.EventAlertUtil
import ru.zavodsvet.fgw_desktop_v2.util.ExceptionHandlerUtil
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Контроллер окна: "Спецификации карточки протокола испытаний".
 */
class CardSpTestReportViewController : Initializable {

    /**
     * Журнал логирования.
     */
    private val logger = LogManager.getLogger(CardSpTestReportViewController::class.java)

    /**
     * Сцена окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameView: AnchorPane

    /**
     * Контроллер окна: "Выбор конструкторского наименования".
     */
    @FXML
    private lateinit var selectionKDNameViewController: SelectionKDNameViewController

    /**
     * Счетчик месяцев продления срока годности.
     */
    @FXML
    private lateinit var countMonthsSpinner: Spinner<Int>

    /**
     * Окно периода даты изготовления.
     */
    @FXML
    private lateinit var selectionDateOfPeriodView: AnchorPane

    /**
     * Контроллер окна периода даты изготовления.
     */
    @FXML
    private lateinit var selectionDateOfPeriodViewController: SelectionDateOfPeriodViewController

    /**
     * Кнопка сохранить спецификацию карточки протокола испытаний.
     */
    @FXML
    private lateinit var saveSpCardTestReportBtn: Button

    /**
     * Отмена создания спецификации карточки протокола испытаний.
     */
    @FXML
    private lateinit var cancelSpOperTestReportBtn: Button

    /**
     * Реализация объекта доступа к данным "Спецификации карточки протокола испытаний".
     */
    private var spCardSpTestReportDao = CardSpTestReportDaoImpl()

    /**
     * Спецификация протокола испытания.
     */
    private var spTestReport = SpecificationTestReportModel()

    /**
     * Утилита для обработки ошибок.
     */
    private var exceptionHandler = ExceptionHandlerUtil()

    /**
     * Матюгатор.
     */
    private var eventAlert = EventAlertUtil()

    /**
     * Помощник со сценой.
     */
    private var helperScene = HelperWithSceneUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        selectionKDNameView.prefWidth = 450.0
    }

    /**
     * Заполнить поля карточки спецификации протокола испытаний.
     *
     * @param idSpTestReport ИД спецификации.
     */
    private fun fillCardSpTestReport(idSpTestReport: Int) {
        spTestReport = spCardSpTestReportDao.getCardSpTestReport(idSpTestReport)

        selectionKDNameViewController.openWindowSelectKDName()
        selectionKDNameViewController.setKdName(spTestReport.name)
        countMonthsSpinner.valueFactory.value = spTestReport.extend
        selectionDateOfPeriodViewController.setStartDate(spTestReport.dtn)
        selectionDateOfPeriodViewController.setEndDate(spTestReport.dtk)
    }

    /**
     * Добавить карточку спецификации протокола испытаний.
     */
    fun addCardSpTestReport(idDoc: Int, callback: (Boolean) -> Unit) {
        cleanedCardSpTestReport()
        spTestReport.id = idDoc
        selectionKDNameViewController.openWindowSelectKDName()

        saveSpCardTestReportBtn.setOnAction { ev ->
            try {
                spTestReport.prodid = selectionKDNameViewController.getKDNameId()!!
                spTestReport.extend = countMonthsSpinner.value
                spTestReport.dtn = selectionDateOfPeriodViewController.getStartDate()
                spTestReport.dtk = selectionDateOfPeriodViewController.getEndDate()

                spCardSpTestReportDao.addSpTestReport(spTestReport)
                helperScene.closeStage(ev)
                callback(true)
            } catch (eNFE: NullPointerException) {
                logger.error(eNFE.message)
                exceptionHandler.printStackTraceElem(eNFE, "addCardSpTestReport", "${eNFE.message}")
                eventAlert.alertError(WRONG_FORMAT_DATA)
            }
        }
        cancelSpOperTestReportBtn.setOnAction { (cancelSpOperTestReportBtn.scene.window as Stage).close() }
    }

    /**
     * Обновление карточки спецификации протокола испытаний.
     *
     * @param idSpTestReport ИД спецификации.
     */
    fun updCardSpTestReport(idSpTestReport: Int, callback: (Boolean) -> Unit) {
        fillCardSpTestReport(idSpTestReport)

        saveSpCardTestReportBtn.setOnAction { ev ->
            try {
                spCardSpTestReportDao.updCardSpTestReport(
                    spdocid = idSpTestReport,
                    prodid = selectionKDNameViewController.getKDNameId()!!,
                    extend = countMonthsSpinner.value,
                    dtn = selectionDateOfPeriodViewController.getStartDate(),
                    dtk = selectionDateOfPeriodViewController.getEndDate(),
                )
                helperScene.closeStage(ev)
                callback(true)
            } catch (eNFE: NullPointerException) {
                logger.error(eNFE.message)
                exceptionHandler.printStackTraceElem(eNFE, "addCardTestReport", "${eNFE.message}")
                eventAlert.alertError(WRONG_FORMAT_DATA)
            }
        }
        cancelSpOperTestReportBtn.setOnAction { (cancelSpOperTestReportBtn.scene.window as Stage).close() }
    }

    /**
     * Очистить поля карточки спецификации протокола испытаний.
     */
    private fun cleanedCardSpTestReport() {
        selectionKDNameViewController.setKdName("")
        countMonthsSpinner.valueFactory.value = 3
        selectionDateOfPeriodViewController.setStartDate(LocalDate.now().toString())
    }
}