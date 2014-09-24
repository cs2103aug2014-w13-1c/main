package app.controllers;

import app.model.TodoItem;
import javafx.geometry.Insets;
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

    private Calendar cal = Calendar.getInstance();

    ArrayList<String> colors = new ArrayList<String>();

    public TaskListCellController() {
        this.getStylesheets().add("app/stylesheets/taskListCell.css");
        this.getStyleClass().add("cell");

        initColors();
        this.setStyle("-fx-background-color: " + getRandomColor() + ";");

        configureGrid();
        configureTaskName();
        configureDateLabel(topDateLabel);
        configureDateLabel(bottomDateLabel);
        addControlsToGrid();
    }

    private void initColors() {
        colors.add("#0D4EB2");
        colors.add("#67BF55");
        colors.add("#F78F37");
        colors.add("#F15B5A");
        colors.add("#B76BDB");
    }

    private void configureGrid() {
        grid.setHgap(5);
        grid.setVgap(5);
        grid.setPadding(new Insets(5, 7, 5, 7));
    }

    private void configureTaskName() {
        taskNameLabel.getStylesheets().add(this.getStylesheets().get(0));
        taskNameLabel.getStyleClass().add("task-name-label");
        taskNameLabel.setMaxWidth(350);
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
        grid.add(taskNameLabel, 1, 0);
        grid.add(topDateLabel, 1, 1);
        grid.add(bottomDateLabel, 1, 2);

    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void addContent(TodoItem task) {
        setText(null);
        taskNameLabel.setText(task.getTaskName().toUpperCase());

        if (task.getStartDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(task.getStartDate());

            topDateLabel.setText("START " + getDateString(task.getStartDate()));
            bottomDateLabel.setText("END " + getDateString(task.getEndDate()));

            topDateLabel.setVisible(true);
            bottomDateLabel.setVisible(true);

        } else if (task.getEndDate() != null) {
            topDateLabel.setText("DUE " + getDateString(task.getEndDate()));
            topDateLabel.setVisible(true);
        }

        setGraphic(grid);
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
        return monthNames[month].toUpperCase();
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
