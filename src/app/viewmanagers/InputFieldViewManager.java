package app.viewmanagers;

import app.helpers.InvalidInputException;
import app.helpers.KeywordDetector;
import app.helpers.LoggingService;
import app.controllers.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.helpers.Keyword;
import app.model.TodoItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import org.fxmisc.richtext.StyleClassedTextArea;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

/**
 * Created by jolly on 24/9/14.
 *
 * computeHighlighting code taken from:
 * https://github.com/TomasMikula/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywords.java
 * will rewrite/refactor later
 */
public class InputFieldViewManager {

    private String lastCommand;
    private StyleClassedTextArea inputField;
    private RootViewManager rootViewManager;

    private final String[] KEYWORDS = new String[] {
        "add", "delete", "display", "clear", "exit", "search", "update", "help", "settings", "start", "end"
    };

    private final Pattern KEYWORD_PATTERN = Pattern.compile("\\b(" + String.join("|", KEYWORDS) + ")\\b");

    public InputFieldViewManager() {
        inputField = new StyleClassedTextArea();
        inputField.setPrefHeight(100);
        inputField.getStylesheets().add("app/stylesheets/inputField.css");
        inputField.getStyleClass().add("input-field");
        inputField.setWrapText(true);

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
//            inputField.setStyleSpans(0, computeHighlighting(newValue));
            inputField.setStyleSpans(0, keywordDetection(newValue));
            if (inputField.getText().startsWith("search ")) {
                assert inputField.getText().length() > 6;
                String query = inputField.getText().substring(7);
                LoggingService.getLogger().log(Level.INFO, "Instant search query: \"" + query + "\"");
                ArrayList<TodoItem> results =
                        rootViewManager.getMainApp().getCommandController().instantSearch(query);
                rootViewManager.getMainApp().getCommandController().updateView(results);
                if (results.isEmpty()) {
                    rootViewManager.getTaskListViewManager().setEmptySearchPlaceholder();
                }
            } else {
                LoggingService.getLogger().log(Level.INFO, "InputField text changed: \"" + newValue + "\"");
//                rootViewManager.getMainApp().getCommandController().updateView();
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
            }
//            else if (event.getCode() == KeyCode.TAB) {
//                event.consume();
//                System.out.println("TAB: \"" + inputField.getText() + "\"");
//            }
        });
    }

    private void checkCommandLengthAndExecute(String command) throws InvalidInputException {
        if (command.length() == 0) {

            throw new InvalidInputException("empty command");
        } else {
            assert command.length() > 0;
            inputField.clear();
            LoggingService.getLogger().log(Level.INFO, "Command passed to CommandController: \"" + command + "\"");
            rootViewManager.getMainApp().getCommandController().parseCommand(command);
            rootViewManager.getMainApp().getCommandController().updateView();
        }
    }

    private StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = KEYWORD_PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while(matcher.find()) {
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton("keyword"), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

    private StyleSpans<Collection<String>> keywordDetection(String command) {
        ArrayList<Keyword> keywords = CommandParser.parseKeywords(command);
        return KeywordDetector.getStyleSpans(keywords, command);
    }


    public StyleClassedTextArea getInputField() {
        return inputField;
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
