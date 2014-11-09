//@author A0111764L

/* titleBarView.css

.choice-box .label {
    -fx-text-fill: black;
}

*/

/* TitleBarView.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.ChoiceBox?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>
<Pane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity"
      prefHeight="30.0" prefWidth="1000.0" xmlns="http://javafx.com/javafx/8" xmlns:fx="http://javafx.com/fxml/1"
      fx:controller="app.viewmanagers.TitleBarViewManager">
    <AnchorPane prefHeight="30.0" prefWidth="1000.0" style="-fx-background-color: black;">
        <children>
          <Label fx:id="titleBarLabel" alignment="CENTER" layoutX="290.0" layoutY="5.0" prefHeight="23.0"
                 prefWidth="421.0" text="Title" textAlignment="CENTER" textFill="WHITE">
              <font>
                  <Font size="18.0"/>
              </font>
          </Label>
          <ChoiceBox id="choice-box" fx:id="sortStyleChoiceBox" layoutX="881.0" layoutY="1.0" prefHeight="26.0"
                     prefWidth="117.0"/>
          <Label fx:id="sortByLabel" layoutX="804.0" layoutY="5.0" text="SORT BY:" textFill="WHITE">
              <font>
                  <Font size="15.0"/>
              </font>
          </Label>
        </children>
    </AnchorPane>
</Pane>

 */

package app.viewmanagers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

/**
 * The TitleBar contains a label that shows the user their current context,
 * as well as a drop-down choice box that lets the user switch how the tasks
 * are sorted by.
 */
public class TitleBarViewManager {

    @FXML
    private Label titleBarLabel;

    @FXML
    private ChoiceBox sortStyleChoiceBox;

    @FXML
    private Label sortByLabel;

    private RootViewManager rootViewManager;

    @FXML
    public void initialize() {
        initSortStyleChoiceBox();
    }

    private void initSortStyleChoiceBox() {
        sortStyleChoiceBox.setItems(FXCollections.observableArrayList(
                "TASK NAME", "START DATE", "END DATE", "PRIORITY"
        ));

        sortStyleChoiceBox.setValue("END DATE");


        sortStyleChoiceBox
                .getSelectionModel()
                .selectedIndexProperty()
                .addListener((observableValue, oldIndex, newIndex) ->
                rootViewManager
                        .getMainApp()
                        .getTaskController()
                        .setSortingStyle((int) newIndex)
                );
    }

    /**
     * The sorting control is disabled in the search view context,
     * and enabled in all other contexts.
     * @param isVisible
     */
    public void setSortControlsVisible(boolean isVisible) {
        sortByLabel.setVisible(isVisible);
        sortStyleChoiceBox.setVisible(isVisible);
    }

    /**
     * Public setter for the titleBarLabel.
     * @param title
     */
    public void setTitle(String title) {
        titleBarLabel.setText(title.toUpperCase());
    }

    /**
     * Set back-reference to the rootViewManager.
     * @param rootViewManager
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
