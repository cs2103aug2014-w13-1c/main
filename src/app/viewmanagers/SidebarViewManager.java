//@author A0111764L

/* sidebar.css

.sidebar {
    -fx-border-style: none;
    -fx-stroke: black;
    -fx-stroke-width: 8;
    -fx-background-color: #4ECDC4;
}

*/

/* SidebarView.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.image.*?>
<?import javafx.scene.layout.VBox?>
<VBox prefHeight="592.0" prefWidth="32.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="app.viewmanagers.SidebarViewManager">
    <opaqueInsets>
    </opaqueInsets>
    <Button fx:id="displayButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
            <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                       scaleY="0.8">
                <Image url="@../resources/list.png"/>
            </ImageView>
        </graphic>
        <tooltip>
        <Tooltip text="Display undone tasks. Command: &quot;display&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="showDoneButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
            <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                       scaleY="0.8">
                <image>
                    <Image url="@../resources/tick.png"/>
                </image>
            </ImageView>
        </graphic>
        <tooltip>
        <Tooltip text="Show completed tasks. Command: &quot;display done&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="addButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
            <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                       scaleY="0.8">
                <image>
                    <Image url="@../resources/add.png"/>
                </image>
            </ImageView>
        </graphic>
        <tooltip>
        <Tooltip text="Add a task. Command: &quot;add &lt;task name&gt; [start | end | priority]&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="searchButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
            <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                       scaleY="0.8">
                <image>
                    <Image url="@../resources/search.png"/>
                </image>
            </ImageView>
        </graphic>
        <tooltip>
        <Tooltip text="Search for a task. Command: &quot;search &lt;task name&gt;&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="undoButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fx:id="undoImageView" fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true"
                     scaleX="0.8" scaleY="0.8">
              <image>
                  <Image url="@../resources/undo.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Undo action. Command: &quot;undo&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="redoButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;">
        <graphic>
          <ImageView fx:id="redoImageView" fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true"
                     scaleX="0.8" scaleY="0.8">
              <image>
                  <Image url="@../resources/redo.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Redo action. Command: &quot;redo&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="helpButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;" translateY="230.0">
        <graphic>
          <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                     scaleY="0.8">
              <image>
                  <Image url="@../resources/bulb.png"/>
              </image>
          </ImageView>
        </graphic>
        <tooltip>
          <Tooltip text="Help. Command: &quot;help&quot;"/>
        </tooltip>
    </Button>
    <Button fx:id="settingsButton" mnemonicParsing="false" prefHeight="32.0" prefWidth="32.0"
            style="-fx-background-color: transparent;" translateY="230.0">
        <graphic>
            <ImageView fitHeight="32.0" fitWidth="32.0" pickOnBounds="true" preserveRatio="true" scaleX="0.8"
                       scaleY="0.8">
                <image>
                    <Image url="@../resources/settings.png"/>
                </image>
            </ImageView>
        </graphic>
        <tooltip>
        <Tooltip text="Settings. Command: &quot;settings&quot;"/>
        </tooltip>
    </Button>
</VBox>

 */

package app.viewmanagers;

import app.helpers.InvalidInputException;
import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.logging.Level;

/**
 * This class manages the Sidebar control, which consists of various buttons
 * for interacting with the application.
 */
public class SidebarViewManager {

    @FXML
    private Button displayButton;

    @FXML
    private Button addButton;

    @FXML
    private Button searchButton;

    @FXML
    private Button showDoneButton;

    @FXML
    private Button undoButton;

    @FXML
    private ImageView undoImageView;

    @FXML
    private Button redoButton;

    @FXML
    private ImageView redoImageView;

    @FXML
    private Button helpButton;

    @FXML
    private Button settingsButton;

    private RootViewManager rootViewManager;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Also sets the event listeners for each button.
     */
    @FXML
    private void initialize() {
        Button[] buttons = {
                displayButton,
                addButton,
                searchButton,
                helpButton,
                undoButton,
                redoButton,
                settingsButton,
                showDoneButton
        };

        for (Button button : buttons) {
            button.setOnAction((e) -> clickedButton(button));
        }
    }

    /**
     * Apply the button's event listener action. Some actions are immediate upon clicking (undo,
     * redo), while the rest fill the input field with a command string.
     * @param button The button to assign an action to.
     */
    private void clickedButton(Button button) {
        LoggingService.getLogger().log(Level.INFO, "Clicked on: " + button.getId());
        switch (button.getId()) {
            case "displayButton":
                rootViewManager.setAndFocusInputField("display");
                break;
            case "addButton":
                rootViewManager.setAndFocusInputField("add ");
                break;
            case "searchButton":
                rootViewManager.setAndFocusInputField("search ");
                break;
            case "showDoneButton":
                rootViewManager.setAndFocusInputField("display done");
                break;
            case "undoButton":
                tryUndo();
                break;
            case "redoButton":
                tryRedo();
                break;
            case "helpButton":
                rootViewManager.openHelp();
                break;
            case "settingsButton":
                rootViewManager.openSettings();
                break;
            default:
                break;
        }
    }

    /**
     * Attempt to undo the state. Show error if there's nothing to undo.
     */
    private void tryUndo() {
        if (isUndoable()) {
            try {
                rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("undo");
            } catch (InvalidInputException e) {
                LoggingService.getLogger().log(Level.SEVERE, e.getMessage());
            }
        } else {
            rootViewManager.getMainApp().showErrorNotification("Error", "Nothing to undo.\n");
        }
    }

    /**
     * Attempt to redo the state. Show error if there's nothing to redo.
     */
    private void tryRedo() {
        if (isRedoable()) {
            try {
                rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("redo");
            } catch (InvalidInputException e) {
                LoggingService.getLogger().log(Level.SEVERE, e.getMessage());
            }
        } else {
            rootViewManager.getMainApp().showErrorNotification("Error", "Nothing to redo.\n");
        }
    }

    /**
     * Check with UndoController if there's anything to undo.
     * @return True if there is an undoable state to go to.
     */
    private boolean isRedoable() {
        return !rootViewManager.getMainApp().getCommandController().getUndoController().isRedoEmpty();
    }

    /**
     * Check with UndoController if there's anything to redo.
     * @return True if there is a redoable state to go to.
     */
    private boolean isUndoable() {
        return !rootViewManager.getMainApp().getCommandController().getUndoController().isUndoEmpty();
    }

    /**
     * This is called whenever there is a change of list state. Grey out undo button if
     * there is nothing to undo.
     */
    public void refreshUndoButton() {
        if (rootViewManager.getMainApp().getCommandController().getUndoController().isUndoEmpty()) {
            undoImageView.setImage(new Image("app/resources/undo-grey.png"));
        } else{
            undoImageView.setImage(new Image("app/resources/undo.png"));
        }
    }

    /**
     * This is called whenever there is a change of list state. Grey out redo button if
     * there is nothing to redo.
     */
    public void refreshRedoButton() {
        if (rootViewManager.getMainApp().getCommandController().getUndoController().isRedoEmpty()) {
            redoImageView.setImage(new Image("app/resources/redo-grey.png"));
        } else{
            redoImageView.setImage(new Image("app/resources/redo.png"));
        }
    }

    /**
     * Set back-reference to rootViewManager.
     * @param rootViewManager The RootViewManager instance where this SidebarViewManager was created from.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
