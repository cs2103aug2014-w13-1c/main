//@author A0111764L


/* taskListView.css

Retrieved from: http://blog.ngopal.com.np/2012/07/11/customize-scrollbar-via-css/
I'm not the author of the code, so it will not be included here.

*/

/* TaskListView.fxml

<?xml version="1.0" encoding="UTF-8"?>


<?import javafx.scene.control.Label?>
<?import javafx.scene.control.ListView?>
<?import javafx.scene.text.Font?>
<ListView fx:id="taskListView" fixedCellSize="44.0" prefHeight="400.0" prefWidth="400.0" stylesheets="@../stylesheets/taskList.css" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1" fx:controller="app.viewmanagers.TaskListViewManager">
    <Label text="You have no tasks." textFill="BLACK">
        <font>
            <Font size="20.0" />
        </font>
    </Label>
    <Label fx:id="emptySearch" text="No tasks found." textFill="BLACK">
        <font>
            <Font size="20.0" />
        </font>
    </Label>
</ListView>

 */

package app.viewmanagers;

import app.helpers.LoggingService;
import app.helpers.WelcomePane;
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
import java.util.UUID;
import java.util.logging.Level;

/**
 * TaskListViewManager manages the ListView object, which is responsible
 * for creating and displaying individual ListCell objects that represent
 * a TodoItem each.
 */
public class TaskListViewManager {

    @FXML
    public ListView<TodoItem> taskListView;

    @FXML
    private Label emptySearch;

    private RootViewManager rootViewManager;
    private WelcomePane welcomePane;
    private ObservableList<TodoItem> taskData = FXCollections.observableArrayList();

    private List<String> colors;
    private String color;

    /**
     * In our implementation, we are using a custom CellFactory for generating
     * ListCells. This opens up the class to allow customized controls and methods.
     *
     * The placeholder of the ListView also differs based on the user's context.
     * This can be either a ListView with 0 tasks (welcomePane) or a search view with
     * 0 results (emptySearch Label).
     */
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

        welcomePane = new WelcomePane();
        taskListView.setPlaceholder(welcomePane.getWelcomePane());
    }

    /**
     * Set the placeholder when there are no results for a search term.
     */
    public void setEmptySearchPlaceholder() {
        taskListView.setPlaceholder(emptySearch);
    }

    /**
     * Set the placeholder when the task list is empty.
     */
    public void setUserGuidePlaceholder() {
        taskListView.setPlaceholder(welcomePane.getWelcomePane());
    }

    /**
     * This method is called from various components whenever the task
     * list needs to be updated. For example, searching and changing
     * view context (Overdue, Done, Undone) uses this method.
     * @param taskData An ObservableList containing TodoItems to replace the current list.
     */
    public void updateView(ObservableList<TodoItem> taskData) {

        // Workaround for inherent bug in JavaFX that refuses
        // to update the ListView with new objects.
        taskListView.getItems().clear();

        this.taskData = taskData;
        taskListView.setItems(taskData);

        if (this.taskData.size() > 0) {
            scrollToLastModifiedTask(taskData);
        }

        rootViewManager.refreshSidebar();

        LoggingService.getLogger().log(Level.INFO, "Refreshed task list.");
    }

    /**
     * Provide automatic scrolling to the least recently modified task as an UX feature.
     * @param taskData The current list of TodoItems being displayed to the user.
     */
    private void scrollToLastModifiedTask(ObservableList<TodoItem> taskData) {
        UUID uuid = rootViewManager.getMainApp().getTaskController().getLastModifiedUUID();
        int index = convertUUIDtoIndex(uuid, taskData);
        scrollTo(index);
        highlight(index);
    }

    /**
     * Scroll to a specified index. UX feature.
     * @param index The index of the task list cell to scroll to.
     */
    public void scrollTo(int index) {
        taskListView.scrollTo(index);
    }

    /**
     * Highlight a specified index. UX feature.
     * @param index The index of the task list cell to highlight.
     */
    public void highlight(int index) {
        taskListView.getSelectionModel().select(index);
        taskListView.getFocusModel().focus(index);
    }

    /**
     * To pinpoint to a specific task in the current visible task list, the implementation
     * does not provide an accurate task index, as these tasks are filtered out by their statuses
     * (done, undone) into different lists. Hence, we're making use of a more unique identifier,
     * the UUID. This method does a linear search in the current task list to get the index of
     * the task with the specific UUID.
     *
     * Returns -1 if task is not found.
     *
     * @param uuid The UUID of the specified TodoItem.
     * @param taskData The list of TodoItems to check against.
     * @return index of task in current task list with the specified UUID.
     */
    private int convertUUIDtoIndex(UUID uuid, ObservableList<TodoItem> taskData) {
        for (TodoItem task : taskData) {
            if (task.getUUID() == uuid) {
                return taskData.indexOf(task);
            }
        }
        return -1;
    }

    /**
     * When the user disable random colors, this method returns the selected
     * color that was initialized at launch time.
     *
     * @return The chosen color from instantiation time.
     */
    public String getCurrentColor() {
        return color;
    }

    /**
     * When the user enables random colors, this method generates the colors
     * to be applied for each ListCell.
     * @return A random color.
     */
    public String getRandomColor() {
        return colors.get(new Random().nextInt(colors.size()));
    }

    /**
     * Initialize the color palette.
     */
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

    /**
     * Set back-reference to the rootviewManager.
     * @param rootViewManager The RootViewManager Instance.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    /**
     * Public getter for the current task list.
     * @return an ObservableList of TodoItems.
     */
    public ObservableList<TodoItem> getTaskData() {
        return taskData;
    }
}
