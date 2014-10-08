package app.controllers;

import app.model.TodoItem;
import javafx.collections.FXCollections;
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

    private RootViewController rootViewController;

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListView -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(rootViewController.getMainApp().getClass().getResource("views/TaskListCell.fxml"));
                loader.load();
                TaskListCellController controller = loader.getController();
                controller.setRootViewController(rootViewController);
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
       return _new.size() > _old.size();
    }

    private void scrollToLast() {
        taskListView.scrollTo(taskData.size());
    }


    public void setRootController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

    public RootViewController getRootViewController() {
        return rootViewController;
    }
}