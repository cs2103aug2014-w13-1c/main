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

import app.controllers.TaskController;
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

    public static enum SortStyle { START, END, PRIORITY, NAME };

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    public void initialize() {
        initSortStyleChoiceBox();
    }

    /**
     * Initialize the choice box that lets the user choose the sorting style of the
     * task list.
     */
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
     * @param isVisible If set to true, show the sorting controls.
     */
    public void setSortControlsVisible(boolean isVisible) {
        sortByLabel.setVisible(isVisible);
        sortStyleChoiceBox.setVisible(isVisible);
    }

    /**
     * Public setter for the titleBarLabel.
     * @param title The title of the current view context.
     */
    public void setTitle(String title) {
        titleBarLabel.setText(title.toUpperCase());
    }

    /**
     * Called by ActionController. This changes the selected item
     * in the sortStyleChoiceBox when the user sorts the list
     * with a command input instead of selecting the style in
     * the choiceBox.
     * @param style The SortStyle enum element (START, END, NAME, PRIORITY).
     */
    public void setSortStyle(TaskController.SortingStyle style) {
        switch (style) {
            case TASKNAME_ENDDATE:
                sortStyleChoiceBox.setValue("TASK NAME");
                break;
            case STARTDATE_PRIORITY:
                sortStyleChoiceBox.setValue("START DATE");
                break;
            case ENDDATE_PRIORITY:
                sortStyleChoiceBox.setValue("END DATE");
                break;
            case PRIORITY_ENDDATE:
                sortStyleChoiceBox.setValue("PRIORITY");
                break;
        }
    }

    /**
     * Set back-reference to the rootViewManager.
     * @param rootViewManager The RootViewManager instance where this TitleBarViewManager instance was created from.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
