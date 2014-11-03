package app.viewmanagers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Created by jin on 30/10/14.
 */
public class TitleBarViewManager {

    @FXML
    private Pane titleBarView;

    @FXML
    private Label titleBarLabel;

    private RootViewManager rootViewManager;

    @FXML
    public void initialize() {  }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
