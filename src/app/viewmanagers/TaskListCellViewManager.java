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

    private void clearContent() {
        setText(null);
        setGraphic(null);
    }

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

    private void setPriorityLevel(TodoItem task) {
        priorityLevelLabel.setText(task.getPriority().substring(3).toUpperCase());
        setBackgroundColor(task);
    }

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

    private void setIndex() {
        indexLabel.setText(String.valueOf(getTaskIndex()));
    }

    private void setTaskName(TodoItem task){
        taskNameLabel.setText(task.getTaskName().toUpperCase());
    }

    private int getTaskIndex() { return getIndex() + 1; }

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
        if (task.isDone()) {
            alphaValue = "0.45";
        }

        if (rootViewManager.getMainApp().getCommandController().areRandomColorsEnabled()) {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getRandomColor() + "," + alphaValue + ");");
        } else {
            anchorPane.setStyle("-fx-background-color: rgba(" + taskListViewManager.getCurrentColor() + "," + alphaValue + ");");
        }
    }

    // Return a value between 0.5 and 1
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

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void setTaskListViewManager(TaskListViewManager taskListViewManager) {
        this.taskListViewManager = taskListViewManager;
    }
}
