package app.viewmanagers;

import app.helpers.InvalidInputException;
import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.logging.Level;

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
                if (!rootViewManager.getMainApp().getCommandController().getUndoController().isUndoEmpty()) {
                    try {
                        rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("undo");
                    } catch (InvalidInputException e) {
                        LoggingService.getLogger().log(Level.INFO, "Invalid Input Exception: empty command");
                    }
                } else {
                    rootViewManager.getMainApp().showErrorNotification("Error", "Command error.\n");
                }
                break;
            case "redoButton":
                if (!rootViewManager.getMainApp().getCommandController().getUndoController().isRedoEmpty()) {
                    try {
                        rootViewManager.getInputFieldViewManager().checkCommandLengthAndExecute("redo");
                    } catch (InvalidInputException e) {
                        LoggingService.getLogger().log(Level.INFO, "Invalid Input Exception: empty command");
                    }
                } else {
                    rootViewManager.getMainApp().showErrorNotification("Error", "Command error.\n");
                }
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

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void refreshUndoButton() {
        if (rootViewManager.getMainApp().getCommandController().getUndoController().isUndoEmpty()) {
            undoImageView.setImage(new Image("app/resources/undo-grey.png"));
        } else{
            undoImageView.setImage(new Image("app/resources/undo.png"));
        }
    }
    public void refreshRedoButton() {
        if (rootViewManager.getMainApp().getCommandController().getUndoController().isRedoEmpty()) {
            redoImageView.setImage(new Image("app/resources/redo-grey.png"));
        } else{
            redoImageView.setImage(new Image("app/resources/redo.png"));
        }
    }
}
