package app.viewmanagers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
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

    @FXML
    private ChoiceBox sortStyleChoiceBox;

    @FXML
    private Label sortByLabel;

    private RootViewManager rootViewManager;

    @FXML
    public void initialize() {
        sortStyleChoiceBox.setItems(FXCollections.observableArrayList(
            "TASK NAME", "START DATE", "END DATE", "PRIORITY"
        ));

        sortStyleChoiceBox.setValue("END DATE");

        sortStyleChoiceBox.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) -> rootViewManager.getMainApp().getTaskController().setSortingStyle((int) newIndex));
    }

    public void setSortControlsVisible(boolean isVisible) {
        sortByLabel.setVisible(isVisible);
        sortStyleChoiceBox.setVisible(isVisible);
    }

    public void setTitle(String title) {
        titleBarLabel.setText(title.toUpperCase());
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
