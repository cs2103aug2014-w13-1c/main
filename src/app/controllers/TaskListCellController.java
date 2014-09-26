package app.controllers;

import app.Main;
import app.model.TodoItem;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TaskListCellController extends ListCell<TodoItem> {

    final private GridPane grid = new GridPane();
    final private Label taskNameLabel = new Label();

    final private Label topDateLabel = new Label();
    final private Label bottomDateLabel = new Label();

    final private Button updateButton = new Button();
    final private Button deleteButton = new Button();

    private Main main;

    ArrayList<String> colors = new ArrayList<>();

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

    private void configureGrid() {
        grid.setHgap(30);
        grid.setVgap(5);
        grid.setPadding(new Insets(5, 7, 5, 7));
//        grid.setGridLinesVisible(true);
    }

    private void configureDeleteButton() {
        Image image = new Image("app/resources/cross.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);


        deleteButton.setGraphic(imageView);
        deleteButton.setStyle("-fx-background-color: transparent;");
    }

    private void configureUpdateButton() {
        Image image = new Image("app/resources/compose-3.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        updateButton.setGraphic(imageView);
        updateButton.setStyle("-fx-background-color: transparent;");
    }

    private void initColors() {
        colors.add("#556270");
        colors.add("#4ECDC4");
        colors.add("#C7F464");
        colors.add("#FF6B6B");
        colors.add("#C44D58");
    }

    private void configureTaskName() {
        taskNameLabel.getStylesheets().add(this.getStylesheets().get(0));
        taskNameLabel.getStyleClass().add("task-name-label");
        taskNameLabel.setMaxWidth(350);
        taskNameLabel.setMaxHeight(50);
        taskNameLabel.setWrapText(true);
        taskNameLabel.setTextFill(Color.WHITE);
    }

    private void configureDateLabel(Label label) {
        label.setVisible(false);
        label.getStylesheets().add(this.getStylesheets().get(0));
        label.getStyleClass().add("date-label");
        label.setMaxWidth(250);
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

        grid.setColumnSpan(taskNameLabel, 6);
        grid.setRowSpan(taskNameLabel, 1);
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
        deleteButton.setOnAction((event) -> main.setAndFocusInputField("delete " + getTaskIndex(task)));
    }

    private void setUpdateButtonEventHandler(TodoItem task) {
        updateButton.setOnAction((event) -> main.setAndFocusInputField("update " + getTaskIndex(task) + " "));
    }

    private int getTaskIndex(TodoItem task) {
        return new Scanner(task.getTaskName()).useDelimiter("\\D+").nextInt();
    }

    private void addContentToDateLabels(TodoItem task) {
        if (task.getTodoItemType().equalsIgnoreCase("Event")) {
            topDateLabel.setText("START " + task.getStartDateString());
            bottomDateLabel.setText("END " + task.getEndDateString());
            topDateLabel.setVisible(true);
            bottomDateLabel.setVisible(true);
        } else if (task.getTodoItemType().equalsIgnoreCase("Deadline")) {
            topDateLabel.setText("DUE " + task.getEndDateString());
        }
    }

    private void addContentToTaskName(TodoItem task) {
        taskNameLabel.setText(task.getTaskName().toUpperCase());
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
        return colors.get(new Random().nextInt(colors.size()));
    }
}
