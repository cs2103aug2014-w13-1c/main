package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by jin on 24/9/14.
 */
public class TaskListCellController extends ListCell<TodoItem> {

    private GridPane grid = new GridPane();
    private Label taskNameLabel = new Label();

    private Label topDateLabel = new Label();
    private Label bottomDateLabel = new Label();

    private Button updateButton = new Button("update");
    private Button deleteButton = new Button("delete");

    private Calendar cal = Calendar.getInstance();

    private Main main;

    ArrayList<String> colors = new ArrayList<String>();

    public TaskListCellController(Main main) {
        this.getStylesheets().add("app/stylesheets/taskListCell.css");
        this.getStyleClass().add("cell");

        initColors();
        this.setStyle("-fx-background-color: " + getRandomColor() + ";");

        configureGrid();
        configureTaskName();
        configureDateLabel(topDateLabel);
        configureDateLabel(bottomDateLabel);
        configureUpdateButton();
        configureDeleteButton();
        addControlsToGrid();

        this.main = main;
    }

    private void configureDeleteButton() {
//        Image image = new Image(getClass().getResourceAsStream("app/resources/cross.png"));
//        deleteButton.setGraphic(new ImageView(image));
    }

    private void configureUpdateButton() {
//        Image image = new Image(getClass().getResourceAsStream("app/resources/compose-3.png"));
//        updateButton.setGraphic(new ImageView(image));
    }

    private void initColors() {
        colors.add("#0D4EB2");
        colors.add("#67BF55");
        colors.add("#F78F37");
        colors.add("#F15B5A");
        colors.add("#B76BDB");
    }

    private void configureGrid() {
        grid.setHgap(27);
        grid.setVgap(5);
        grid.setPadding(new Insets(5, 7, 5, 7));
    }

    private void configureTaskName() {
        taskNameLabel.getStylesheets().add(this.getStylesheets().get(0));
        taskNameLabel.getStyleClass().add("task-name-label");
        taskNameLabel.setMaxWidth(450);
        taskNameLabel.setMaxHeight(70);
        taskNameLabel.setWrapText(true);
        taskNameLabel.setTextFill(Color.WHITE);
    }

    private void configureDateLabel(Label label) {
        label.setVisible(false);
        label.getStylesheets().add(this.getStylesheets().get(0));
        label.getStyleClass().add("date-label");
        label.setMaxWidth(350);
        label.setMaxHeight(30);
        label.setWrapText(true);
        label.setTextFill(Color.WHITE);
    }

    private void addControlsToGrid() {
        grid.add(taskNameLabel, 0, 0);
        grid.add(topDateLabel, 0, 1);
        grid.add(bottomDateLabel, 0, 2);
        grid.add(updateButton, 5, 0);
        grid.add(deleteButton, 5, 5);
//        grid.setGridLinesVisible(true);
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void addContent(TodoItem task) {
        setText(null);
        addContentToTaskName(task);
        addContentToDateLabels(task);
        setUpdateButtonEventHandler(task);
        setDeleteButtonEventHandler(task);

        setGraphic(grid);
    }

    private void setDeleteButtonEventHandler(TodoItem task) {
        deleteButton.setOnAction((event) -> {
            main.getInputField().setText("delete ");
        });
    }

    private void setUpdateButtonEventHandler(TodoItem task) {
        updateButton.setOnAction((event) -> {
            main.getInputField().setText("update ");
        });
    }

    private void addContentToDateLabels(TodoItem task) {
        if (task.getTodoItemType().equalsIgnoreCase("Event")) {
            topDateLabel.setText("START " + getDateString(task.getStartDate()).toUpperCase());
            bottomDateLabel.setText("END " + getDateString(task.getEndDate()).toUpperCase());
            topDateLabel.setVisible(true);
            bottomDateLabel.setVisible(true);
        } else if (task.getTodoItemType().equalsIgnoreCase("Deadline")) {
            topDateLabel.setText("DUE " + getDateString(task.getEndDate()).toUpperCase());
            topDateLabel.setVisible(true);
        }
    }

    private void addContentToTaskName(TodoItem task) {
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    private String getDateString(Date date) {
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month = theMonth(cal.get(Calendar.MONTH));
        int year = cal.get(Calendar.YEAR);
        return day + " " + month + " " + year;
    }

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month];
    }

    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        if (empty) {
            clearContent();
        } else {
            addContent(task);
        }
    }

    public String getRandomColor() {
        return colors.get((int) (Math.random() * (colors.size() - 1)));
    }
}
