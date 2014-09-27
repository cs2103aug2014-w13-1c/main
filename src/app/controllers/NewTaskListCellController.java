package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Created by jin on 28/9/14.
 */
public class NewTaskListCellController extends ListCell<TodoItem> {

    @FXML
    private GridPane cellGrid;

    @FXML
    private Label taskNameLabel;

    private Main main;

    public NewTaskListCellController() {
    }

    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        setGraphic(cellGrid);
        if (task != null) {
            System.out.println(task.getTaskName());
            this.taskNameLabel.setText(task.getTaskName());
        }
    }

    @FXML
    private void initialize() {

    }

    public void setMainApp(Main main) {
        this.main = main;
    }
}
