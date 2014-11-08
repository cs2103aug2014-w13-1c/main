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
    <children>
        <AnchorPane layoutX="275.0" layoutY="85.0" minHeight="0.0" minWidth="0.0" prefHeight="531.0" prefWidth="450.0"
                    style="-fx-background-color: black;">
            <children>
                <Label alignment="CENTER" layoutX="32.0" layoutY="14.0" prefHeight="53.0" prefWidth="386.0"
                       text="Need some help using wat do?" textFill="WHITE">
                    <font>
                        <Font name="System Bold" size="22.0"/>
                    </font>
                </Label>
                <Button fx:id="okButton" layoutX="321.0" layoutY="483.0" mnemonicParsing="false" prefHeight="26.0"
                        prefWidth="97.0" text="GOT IT"/>
                <Label fx:id="helpCommands" layoutX="32.0" layoutY="66.0" prefHeight="399.0" prefWidth="386.0"
                       textFill="WHITE" wrapText="true">
                    <font>
                        <Font size="16.0"/>
                    </font>
                </Label>
            </children>
        </AnchorPane>
    </children>
</Pane>
 */


/**
 * Created by jolly on 10/10/14.
 */
public class HelpViewManager {

    @FXML
    private Button okButton;

    private RootViewManager rootViewManager;

    @FXML
    private Label helpCommands;

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
                "exit");
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
