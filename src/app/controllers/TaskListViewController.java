package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.Date;

/**
 * Created by jin on 24/9/14.
 */
public class TaskListViewController {

    @FXML
    public ListView<TodoItem> taskListView;

    private ObservableList<TodoItem> taskData;

    private Main main;

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListView -> new TaskListCellController());
    }

    public void updateView(ObservableList<TodoItem> taskData) {
        taskListView.setItems(taskData);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
    }
}