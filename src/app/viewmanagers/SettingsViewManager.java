package app.viewmanagers;

import app.helpers.LoggingService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

    @FXML
    private CheckBox randomColorsCheckBox;

    @FXML
    private CheckBox notificationCheckBox;

    private RootViewManager rootViewManager;

    private File directory;
    private Boolean randomColorsEnabled;

    @FXML
    private void initialize() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        browseButton.setOnAction((event) -> showChooser(directoryChooser));
        saveButton.setOnAction((event) -> rootViewManager.saveSettings(filePathTextField.getText(), randomColorsCheckBox.isSelected(), notificationCheckBox.isSelected()));
        cancelButton.setOnAction((event) -> rootViewManager.closeSettings());
    }

    private void showChooser(DirectoryChooser directoryChooser) {
        directory = directoryChooser.showDialog(rootViewManager.getMainApp().getPrimaryStage());
        filePathTextField.setText(directory.toString());

        assert(directory.length() >= 0);
        LoggingService.getLogger().log(Level.INFO, "Selected directory: " + directory.toString());
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

    public void setAbsolutePathToDirectory(String absolutePath) {
        filePathTextField.setText(absolutePath);
    }

    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        notificationCheckBox.setSelected(notificationsEnabled);
    }

    public void setRandomColorsEnabled(Boolean randomColorsEnabled) {
        randomColorsCheckBox.setSelected(randomColorsEnabled);
    }
}
