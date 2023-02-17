package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.TextField
import javafx.scene.control.TreeItem
import javafx.scene.layout.AnchorPane
import ru.zavodsvet.fgw_desktop_v2.common.COLOR_TEXT_BLUE
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.tabs.BalanceByLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.locStore.cardLocStore.tabs.SchemaLocStoreViewController
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.locStore.cardLocStore.CardLocStoreDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.locStore.LocStoreModel
import ru.zavodsvet.fgw_desktop_v2.util.TextFieldValidationUtil
import java.net.URL
import java.util.*

class CardLocStoreViewController : Initializable {
    /**
     * Поле Наименование.
     */
    @FXML
    private lateinit var nameLocStoreTF: TextField

    /**
     * Поле Комментарий.
     */
    @FXML
    private lateinit var commLocStoreTF: TextField

    /**
     * Поле Площадь.
     */
    @FXML
    private lateinit var areaLocStoreTF: TextField

    /**
     * Возможный процент использования.
     */
    @FXML
    private lateinit var limitLocStoreTF: TextField

    /**
     * Чек бокс Крытая площадка.
     */
    @FXML
    private lateinit var choiceCoverLocStoreCheckBox: CheckBox

    /**
     * Чек бокс Наличие ЖД путей.
     */
    @FXML
    private lateinit var choiceRZDLocStoreCheckBox: CheckBox

    /**
     * Кнопка Сохранить.
     */
    @FXML
    private lateinit var saveLocStoreBtn: Button

    /**
     * Вкладка Остатки по участку хранения.
     */
    @FXML
    private lateinit var balanceByLocStoreView: AnchorPane

    /**
     * Контроллер таба "Остатки по участку хранения".
     */
    @FXML
    private lateinit var balanceByLocStoreViewController: BalanceByLocStoreViewController

    /**
     * Вкладка схема участка хранения.
     */
    @FXML
    private lateinit var schemaLocStoreView: AnchorPane

    /**
     * Контроллер отображения схемы участка хранения.
     */
    @FXML
    private lateinit var schemaLocStoreViewController: SchemaLocStoreViewController

    /**
     * Участок хранения.
     */
    private var cardLocStore: TreeItem<LocStoreModel> = TreeItem<LocStoreModel>()

    /**
     * Реализация объекта доступа к данным: "Карточки участка хранения".
     */
    private var cardLocStoreDaoImpl = CardLocStoreDaoImpl()

    /**
     * Класс для проверки текстовых полей на валидность.
     */
    private var textFieldValidation = TextFieldValidationUtil()

    // ИНИЦИАЛИЗАТОР.
    override fun initialize(location: URL?, resources: ResourceBundle?) {
        nameLocStoreTF.textProperty().addListener { _, _, newValue ->
            if (!nameLocStoreTF.text.isNullOrEmpty() && newValue != cardLocStore.value.name) {
                nameLocStoreTF.style = COLOR_TEXT_BLUE
            } else {
                nameLocStoreTF.style = null
            }
        }

        commLocStoreTF.textProperty().addListener { _, _, newValue ->
            if (!commLocStoreTF.text.isNullOrEmpty() && newValue != cardLocStore.value.comm) {
                commLocStoreTF.style = COLOR_TEXT_BLUE
            } else {
                commLocStoreTF.style = null
            }
        }

        areaLocStoreTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputDoubleNumber(areaLocStoreTF, newValue)
            if (!areaLocStoreTF.text.isNullOrEmpty() && newValue != cardLocStore.value.area.toString()) {
                areaLocStoreTF.style = COLOR_TEXT_BLUE
            }
        }

        limitLocStoreTF.textProperty().addListener { _, _, newValue ->
            textFieldValidation.correctInputDoubleNumber(limitLocStoreTF, newValue)
            if (!limitLocStoreTF.text.isNullOrEmpty() && newValue != cardLocStore.value.limit.toString()) {
                limitLocStoreTF.style = COLOR_TEXT_BLUE
            }
        }
    }

    /**
     * Заполнить Карточку участка хранения.
     */
    fun fillCardLocStoreByIdLocStore(locid: Int) {
        cardLocStore = cardLocStoreDaoImpl.getLocStorePallets(locid)
        nameLocStoreTF.text = cardLocStore.value.name
        commLocStoreTF.text = cardLocStore.value.comm
        areaLocStoreTF.text = cardLocStore.value.area.toString()
        limitLocStoreTF.text = cardLocStore.value.limit.toString()
        if (cardLocStore.value.cover == 1) choiceCoverLocStoreCheckBox.isSelected = true
        if (cardLocStore.value.rzd == 1) choiceRZDLocStoreCheckBox.isSelected = true

        balanceByLocStoreViewController.fillTableBalanceByLocStore(locid)
        schemaLocStoreViewController.showSchemaLocStore(locid)

        saveLocStoreBtn.setOnAction {
            editFieldInTopCardLocStore(locid)
            cardLocStore.value.name = nameLocStoreTF.text
            cardLocStore.value.comm = commLocStoreTF.text
            cardLocStore.value.area = areaLocStoreTF.text.toDouble()
            cardLocStore.value.limit = limitLocStoreTF.text.toDouble()

            nameLocStoreTF.style = null
            commLocStoreTF.style = null
            areaLocStoreTF.style = null
            limitLocStoreTF.style = null
        }
    }

    /**
     * Редактирование полей в шапке карточки участка хранения.
     *
     * @param locid ИД участка хранения.
     */
    private fun editFieldInTopCardLocStore(locid: Int) {
        cardLocStoreDaoImpl.editLocStore(locid, "name", nameLocStoreTF.text)
        cardLocStoreDaoImpl.editLocStore(locid, "comm", commLocStoreTF.text)
        cardLocStoreDaoImpl.editLocStore(locid, "area", areaLocStoreTF.text)
        cardLocStoreDaoImpl.editLocStore(locid, "limit", limitLocStoreTF.text)
        cardLocStoreDaoImpl.editLocStore(locid, "cover", isChoice(choiceCoverLocStoreCheckBox))
        cardLocStoreDaoImpl.editLocStore(locid, "rzd", isChoice(choiceRZDLocStoreCheckBox))
    }

    /**
     * Проверить, является ли выбранным чек бокс.
     *
     * @param choice Крыжик чек бокса.
     */
    private fun isChoice(choice: CheckBox) = if (choice.isSelected) 1.toString() else 0.toString()
}