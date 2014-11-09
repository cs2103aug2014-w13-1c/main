//@author A0111987X
package app.viewmanagers;

import app.controllers.CommandParser;
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

/* inputField.css
.input-field {
    -fx-font-family: Helvetica;
    -fx-font-size: 30px;
    -fx-background-color: #C7F464;
    -fx-padding: 15px;
}

.keyword {
    -fx-fill: #C44D58;
    -fx-font-weight: bold;
}
 */

/**
 * InputFieldViewManager
 */
public class InputFieldViewManager {

    private String lastCommand;
    private StyleClassedTextArea inputField;
    private RootViewManager rootViewManager;
    private Boolean searchState;
    private Boolean isFromButton;

    /**
     *
     */
    public InputFieldViewManager() {
        initInputFieldViewManager();
        inputField.textProperty().addListener(this::keyListener);
        inputField.addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressListener);
    }

    /**
     *
     */
    private void initInputFieldViewManager() {
        lastCommand = "";
        inputField = new StyleClassedTextArea();
        inputField.setPrefHeight(100);
        inputField.getStylesheets().add("app/stylesheets/inputField.css");
        inputField.getStyleClass().add("input-field");
        inputField.setWrapText(true);
        searchState = false;
        isFromButton = false;
    }

    /**
     *
     *
     * @param observable
     * @param oldValue
     * @param newValue
     */
    private void keyListener(javafx.beans.Observable observable, String oldValue, String newValue) {
        if (newValue.length() > 0 && newValue.substring(0, 1).equals(" ")) {
            newValue = newValue.substring(1, newValue.length());
            inputField.replaceText(newValue);
        } else {
            inputField.setStyleSpans(0, keywordDetection(newValue));
        }
        instantSearchAndHighlight();
    }

    /**
     *
     *
     * @param event
     */
    private void keyPressListener(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            lastCommand = inputField.getText();
            try {
                checkCommandLengthAndExecute(lastCommand);
            } catch (InvalidInputException e) {
                LoggingService.getLogger().log(Level.INFO, "Invalid Input Exception: empty command");
            }
        } else if (event.getCode() == KeyCode.UP && !lastCommand.equals("") &&
                !lastCommand.equals(inputField.getText())) {
            event.consume();
            inputField.replaceText(lastCommand);
        } else if (event.getCode() == KeyCode.TAB) {
            event.consume();
            String completedString = autoComplete(inputField.getText());
            if (completedString != null) {
                inputField.replaceText(completedString + " ");
            }
        }
    }

    /**
     *
     */
    private void instantSearchAndHighlight() {
        if (inputField.getText().startsWith("search ")) {
            searchState = true;
            instantSearch(inputField.getText().substring(7));
        } else if (inputField.getText().startsWith("update ") ||
                inputField.getText().startsWith("delete ") ||
                inputField.getText().startsWith("undone ") ||
                inputField.getText().startsWith("done ")) {
            highlightCell(inputField.getText().split(" ", -1)[1], isFromButton);
        } else {
            if (searchState) {
                rootViewManager.getMainApp().getCommandController().updateView();
                searchState = false;
            }
            rootViewManager.getTaskListViewManager().setUserGuidePlaceholder();
        }
    }

    /**
     *
     *
     * @param index
     * @param fromButton
     */
    private void highlightCell(String index, boolean fromButton) {
        int highlightIndex;
        try {
            highlightIndex = Integer.parseInt(index);
        } catch (NumberFormatException e) {
            highlightIndex = -1;
        }
        if (highlightIndex > 0 &&
                highlightIndex <= rootViewManager.getTaskListViewManager().getTaskData().size()) {
            if (!fromButton) {
                rootViewManager.getTaskListViewManager().scrollTo(highlightIndex - 1);
            }
            rootViewManager.getTaskListViewManager().highlight(highlightIndex - 1);
        }
    }

    /**
     *
     *
     * @param query
     */
    private void instantSearch(String query) {
        LoggingService.getLogger().log(Level.INFO, "Instant search query: \"" + query + "\"");
        ArrayList<TodoItem> results =
                rootViewManager.getMainApp().getTaskController().instantSearch(query);
        rootViewManager.getMainApp().getCommandController().updateView(results);
        if (results.isEmpty() && rootViewManager.getMainApp().getCommandController().getModelManager() != null) {
            rootViewManager.getTaskListViewManager().setEmptySearchPlaceholder();
        }
    }

    /**
     *
     *
     * @param command
     * @return
     */
    private String autoComplete(String command) {
        ArrayList<String> results = new ArrayList<String>();
        for (String keyword : CommandParser.commandKeywords) {
            if (command.length() < keyword.length() && command.equals(keyword.substring(0, command.length()))) {
                results.add(keyword);
            }
        }
        return autoCompleteResults(results);
    }

    /**
     *
     *
     * @param results
     * @return
     */
    private String autoCompleteResults(ArrayList<String> results) {
        if (results.size() == 0) {
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            String multipleKeywords = "Possible keywords: ";
            for (String result : results) {
                multipleKeywords = multipleKeywords + result + " ";
            }
            rootViewManager.getMainApp().showInfoNotification("AutoComplete", multipleKeywords);
            return null;
        }
    }

    /**
     *
     *
     * @param command
     * @throws InvalidInputException
     */
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

    /**
     *
     *
     * @param command
     * @return
     */
    private StyleSpans<Collection<String>> keywordDetection(String command) {
        ArrayList<Keyword> keywords = rootViewManager.getMainApp().getCommandController().parseKeywords(command);
        return KeywordDetector.getStyleSpans(keywords, command);
    }

    /**
     *
     *
     * @param newIsFromButton
     */
    protected void setFromButton(boolean newIsFromButton) {
        this.isFromButton = newIsFromButton;
    }

    /**
     *
     *
     * @return
     */
    public StyleClassedTextArea getInputField() {
        return inputField;
    }

    /**
     *
     *
     * @param rootViewManager
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
