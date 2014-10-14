package app.controllers;

import app.helpers.LoggingService;
import app.helpers.UserGuide;
import app.model.TodoItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.logging.Level;

public class TaskListViewController {

    @FXML
    public ListView<TodoItem> taskListView;

    @FXML
    private Label placeholder;

    private RootViewController rootViewController;

    @FXML
    private Label emptySearch;

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
    private UserGuide userGuide;

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListView -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(rootViewController.getMainApp().getResourceURL("views/TaskListCell.fxml"));
                loader.load();
                TaskListCellController controller = loader.getController();
                controller.setRootViewController(rootViewController);
                return controller;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        userGuide = new UserGuide();
        taskListView.setPlaceholder(userGuide.getUserGuide());
    }

    public void setEmptySearchPlaceholder() {
        taskListView.setPlaceholder(emptySearch);
    }

    public void setUserGuidePlaceholder() {
        taskListView.setPlaceholder(userGuide.getUserGuide());
    }

    public void updateView(ObservableList<TodoItem> taskData) {
        assert(taskData.size() >= 0);
        assert(taskData.size() <= Integer.MAX_VALUE);

        if (newTaskAdded(taskData, this.taskData)) {
            scrollToLast();
        }
        this.taskData = taskData;
        taskListView.setItems(taskData);
        LoggingService.getLogger().log(Level.INFO, "Refreshed task list.");
    }

    private boolean newTaskAdded(ObservableList<TodoItem> _new, ObservableList<TodoItem> _old) {
        return _new.size() > _old.size();
    }

    private void scrollToLast() {
        taskListView.scrollTo(taskData.size());
    }


    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

}
