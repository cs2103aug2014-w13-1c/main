package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by jin on 28/9/14.
 */
public class TaskListCellController extends ListCell<TodoItem> {

    @FXML
    private GridPane cellGrid;

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label topDateLabel;

    @FXML
    private Label bottomDateLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    private Main main;

    ArrayList<String> colors = new ArrayList<>();

    public TaskListCellController() {
    }

    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        setGraphic(cellGrid);
        if (!empty) {
            populateContent(task);
            setUpdateButtonEventHandler(task);
            setDeleteButtonEventHandler(task);
        }
    }

    private void populateContent(TodoItem task) {
        setTaskName(task);
        setDates(task);
    }

    private void setDates(TodoItem task) {
        if (task.getTodoItemType().equalsIgnoreCase("Event")) {
            topDateLabel.setText("START " + task.getStartDateString());
            bottomDateLabel.setText("END " + task.getEndDateString());
            topDateLabel.setVisible(true);
            bottomDateLabel.setVisible(true);
        } else if (task.getTodoItemType().equalsIgnoreCase("Deadline")) {
            topDateLabel.setText("DUE " + task.getEndDateString());
        }
    }

    private void setTaskName(TodoItem task) {
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    private void setDeleteButtonEventHandler(TodoItem task) {
        deleteButton.setOnAction((event) -> main.setAndFocusInputField("delete " + getTaskIndex(task)));
    }

    private void setUpdateButtonEventHandler(TodoItem task) {
        updateButton.setOnAction((event) -> main.setAndFocusInputField("update " + getTaskIndex(task) + " "));
    }

    private void initColors() {
        colors.add("#556270");
        colors.add("#4ECDC4");
        colors.add("#C7F464");
        colors.add("#FF6B6B");
        colors.add("#C44D58");
    }

    public String getRandomColor() {
        return colors.get(new Random().nextInt(colors.size()));
    }

    private int getTaskIndex(TodoItem task) {
        return new Scanner(task.getTaskName()).useDelimiter("\\D+").nextInt();
    }

    @FXML
    private void initialize() {
        initColors();
        cellGrid.setStyle("-fx-background-color: " + getRandomColor() + ";");
    }

    public void setMainApp(Main main) {
        this.main = main;
    }
}
