package app.viewmanagers;

import app.helpers.InvalidInputException;
import app.helpers.Keyword;
import app.helpers.KeywordDetector;
import app.helpers.LoggingService;
import app.model.TodoItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;

/**
 * InputFieldViewManager
 *
 * Created by jolly on 24/9/14.
 */
public class InputFieldViewManager {

    private String lastCommand;
    private StyleClassedTextArea inputField;
    private RootViewManager rootViewManager;
    private Boolean searchState;

    public InputFieldViewManager() {
        lastCommand = "";
        inputField = new StyleClassedTextArea();
        inputField.setPrefHeight(100);
        inputField.getStylesheets().add("app/stylesheets/inputField.css");
        inputField.getStyleClass().add("input-field");
        inputField.setWrapText(true);
        searchState = false;

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            inputField.setStyleSpans(0, keywordDetection(newValue));
            if (inputField.getText().startsWith("search ")) {
                searchState = true;
                assert inputField.getText().length() > 6;
                String query = inputField.getText().substring(7);
                LoggingService.getLogger().log(Level.INFO, "Instant search query: \"" + query + "\"");
                ArrayList<TodoItem> results =
                        rootViewManager.getMainApp().getTaskController().instantSearch(query);
                rootViewManager.getMainApp().getCommandController().updateView(results);
                if (results.isEmpty()) {
                    rootViewManager.getTaskListViewManager().setEmptySearchPlaceholder();
                }
            } else {
                if (searchState) {
                    rootViewManager.getMainApp().getCommandController().updateView();
                    searchState = false;
                }
                rootViewManager.getTaskListViewManager().setUserGuidePlaceholder();
            }
        });

        inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                lastCommand = inputField.getText();
                try {
                    checkCommandLengthAndExecute(lastCommand);
                } catch (InvalidInputException e) {
                    LoggingService.getLogger().log(Level.INFO, "Invalid Input Exception: empty command");
                }
            } else if (event.getCode() == KeyCode.UP && !lastCommand.equals("")) {
                event.consume();
                inputField.replaceText(lastCommand);
            } else if (event.getCode() == KeyCode.TAB) {
                event.consume();
                System.out.println("TAB: \"" + inputField.getText() + "\"");
            }
        });
    }

    private String autoComplete(String command) {
        


        return null;
    }

    public void checkCommandLengthAndExecute(String command) throws InvalidInputException {
        if (command.length() == 0) {
            throw new InvalidInputException("empty command");
        } else {
            assert command.length() > 0;
            inputField.clear();
            LoggingService.getLogger().log(Level.INFO, "Command passed to CommandController: \"" + command + "\"");
            rootViewManager.getMainApp().getCommandController().parseCommand(command);
        }
    }

    private StyleSpans<Collection<String>> keywordDetection(String command) {
        ArrayList<Keyword> keywords = rootViewManager.getMainApp().getCommandController().parseKeywords(command);
        return KeywordDetector.getStyleSpans(keywords, command);
    }

    public StyleClassedTextArea getInputField() {
        return inputField;
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
