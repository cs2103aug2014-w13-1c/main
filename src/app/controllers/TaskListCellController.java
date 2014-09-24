package app.controllers;

import app.model.TodoItem;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;

/**
 * Created by jin on 24/9/14.
 */
public class TaskListCellController extends ListCell<TodoItem> {

    private GridPane grid = new GridPane();
    private Label taskNameLabel = new Label();

    ArrayList<String> colors = new ArrayList<String>();

    public TaskListCellController() {
        this.getStylesheets().add("app/stylesheets/taskListCell.css");
        this.getStyleClass().add("cell");
        taskNameLabel.setTextFill(Color.WHITE);

        initColors();
        this.setStyle("-fx-background-color: " + getRandomColor() + ";");

        configureGrid();
        configureTaskName();
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
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(5, 7, 5, 7));
    }

    private void configureTaskName() {
        taskNameLabel.getStylesheets().add(this.getStylesheets().get(0));
        taskNameLabel.getStyleClass().add("task-name-label");
        taskNameLabel.setMaxWidth(350);
        taskNameLabel.setMaxHeight(80);
        taskNameLabel.setWrapText(true);
    }

    private void addControlsToGrid() {
        grid.add(taskNameLabel, 1, 0);
    }

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    private void addContent(TodoItem task) {
        setText(null);
        taskNameLabel.setText(task.getTaskName().toUpperCase());
        setGraphic(grid);
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
