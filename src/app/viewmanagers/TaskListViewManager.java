package app.viewmanagers;

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

public class TaskListViewManager {

    @FXML
    public ListView<TodoItem> taskListView;

    @FXML
    private Label placeholder;

    private RootViewManager rootViewManager;

    @FXML
    private Label emptySearch;

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
    private UserGuide userGuide;

    @FXML
    public void initialize() {
        taskListView.setCellFactory(taskListView -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(rootViewManager.getMainApp().getResourceURL("views/TaskListCellView.fxml"));
                loader.load();
                TaskListCellViewManager controller = loader.getController();
                controller.setRootViewManager(rootViewManager);
                return controller;
            } catch (IOException e) {
                LoggingService.getLogger().log(Level.SEVERE, e.getMessage());
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

        scrollToLastModifiedTask();

        this.taskData = taskData;
        taskListView.setItems(taskData);
        LoggingService.getLogger().log(Level.INFO, "Refreshed task list.");
    }

    private void scrollToLastModifiedTask() {
        int index = rootViewManager.getMainApp().getTaskController().getLastModifiedIndex();
        taskListView.scrollTo(index);
        taskListView.getSelectionModel().select(index);
        taskListView.getFocusModel().focus(index);
    }


    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

}
