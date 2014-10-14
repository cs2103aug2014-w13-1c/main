package app.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Created by jolly on 10/10/14.
 */
public class HelpViewController {

    @FXML
    private Button okButton;

    private RootViewController rootViewController;

    @FXML
    private void initialize() {
        okButton.setOnAction((event) -> rootViewController.closeHelp());
    }

    public void setRootViewController(RootViewController rootViewController) {
        this.rootViewController = rootViewController;
    }

    public void focusOnButton() {
        okButton.setDefaultButton(true);
        okButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        okButton.setDefaultButton(false);
    }
}
