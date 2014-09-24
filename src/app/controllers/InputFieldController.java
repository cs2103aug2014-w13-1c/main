package app.controllers;

import app.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputFieldController {

    private String lastCommand;

    @FXML
    private TextField inputField;

    // Reference to the main application
    private Main main;

    public InputFieldController() {

    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     */
    @FXML
    private void initialize() {
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            // System.out.println("TextField Text Changed (newValue: " + newValue + ")");
        });

        inputField.setOnAction((event) -> {
            lastCommand = inputField.getText();
            inputField.clear();
            System.out.println(lastCommand);
        });

        inputField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.TAB) {
                    System.out.println("TAB");
                    event.consume();
                    inputField.requestFocus();
                }
            }
        });
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
