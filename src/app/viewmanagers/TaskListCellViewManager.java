package app.viewmanagers;

import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;

import java.util.*;

/**
 * Created by jin on 28/9/14.
 */
public class TaskListCellViewManager extends ListCell<TodoItem> {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label topDateLabel;

    @FXML
    private Label bottomDateLabel;

    @FXML
    private Label priorityLevelLabel;

    @FXML
    private Label overdueLabel;

    @FXML
    private Label tickLabel;

    @FXML
    private Label indexLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button doneButton;

    private RootViewManager rootViewManager;

    List<String> colors;

    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        setGraphic(anchorPane);
        if (empty) {
            clearContent();
        } else {
            populateContent(task);
            setButtonEventHandlers(task);
        }
    }

    private void setButtonEventHandlers(TodoItem task) {
        setUpdateButtonEventHandler(task);
        setDeleteButtonEventHandler(task);
        setDoneButtonEventHandler(task);
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void populateContent(TodoItem task) {
        setIndex();
        setTaskName(task);
        setPriorityLevel(task);
        setDates(task);

        tickLabel.setVisible(task.isDone());
        overdueLabel.setVisible(task.isOverdue() && !task.isDone());
    }

    private void setPriorityLevel(TodoItem task) {
        priorityLevelLabel.setText("PRIORITY: " + task.getPriority().substring(3).toUpperCase());
        setBackgroundColor(task.getPriority());
    }

    private void setDates(TodoItem task) {
        switch (task.getTodoItemType().toLowerCase()) {
            case "event":
                topDateLabel.setText("START " + task.getStartDateString());
                bottomDateLabel.setText("END " + task.getEndDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(true);
                break;
            case "deadline":
                topDateLabel.setText("DUE " + task.getEndDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(false);
                break;
            case "endless":
                topDateLabel.setText("START " + task.getStartDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(false);
                break;
            default:
                topDateLabel.setVisible(false);
                bottomDateLabel.setVisible(false);
                break;
        }
    }

    private void setIndex() {
        indexLabel.setText(String.valueOf(getTaskIndex()));
    }

    private void setTaskName(TodoItem task) {
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    private void setDeleteButtonEventHandler(TodoItem task) {
        deleteButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("delete " + getTaskIndex()));
    }

    private void setUpdateButtonEventHandler(TodoItem task) {
        updateButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("update " + getTaskIndex() + " "));
    }

    private void setDoneButtonEventHandler(TodoItem task) {
        doneButton.setOnAction((event) -> rootViewManager.setAndFocusInputField(getDoneAction(task) + getTaskIndex()));
    }

    public String getRandomColor() {
        return colors.get(new Random().nextInt(colors.size()));
    }

    private int getTaskIndex() { return getIndex() + 1; }

    private String getDoneAction(TodoItem task) {
        return (task.isDone()) ? "undone " : "done ";
    }

    private void setBackgroundColor(String priority) {
        String alphaValue;
        switch(priority.substring(3)) {
            case "High":
                alphaValue = "1";
                break;
            case "Medium":
                alphaValue = "0.75";
                break;
            case "Low":
                alphaValue = "0.35";
                break;
            default:
                alphaValue = "0.75";
        }

        anchorPane.setStyle("-fx-background-color: rgba(" + getRandomColor() + "," + alphaValue + ");");
    }

    private void initColors() {
        colors = Arrays.asList(
                "208, 23, 22", // red 700
                "194, 24, 91", // pink 700
                "123, 31, 162", // purple 700
                "81, 45, 168", // deep purple 700
                "57, 63, 159", // indigo 700
                "69, 94, 222", // blue 700
                "2, 136, 209", // light blue 700
                "0, 151, 167", // cyan 700
                "0, 121, 107", // teal 700
                "10, 126, 7", // green 700
                "85, 139, 47", // light green 800
                "130, 119, 23", // lime 900
                "230, 81, 0", // orange 900
                "229, 74, 25", // deep orange 700
                "121, 85, 72"); // brown 500
    }

    @FXML
    private void initialize() {
        initColors();
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
