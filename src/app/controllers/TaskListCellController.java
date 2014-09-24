package app.controllers;

import app.model.TodoItem;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

/**
 * Created by jin on 24/9/14.
 */
public class TaskListCellController extends ListCell<TodoItem> {

    private GridPane grid = new GridPane();
    private Label taskNameLabel = new Label();

    public TaskListCellController() {
        configureGrid();
        configureTaskName();
        addControlsToGrid();
    }

    private void configureGrid() {
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
    }

    private void configureTaskName() {
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
        taskNameLabel.setText(task.getTaskName());
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

}
