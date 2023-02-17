package ru.zavodsvet.fgw_desktop_v2.controller.menuBar.handbook.vidOper

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.MouseButton
import ru.zavodsvet.fgw_desktop_v2.dao.menuBar.handbook.vidOper.VidOperationDaoImpl
import ru.zavodsvet.fgw_desktop_v2.model.menuBar.handbook.vidOper.VidOperationModel
import ru.zavodsvet.fgw_desktop_v2.util.HelperWithSceneUtil

/**
 * Контроллер окна: "Вид операции".
 */
class VidOperationViewController {
    /**
     * Таблица: "Вид операции".
     */
    @FXML
    private lateinit var vidOperTable: TableView<VidOperationModel>

    /**
     * Реализация объекта доступа к данным: "Вид операции".
     */
    private var vidOperDaoImpl = VidOperationDaoImpl()

    /**
     * Список видов операции.
     */
    private var vidOperList: ObservableList<VidOperationModel> = FXCollections.observableArrayList()

    /**
     * Помощник со сценой.
     */
    private var helperWithScene = HelperWithSceneUtil()

    /**
     * Заполнить таблицу: "Вид операции".
     */
    fun fillTableVidOper() {
        vidOperTable.items.removeAll()

        vidOperList = vidOperDaoImpl.getAllVidOper()
        vidOperTable.items = vidOperList
        vidOperTable.selectionModel.selectFirst()
    }

    /**
     * Отправить выбранный элемент: "Вид операции".
     *
     * @param callback Возвращает сущность.
     */
    fun sendSelectedItemVidOper(callback: (VidOperationModel) -> Unit) {
        fillTableVidOper()

        onDoubleClickMouse(vidOperTable, callback)
        onClickEnterKey(vidOperTable, callback)
    }

    /**
     * При нажатии клавиши ввода.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onClickEnterKey(tableView: TableView<VidOperationModel>, callback: (VidOperationModel) -> Unit) {
        tableView.setOnKeyPressed { ev ->
            if (ev.code == KeyCode.ENTER) {
                setSelectedItemVidOper(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * При двойном щелчке мыши.
     *
     * @param callback Возвращает сущность.
     * @param tableView Таблица сущности.
     */
    private fun onDoubleClickMouse(tableView: TableView<VidOperationModel>, callback: (VidOperationModel) -> Unit) {
        tableView.setOnMouseClicked { ev ->
            if (ev.button.equals(MouseButton.PRIMARY) && ev.clickCount == 2) {
                setSelectedItemVidOper(callback)
                helperWithScene.closeStage(ev)
            }
        }
    }

    /**
     * Установить выбранный элемент: "Вид операции".
     *
     * @param callback Возвращает сущность.
     */
    private fun setSelectedItemVidOper(callback: (VidOperationModel) -> Unit) {
        if (getSelectedItemVidOper() != null) {
            vidOperList.filter { it.kod == getSelectedItemVidOper().kod }.forEach { callback.invoke(it) }
        }
    }

    /**
     * Получить выбранный элемент: "Вида операции".
     */
    private fun getSelectedItemVidOper() = vidOperTable.selectionModel.selectedItem
}