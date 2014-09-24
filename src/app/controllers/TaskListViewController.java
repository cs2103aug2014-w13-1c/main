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

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();

    private Main main;

    @FXML
    public void initialize() {
        taskData.add(new TodoItem("buy milk", new Date(), new Date()));
        taskData.add(new TodoItem("walk the dog", null, new Date()));
        taskData.add(new TodoItem("finish CS2101 progress report", null, null));
        taskData.add(new TodoItem("run 24km", new Date(), new Date()));
        taskData.add(new TodoItem("have lunch with some person", new Date(), new Date()));
        taskData.add(new TodoItem("This is a very looooooooooooooong task name to test for word wrap", new Date(), new Date()));

        taskListView.setCellFactory(taskListView -> new TaskListCellController());
        taskListView.setItems(taskData);

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;

        // Add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
    }
}