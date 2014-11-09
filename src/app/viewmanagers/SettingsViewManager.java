// @author A0111764L

/* SettingsView.fxml

<?xml version="1.0" encoding="UTF-8"?>

<?import javafx.scene.control.*?>
<?import javafx.scene.layout.AnchorPane?>
<?import javafx.scene.layout.Pane?>
<?import javafx.scene.text.*?>
<Pane maxHeight="-Infinity" maxWidth="-Infinity" minHeight="-Infinity" minWidth="-Infinity" prefHeight="700.0"
      prefWidth="1000.0" style="-fx-background-color: rgba(0, 0, 0, 0.3);" xmlns="http://javafx.com/javafx/8"
      xmlns:fx="http://javafx.com/fxml/1" fx:controller="app.viewmanagers.SettingsViewManager">
    <AnchorPane layoutX="275.0" layoutY="150.0" minHeight="0.0" minWidth="0.0" prefHeight="400.0" prefWidth="450.0"
                style="-fx-background-color: black;">
        <children>
          <Label layoutX="14.0" layoutY="14.0" prefHeight="53.0" prefWidth="386.0"
                 text="CHOOSE YOUR SAVEFILE LOCATION." textFill="WHITE">
              <font>
                  <Font size="20.0"/>
              </font>
          </Label>
          <TextField fx:id="filePathTextField" layoutX="32.0" layoutY="67.0" prefHeight="44.0" prefWidth="294.0"
                     promptText="watdo.json">
              <font>
                  <Font size="20.0"/>
              </font>
          </TextField>
          <Button fx:id="browseButton" layoutX="336.0" layoutY="67.0" mnemonicParsing="false" prefHeight="44.0"
                  prefWidth="82.0" text="BROWSE?">
              <font>
                  <Font size="12.0"/>
              </font>
          </Button>
          <Button fx:id="saveButton" layoutX="217.0" layoutY="351.0" mnemonicParsing="false" prefHeight="26.0"
                  prefWidth="97.0" text="OK, SAVE"/>
          <Button fx:id="cancelButton" layoutX="326.0" layoutY="351.0" mnemonicParsing="false" prefHeight="26.0"
                  prefWidth="97.0" text="NEVERMIND"/>
          <CheckBox fx:id="randomColorsCheckBox" layoutX="14.0" layoutY="140.0" mnemonicParsing="false"
                    text="ENABLE RANDOM COLORS" textFill="WHITE">
              <font>
                  <Font size="20.0"/>
              </font>
          </CheckBox>
          <CheckBox fx:id="notificationCheckBox" layoutX="14.0" layoutY="186.0" mnemonicParsing="false"
                    text="ENABLE NOTIFICATIONS" textFill="WHITE">
              <font>
                  <Font size="20.0"/>
              </font>
          </CheckBox>
        </children>
    </AnchorPane>
</Pane>
 */

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
 * This is the view manager for the Settings component, which
 * appears as a pop up when "settings" is entered as a command or if
 * the settings button is clicked.
 *
 * It lets the user set the directory of the .json document, as well
 * as options to enable/disable random list cell colors and notifications.
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

    /**
     *  Clicking on the save button passes all options to rootViewManager.
     */
    @FXML
    private void initialize() {
        DirectoryChooser directoryChooser = new DirectoryChooser();

        browseButton.setOnAction((event) -> showChooser(directoryChooser));
        cancelButton.setOnAction((event) -> rootViewManager.closeSettings());
        saveButton.setOnAction((event) ->
                rootViewManager.saveSettings(
                        filePathTextField.getText(),
                        randomColorsCheckBox.isSelected(),
                        notificationCheckBox.isSelected()));
    }

    /**
     * This is called when the user clicks on the "browse" button. It opens
     * up a finder/browser/explorer that allows the user to select a directory
     * graphically.
     *
     * There is also a textfield next to the browse button, should the user
     * prefer entering the directory manually.
     * @param directoryChooser
     */
    private void showChooser(DirectoryChooser directoryChooser) {
        directory = directoryChooser.showDialog(rootViewManager.getMainApp().getPrimaryStage());
        filePathTextField.setText(directory.toString());

        assert(directory.length() >= 0);
        LoggingService.getLogger().log(Level.INFO, "Selected directory: " + directory.toString());
    }

    /**
     * UX feature. Set the default button as cancel to prevent accidental
     * changing of settings.
     */
    public void focusOnButton() {
        cancelButton.setDefaultButton(true);
        cancelButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        cancelButton.setDefaultButton(false);
    }

    /**
     * This is called internally when opening the settings view.
     * This fills the text field with the file path as set in settings.json.
     * @param absolutePath
     */
    public void setAbsolutePathToDirectory(String absolutePath) {
        filePathTextField.setText(absolutePath);
    }

    /**
     * Set the notifications checkbox depending on whether notifications are enabled.
     * @param notificationsEnabled
     */
    public void setNotificationsEnabled(Boolean notificationsEnabled) {
        notificationCheckBox.setSelected(notificationsEnabled);
    }

    /**
     * Set the random colors checkbox depending on whether random colors are enabled.
     * @param randomColorsEnabled
     */
    public void setRandomColorsEnabled(Boolean randomColorsEnabled) {
        randomColorsCheckBox.setSelected(randomColorsEnabled);
    }

    /**
     * Set back-reference to rootViewManager.
     * @param rootViewManager
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
