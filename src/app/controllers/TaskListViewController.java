package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

/**
 * Created by jin on 24/9/14.
 */
public class TaskListViewController {

    @FXML
    public ListView<TodoItem> taskListView;

    private Main main;

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListCell -> new TaskListCellController(main));
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