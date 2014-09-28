package app.controllers;

import app.Main;
import app.model.TodoItem;
import com.sun.tools.javac.comp.Todo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;

public class TaskListViewController {

    @FXML
    public ListView<TodoItem> taskListView;

    @FXML
    private Label placeholder;

    private Main main;

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListView -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(main.getClass().getResource("views/TaskListCell.fxml"));
                loader.load();
                TaskListCellController controller = loader.getController();
                controller.setMainApp(main);
                return controller;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        taskListView.setPlaceholder(placeholder);
    }

    public void updateView(ObservableList<TodoItem> taskData) {
        if (newTaskAdded(taskData, this.taskData)) {
            scrollToLast();
        }
        this.taskData = taskData;
        taskListView.setItems(taskData);

    }

    private boolean newTaskAdded(ObservableList<TodoItem> _new, ObservableList<TodoItem> _old) {
       return _new.size() == _old.size() + 1;
    }

    private void scrollToLast() {
        taskListView.scrollTo(taskData.size());
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;
    }
}