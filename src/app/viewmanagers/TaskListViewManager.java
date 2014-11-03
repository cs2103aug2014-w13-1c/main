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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

public class TaskListViewManager {

    @FXML
    public ListView<TodoItem> taskListView;

    @FXML
    private Label emptySearch;

    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
    private UserGuide userGuide;

    private RootViewManager rootViewManager;
    private List<String> colors;
    private String color;


    @FXML
    public void initialize() {
        initColors();
        color = getRandomColor();

        taskListView.setCellFactory(taskListView -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(rootViewManager.getMainApp().getResourceURL("views/TaskListCellView.fxml"));
                loader.load();
                TaskListCellViewManager controller = loader.getController();
                controller.setRootViewManager(rootViewManager);
                controller.setTaskListViewManager(this);
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

        // Workaround for inherent bug in JavaFX that refuses
        // to update the ListView with new objects.
        taskListView.getItems().clear();

        this.taskData = taskData;
        taskListView.setItems(taskData);

        if (this.taskData.size() > 0) {
            scrollToLastModifiedTask();
        }

        LoggingService.getLogger().log(Level.INFO, "Refreshed task list.");
    }

    private void scrollToLastModifiedTask() {
        int index = rootViewManager.getMainApp().getTaskController().getLastModifiedIndex();
        taskListView.scrollTo(index);
        taskListView.getSelectionModel().select(index);
        taskListView.getFocusModel().focus(index);
    }

    public String getCurrentColor() {
        return color;
    }

    public String getRandomColor() {
        return colors.get(new Random().nextInt(colors.size()));
    }

    private void initColors() {
        colors = Arrays.asList(
                "208, 23, 22", // red 700
                "194, 24, 91", // pink 700
                "123, 31, 162", // purple 700
                "81, 45, 168", // deep purple 700
                "57, 63, 159", // indigo 700
                "69, 94, 222", // blue 700
                "2, 136, 209", // light blue 700
                "0, 151, 167", // cyan 700
                "0, 121, 107", // teal 700
                "10, 126, 7", // green 700
                "85, 139, 47", // light green 800
                "130, 119, 23", // lime 900
                "230, 81, 0", // orange 900
                "229, 74, 25", // deep orange 700
                "121, 85, 72"); // brown 500
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public ObservableList<TodoItem> getTaskData() {
        return taskData;
    }

}
