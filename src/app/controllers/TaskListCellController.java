package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.util.*;

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

    List<String> colors;


    public TaskListCellController() {
    }

    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        setGraphic(cellGrid);
        if (empty) {
            clearContent();
        } else {
            populateContent(task);
            setUpdateButtonEventHandler(task);
            setDeleteButtonEventHandler(task);
        }
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
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
            topDateLabel.setVisible(true);
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
        colors = Arrays.asList(
                "#d01716", // red 700
                "#c2185b", // pink 700
                "#7b1fa2", // purple 700
                "#512da8", // deep purple 700
                "#393f9f", // indigo 700
                "#455ede", // blue 700
                "#0288d1", // light blue 700
                "#0097a7", // cyan 700
                "#00796b", // teal 700
                "#0a7e07", // green 700
                "#558b2f", // light green 800
                "#827717", // lime 900
                "#e65100", // orange 900
                "#e54a19", // deep orange 700
                "#795548"); // brown 500
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
