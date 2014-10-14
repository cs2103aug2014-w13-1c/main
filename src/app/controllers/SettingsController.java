package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

import java.io.File;

/**
 * Created by jin on 8/10/14.
 */
public class SettingsController {

    @FXML
    private TextField filePathTextField;

    @FXML
    private Button browseButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private RootViewController rootViewController;

    private File filePath;

    @FXML
    private void initialize() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        if (filePath != null) {
            filePathTextField.setText(filePath.toString());
        }

        browseButton.setOnAction((event) -> showChooser(directoryChooser));
        saveButton.setOnAction((event) -> rootViewController.closeSettings(filePath));
        cancelButton.setOnAction((event) -> rootViewController.closeSettings(null));
    }

    private void showChooser(DirectoryChooser directoryChooser) {
        filePath = directoryChooser.showDialog(rootViewController.getMainApp().getPrimaryStage());
        filePathTextField.setText(filePath.toString());
    }

    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

    public void focusOnButton() {
        cancelButton.setDefaultButton(true);
        cancelButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        cancelButton.setDefaultButton(false);
    }
}
