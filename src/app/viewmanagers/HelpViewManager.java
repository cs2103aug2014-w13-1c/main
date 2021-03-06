//@author A0111987X
package app.viewmanagers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/* HelpView.fxml
<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.Button?>
<?import javafx.scene.control.Label?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.Font?>
<Pane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="700.0"
      prefWidth="1000.0" style="-fx-background-color: rgba(0, 0, 0, 0.3);" xmlns="http://javafx.com/javafx/8"
      xmlns:fx="http://javafx.com/fxml/1" fx:controller="app.viewmanagers.HelpViewManager">
    <AnchorPane layoutX="275.0" layoutY="50.0" minHeight="0.0" minWidth="0.0" prefHeight="601.0" prefWidth="450.0"
                style="-fx-background-color: black;">
        <Label alignment="CENTER" layoutX="32.0" layoutY="14.0" prefHeight="53.0" prefWidth="386.0"
               text="Need some help using wat do?" textFill="WHITE">
            <font>
                <Font name="System Bold" size="22.0"/>
            </font>
        </Label>
        <Button fx:id="okButton" layoutX="321.0" layoutY="543.0" mnemonicParsing="false" prefHeight="26.0"
                prefWidth="97.0" text="GOT IT"/>
        <Label fx:id="helpCommands" layoutX="32.0" layoutY="66.0" prefHeight="460.0" prefWidth="386.0"
               textFill="WHITE" wrapText="true">
            <font>
                <Font size="16.0"/>
            </font>
        </Label>
    </AnchorPane>
</Pane>
 */


/**
 * View manager for the Help View. View contains a label with the available commands that the user
 * can use in the program, and an OK button.
 */
public class HelpViewManager {

    @FXML
    private Button okButton;

    private RootViewManager rootViewManager;

    @FXML
    private Label helpCommands;

    /**
     * Populates label with help text.
     */
    @FXML
    private void initialize() {
        helpCommands.setText("Here are the other commands you can use:\n" +
                "\n" +
                "add <task name> [start | end | priority]\n" +
                "delete <task index>\n" +
                "done <task index>\n" +
                "undone <task index>\n" +
                "update <task index> [task name | start | end | priority]\n" +
                "search <task name>\n" +
                "search [start | end]\n" +
                "sort [start / end / priority]\n" +
                "display [done / all / overdue]\n" +
                "undo\n" +
                "redo\n" +
                "help\n" +
                "settings\n" +
                "clear\n" +
                "exit\n" +
                "\n" +
                "Other things you can do:\n" +
                "Press [TAB] when entering the first word for auto-complete\n" +
                "Press [UP] to return to your last entered command");
        okButton.setOnAction((event) -> rootViewManager.closeHelp());
    }

    /**
     * Setter for rootViewManager
     *
     * @param rootViewManager The rootViewManager that initialises HelpViewManager.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    /**
     * Sets okButton as default button and brings focus to it.
     */
    public void focusOnButton() {
        okButton.setDefaultButton(true);
        okButton.requestFocus();
    }

    /**
     * Unsets okButton as the default button.
     */
    public void cancelFocusOnButton() {
        okButton.setDefaultButton(false);
    }
}
