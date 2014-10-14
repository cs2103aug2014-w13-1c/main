package app.viewmanagers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by jolly on 10/10/14.
 */
public class HelpViewManager {

    @FXML
    private Button okButton;

    private RootViewManager rootViewManager;

    @FXML
    private void initialize() {
        okButton.setOnAction((event) -> rootViewManager.closeHelp());
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void focusOnButton() {
        okButton.setDefaultButton(true);
        okButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        okButton.setDefaultButton(false);
    }
}
