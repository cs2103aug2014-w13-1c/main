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
            if (newValue.length() > 0 && newValue.substring(0, 1).equals(" ")) {
                newValue = newValue.substring(1, newValue.length());
                inputField.replaceText(newValue);
            } else {
                inputField.setStyleSpans(0, keywordDetection(newValue));
            }
            if (inputField.getText().startsWith("search ")) {
                assert inputField.getText().length() > 6;
                searchState = true;
                instantSearch(inputField.getText().substring(7));
            } else if (inputField.getText().startsWith("update ")) {
                assert inputField.getText().length() > 6;
                highlightCell(inputField.getText().split(" ", -1)[1]);
            } else if (inputField.getText().startsWith("delete ")) {
                assert inputField.getText().length() > 6;
                highlightCell(inputField.getText().split(" ", -1)[1]);
            } else if (inputField.getText().startsWith("done ")) {
                assert inputField.getText().length() > 4;
                highlightCell(inputField.getText().split(" ", -1)[1]);
            } else if (inputField.getText().startsWith("undone ")) {
                assert inputField.getText().length() > 6;
                highlightCell(inputField.getText().split(" ", -1)[1]);
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
                String completedString = autoComplete(inputField.getText());
                if (completedString != null) {
                    inputField.replaceText(completedString + " ");
                }
            }
        });
    }

    private void highlightCell(String index) {
//        System.out.println("index: " + index);
        int highlightIndex;
        try {
            highlightIndex = Integer.parseInt(index);
        } catch (NumberFormatException e) {
            highlightIndex = -1;
        }
        if (highlightIndex > 0 ||
            highlightIndex <= rootViewManager.getTaskListViewManager().getTaskData().size()) {
//            System.out.println("focusing on: " + highlightIndex);
            rootViewManager.getTaskListViewManager().highlightCell(highlightIndex - 1);
        }
    }

    private void instantSearch(String query) {
        LoggingService.getLogger().log(Level.INFO, "Instant search query: \"" + query + "\"");
        ArrayList<TodoItem> results =
                rootViewManager.getMainApp().getTaskController().instantSearch(query);
        rootViewManager.getMainApp().getCommandController().updateView(results);
        if (results.isEmpty() && rootViewManager.getMainApp().getCommandController().getModelManager() != null) {
            rootViewManager.getTaskListViewManager().setEmptySearchPlaceholder();
        }
    }

    private String autoComplete(String command) {
        ArrayList<String> results = new ArrayList<String>();
        for (String keyword : CommandParser.commandKeywords) {
            if (command.length() < keyword.length() && command.equals(keyword.substring(0, command.length()))) {
                System.out.println("Match: " + keyword);
                results.add(keyword);
            }
        }
        if (results.size() == 0) {
            System.out.println("no keywords found");
            return null;
        } else if (results.size() == 1) {
            return results.get(0);
        } else {
            String multipleKeywords  = "Possible keywords: ";
            for (String result : results) {
                multipleKeywords = multipleKeywords + result + " ";
            }
            rootViewManager.getMainApp().showInfoNotification("AutoComplete", multipleKeywords);
            return null;
        }
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
