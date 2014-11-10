//@author A0111764L

/* taskListCell.css

.cell {
    -fx-border-style: solid;
    -fx-border-color: white;
    -fx-border-width: 5 5 5 5;
}

*/

/* TaskListCellView.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.image.*?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.text.*?>
<AnchorPane id="cell" fx:id="anchorPane" maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity"
            minWidth="-Infinity" prefHeight="40.0" prefWidth="920.0" style="-fx-background-color: #444444;"
            xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
            fx:controller="app.viewmanagers.TaskListCellViewManager">
    <Label fx:id="overdueLabel" layoutX="557.0" layoutY="-2.0" prefHeight="30.0" prefWidth="71.0" text="ಠ_ಠ"
           textAlignment="CENTER" textFill="#797777">
        <font>
          <Font size="34.0"/>
        </font>
    </Label>
    <Button fx:id="deleteButton" layoutX="882.0" layoutY="5.0" mnemonicParsing="false"
            style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
              <image>
                  <Image url="@../resources/cross.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Delete this task. Command: &quot;delete &lt;task index&gt;&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="updateButton" layoutX="857.0" layoutY="5.0" mnemonicParsing="false" prefHeight="20.0"
            prefWidth="20.0" style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
              <image>
                  <Image url="@../resources/compose.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip
                  text="Update this task. Command: &quot;update &lt;index number&gt; [task name | start | end | priority]&quot;"/>
        </tooltip>
    </Button>
    <Label fx:id="bottomDateLabel" layoutX="628.0" layoutY="22.0" prefHeight="14.0" prefWidth="184.0"
           text="END 1 JANUARY 2015" textFill="WHITE">
        <font>
          <Font size="11.0"/>
        </font>
    </Label>
    <Label fx:id="topDateLabel" layoutX="628.0" layoutY="6.0" prefHeight="14.0" prefWidth="184.0"
           text="START 31 DECEMBER 2014 16:00" textFill="WHITE">
        <font>
          <Font size="11.0"/>
        </font>
    </Label>
    <Label fx:id="priorityLevelLabel" layoutX="806.0" layoutY="12.0" prefHeight="16.0" prefWidth="57.0" text="high"
           textAlignment="RIGHT" textFill="WHITE" wrapText="true">
        <font>
          <Font name="System Italic" size="11.0"/>
        </font>
    </Label>
    <Label fx:id="taskNameLabel" layoutX="81.0" layoutY="11.0" prefHeight="16.0" prefWidth="468.0"
           stylesheets="@../stylesheets/taskListCell.css" text="Lorem ipsum" textFill="WHITE">
        <font>
          <Font size="15.0"/>
        </font>
        <tooltip>
          <Tooltip fx:id="taskNameTooltip"/>
        </tooltip>
    </Label>
    <Button fx:id="doneButton" layoutY="5.0" mnemonicParsing="false" prefHeight="20.0" prefWidth="20.0"
            style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
              <image>
                  <Image url="@../resources/tick.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Mark as undone. Command: &quot;undone &lt;index number&gt;&quot;"/>
        </tooltip>
    </Button>
    <Label layoutX="34.0" layoutY="12.0" text="#" textFill="WHITE"/>
    <Label fx:id="indexLabel" layoutX="43.0" layoutY="12.0" prefHeight="16.0" prefWidth="38.0" text="42"
           textFill="WHITE">
        <font>
          <Font name="System Bold" size="13.0"/>
        </font>
    </Label>
    <Button fx:id="undoneButton" layoutY="6.0" mnemonicParsing="false" prefHeight="20.0" prefWidth="20.0"
            style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fitHeight="20.0" fitWidth="20.0" pickOnBounds="true" preserveRatio="true">
              <image>
                  <Image url="@../resources/untick.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Mark as done. Command: &quot;done &lt;index number&gt;&quot;"/>
        </tooltip>
    </Button>
</AnchorPane>

*/

package app.viewmanagers;

import app.model.TodoItem;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

import java.util.Date;

/**
 * Each TaskListCell represents a single TodoItem.
 */
public class TaskListCellViewManager extends ListCell<TodoItem> {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Label taskNameLabel;

    @FXML
    private Label topDateLabel;

    @FXML
    private Label bottomDateLabel;

    @FXML
    private Label priorityLevelLabel;

    @FXML
    private Label overdueLabel;

    @FXML
    private Label indexLabel;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button doneButton;

    @FXML
    private Button undoneButton;

    @FXML
    private Tooltip taskNameTooltip;

    private RootViewManager rootViewManager;

    private TodoItem task;

    private TaskListViewManager taskListViewManager;
    final long DAY_IN_MILLIS = 1000 * 60 * 60 * 24;


    /**
     * This method is implicitly called whenever the taskData of TaskListViewManager
     * is changed. It updates the ListCell object with the new TodoItem.
     * @param task Update the ListCell with this TodoItem object.
     * @param empty True if there is no TodoItem object.
     */
    @Override
    protected void updateItem(TodoItem task, boolean empty) {
        super.updateItem(task, empty);
        this.task = task;
        setGraphic(anchorPane);
        if (empty) {
            clearContent();
        } else {
            populateContent(task);
        }
    }

    /**
     * Remove all content from the ListCell.
     */
    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

    /**
     * Fills up the controls in the cell with the appropriate information.
     *
     * Some labels are shown conditionally. For example, the done/undone button
     * is shown based on the done/undone status of the task, and overdue labels aren't
     * shown when the task is done.
     *
     * Technically, we could replace the done/undone buttons with a single button, but
     * having two buttons trades memory usage for code readability and organization, which is
     * a worthwhile tradeoff.
     * @param task Populate the controls with the properties of this TodoItem.
     */
    private void populateContent(TodoItem task) {
        setIndex();
        setTaskName(task);
        setPriorityLevel(task);
        setDates(task);

        taskNameTooltip.setText(task.getTaskName());
        doneButton.setVisible(task.isDone());
        undoneButton.setVisible(!task.isDone());
        overdueLabel.setVisible(task.isOverdue() && !task.isDone());
    }

    /**
     * Setter for the priority label.
     *
     * Substring-ing is a hacky way to get around the implementation of priority strings.
     * Currently, priority strings are in the form of "1. High", "2. Medium", etc. to facilitate
     * sorting. Because we only want the priority text, we'll have to substring the number out.
     * @param task Set the priorityLevelLabel with the priority string of this TodoItem.
     */
    private void setPriorityLevel(TodoItem task) {
        priorityLevelLabel.setText(task.getPriority().substring(3).toUpperCase());
        setBackgroundColor(task);
    }

    /**
     * Setter for the date labels.
     *
     * There are four different configurations of data labels in accordance to the different types
     * of tasks: floating, deadline, event, endless.
     * @param task Set the dateLabels with the priority string of this TodoItem.
     */
    private void setDates(TodoItem task) {
        switch (task.getTodoItemType().toLowerCase()) {
            case "event":
                topDateLabel.setText("START " + task.getStartDateString());
                bottomDateLabel.setText("END " + task.getEndDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(true);
                break;
            case "deadline":
                topDateLabel.setText("DUE " + task.getEndDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(false);
                break;
            case "endless":
                topDateLabel.setText("START " + task.getStartDateString());
                topDateLabel.setVisible(true);
                bottomDateLabel.setVisible(false);
                break;
            default:
                topDateLabel.setVisible(false);
                bottomDateLabel.setVisible(false);
                break;
        }
    }

    /**
     * Setter for the index label.
     */
    private void setIndex() {
        indexLabel.setText(String.valueOf(getTaskIndex()));
    }

    /**
     * Setter for the task name label.
     *
     * toUpperCase() is purely for cosmetic reasons.
     * @param task Set the taskNameLabel with the name of this TodoItem.
     */
    private void setTaskName(TodoItem task){
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    /**
     * Since the actual task list displayed to the user is 1-index,
     * we'll have to increment getIndex() by 1.
     * @return 1-index of the task in the current list.
     */
    private int getTaskIndex() { return getIndex() + 1; }

    /**
     * Getter for the task information.
     *
     * This method is called when the update button is clicked. For UX reasons,
     * we want to pre-fill the input field with all of the task's information, such as
     * name, dates, and priority levels. This helps the user by not requiring them to
     * type the entire command again in the case where they only made a minor
     * mistake.
     *
     * @param task The referenced TodoItem.
     * @return a valid command string with all of the task's information.
     */
    private String getTaskInfo(TodoItem task) {
        String info = "";

        info = info + task.getTaskName();

        if (task.getStartDate() != null) {
           info = info + " start " + task.getStartDateString().toLowerCase();
        }

        if (task.getEndDate() != null) {
            info = info + " end " + task.getEndDateString().toLowerCase();
        }

        if (task.getPriority() != null) {
            info = info + " priority " + task.getPriority().substring(3).toLowerCase();
        }

        return info;
    }

    /**
     * Setter of the cell's background color.
     *
     * Due to the information we have from each task (due date + priority, specifically),
     * we are able to estimate the importance of the task. We will quantify this
     * importance level and represent it by adjusting the alpha value (transparency)
     * of the background color of the cell.
     *
     * Each priority level (High/Med/Low) is assigned a static weight. This weight is
     * then multiplied by a factor that's obtained from the difference in days
     * from the current day to the deadline. The range of values is from 0.315 to 1,
     * where 1 is full opaqueness.
     *
     * Done tasks always have a alpha value of 0.45.
     *
     * @param task The referenced TodoItem.
     */
    private void setBackgroundColor(TodoItem task) {
        String alphaValue;
        int differenceInDays = 0;

        if (task.getEndDate() != null) {
            differenceInDays = (int) (((task.getEndDate().getTime() - new Date().getTime())) / DAY_IN_MILLIS);
        }

        // Overdue tasks should be fully saturated
        if (differenceInDays < 0) {
            alphaValue = "1";
        } else {
            float factor = calculateFactor(differenceInDays);
            // Compare by priority level
            switch(task.getPriority().substring(3).toLowerCase()) {
                case "high":
                    alphaValue = String.valueOf(1 * factor);
                    break;
                case "medium":
                    alphaValue = String.valueOf(0.85 * factor);
                    break;
                case "low":
                    alphaValue = String.valueOf(0.7 * factor);
                    break;
                default:
                    alphaValue = "1";
            }

        }

        // Done tasks are low priority
        if (task.isDone() || task.getTodoItemType().equals(TodoItem.FLOATING)) {
            alphaValue = "0.45";
        }

        if (rootViewManager.getMainApp().getCommandController().areRandomColorsEnabled()) {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getRandomColor() + "," + alphaValue + ");");
        } else {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getCurrentColor() + "," + alphaValue + ");");
        }
    }

    /**
     * Normalize and return a scaling factor between 0.45 and 1. The further the deadline,
     * the smaller the factor.
     * @param differenceInDays
     * @return a float value between 0.45 and 1, depending on the difference in days till the deadline.
     */
    private float calculateFactor(int differenceInDays) {
        if (differenceInDays == 0) {
            return 1;
        }

        float normalized;
        if (differenceInDays > 30) {
            normalized = (float) 0.45;
        } else {
            normalized = (float) (((30.0 - differenceInDays) / 30.0) + 0.45);
        }

        if (normalized > 1) {
            normalized = 1;
        }

        return normalized;
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Also initialize the buttons and assign their event listeners.
     */
    @FXML
    private void initialize() {
        Button[] buttons = {
                updateButton,
                deleteButton,
                doneButton,
                undoneButton
        };

        for (Button button : buttons) {
            button.setOnAction((e) -> clickedButton(button) );
        }
    }

    /**
     * Clicking the buttons will fill the input field with a command string corresponding
     * to the action.
     * @param button The button to assign an action to.
     */
    private void clickedButton(Button button) {
        switch (button.getId()) {
            case "updateButton":
                rootViewManager.setAndFocusInputField("update " + getTaskIndex() + " " + getTaskInfo(task));
                break;
            case "deleteButton":
                rootViewManager.setAndFocusInputField("delete " + getTaskIndex());
                break;
            case "doneButton":
                rootViewManager.setAndFocusInputField("undone " + getTaskIndex());
                break;
            case "undoneButton":
                rootViewManager.setAndFocusInputField("done " + getTaskIndex());
                break;
        }
    }

    /**
     * Set back-reference to the rootViewManager.
     * @param rootViewManager The RootViewManager instance where the parent TaskListViewManager was created from.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    /**
     * Set back-reference to taskListViewManager.
     * @param taskListViewManager The TaskListViewManager instance where this TaskListCellViewManager instance was created from.
     */
    public void setTaskListViewManager(TaskListViewManager taskListViewManager) {
        this.taskListViewManager = taskListViewManager;
    }
}
