package app.viewmanagers;

import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
    private Label indexLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button doneButton;

    @FXML
    private Button undoneButton;

    @FXML
    private Tooltip taskNameTooltip;

    private RootViewManager rootViewManager;

    List<String> colors;
    private TaskListViewManager taskListViewManager;
    final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;

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
        setDeleteButtonEventHandler();
        setDoneButtonEventHandler();
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

        taskNameTooltip.setText(task.getTaskName());
        doneButton.setVisible(task.isDone());
        undoneButton.setVisible(!task.isDone());
        overdueLabel.setVisible(task.isOverdue() && !task.isDone());
    }

    private void setPriorityLevel(TodoItem task) {
        priorityLevelLabel.setText(task.getPriority().substring(3).toUpperCase());
        setBackgroundColor(task);
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

    private void setTaskName(TodoItem task){
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    private void setDeleteButtonEventHandler() {
        deleteButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("delete " + getTaskIndex()));
    }

    private void setUpdateButtonEventHandler(TodoItem task) {
        updateButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("update " + getTaskIndex() + " " + getTaskInfo(task)));
    }

    private void setDoneButtonEventHandler() {
        doneButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("undone " + getTaskIndex()));
        undoneButton.setOnAction((event) -> rootViewManager.setAndFocusInputField("done " + getTaskIndex()));
    }

    private int getTaskIndex() { return getIndex() + 1; }

    private String getTaskInfo(TodoItem task) {
        String info = "";

        info = info + task.getTaskName();

        if (task.getStartDate() != null) {
           info = info + " start " + task.getStartDateString().toLowerCase();
        }

        if (task.getEndDate() != null) {
            info = info + " end " + task.getEndDateString().toLowerCase();
        }

        if (task.getPriority() != null) {
            info = info + " priority " + task.getPriority().substring(3).toLowerCase();
        }

        return info;
    }

    private void setBackgroundColor(TodoItem task) {
        String alphaValue;
        int differenceInDays = 0;

        if (task.getEndDate() != null) {
            differenceInDays = (int) (((task.getEndDate().getTime() - new Date().getTime())) / DAY_IN_MILLIS);
        }

        // Overdue tasks should be fully saturated
        if (differenceInDays < 0) {
            alphaValue = "1";
        } else {
            float factor = calculateFactor(differenceInDays);
            // Compare by priority level
            switch(task.getPriority().substring(3).toLowerCase()) {
                case "high":
                    alphaValue = String.valueOf(1 * factor);
                    break;
                case "medium":
                    alphaValue = String.valueOf(0.85 * factor);
                    break;
                case "low":
                    alphaValue = String.valueOf(0.7 * factor);
                    break;
                default:
                    alphaValue = "1";
            }

        }

        // Done tasks are low priority
        if (task.isDone()) {
            alphaValue = "0.45";
        }

        if (rootViewManager.getMainApp().getCommandController().areRandomColorsEnabled()) {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getRandomColor() + "," + alphaValue + ");");
        } else {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getCurrentColor() + "," + alphaValue + ");");
        }
    }

    // Return a value between 0.5 and 1
    private float calculateFactor(int differenceInDays) {
        if (differenceInDays == 0) {
            return 1;
        }

        float normalized;
        if (differenceInDays > 30) {
            normalized = (float) 0.45;
        } else {
            normalized = (float) (((30.0 - differenceInDays) / 30.0) + 0.45);
        }

        if (normalized > 1) {
            normalized = 1;
        }

        return normalized;
    }

    @FXML
    private void initialize() { }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void setTaskListViewManager(TaskListViewManager taskListViewManager) {
        this.taskListViewManager = taskListViewManager;
    }
}
