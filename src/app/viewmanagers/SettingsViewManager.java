package app.viewmanagers;

import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by jin on 8/10/14.
 */
public class SettingsViewManager {

    @FXML
    private TextField filePathTextField;

    @FXML
    private Button browseButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private RootViewManager rootViewManager;

    private File filePath;

    @FXML
    private void initialize() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        // filePath = rootViewManager.getMainApp().getSaveLocation();

        if (filePath != null) {
            filePathTextField.setText(filePath.toString());
        }

        browseButton.setOnAction((event) -> showChooser(directoryChooser));
        saveButton.setOnAction((event) -> rootViewManager.closeSettings(filePath));
        cancelButton.setOnAction((event) -> rootViewManager.closeSettings(null));
    }

    private void showChooser(DirectoryChooser directoryChooser) {
        filePath = directoryChooser.showDialog(rootViewManager.getMainApp().getPrimaryStage());
        filePathTextField.setText(filePath.toString());

        assert(filePath.length() >= 0);
        LoggingService.getLogger().log(Level.INFO, "Selected filePath: " + filePath.toString());
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void focusOnButton() {
        cancelButton.setDefaultButton(true);
        cancelButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        cancelButton.setDefaultButton(false);
    }
}
