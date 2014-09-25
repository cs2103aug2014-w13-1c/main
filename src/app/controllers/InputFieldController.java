package app.controllers;

import app.Main;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.fxmisc.richtext.InlineCssTextArea;

public class InputFieldController {

    private String lastCommand;
    private InlineCssTextArea inputField;

    // Reference to the main application
    private Main main;

    public InputFieldController() {
        inputField = new InlineCssTextArea();
        inputField.setPrefHeight(100);
        inputField.getStylesheets().add("app/stylesheets/inputField.css");
        inputField.getStyleClass().add("input-field");
        inputField.setWrapText(true);

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("TextField Text Changed (newValue: " + newValue + ")");
            inputField.setStyle(0, inputField.getLength(), "-fx-fill: white;");
        });

        inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                lastCommand = inputField.getText();
                inputField.clear();
                main.getCommandController().parseCommand(lastCommand);
                main.getCommandController().updateView();
            } else if (event.getCode() == KeyCode.TAB) {
                event.consume();
                System.out.println("TAB: \"" + inputField.getText() + "\"");
            }
        });
    }

    public InlineCssTextArea getInputField() {
        return inputField;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param main
     */
    public void setMainApp(Main main) {
        this.main = main;

        // Add observable list data to the table
        // personTable.setItems(mainApp.getPersonData());
    }
}
