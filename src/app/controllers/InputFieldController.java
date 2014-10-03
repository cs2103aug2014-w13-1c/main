package app.controllers;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.Main;
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
public class InputFieldController {

    private String lastCommand;
    private StyleClassedTextArea inputField;

    // Reference to the main application
    private Main main;

    private final String[] KEYWORDS = new String[] {
        "add", "delete", "display", "clear", "exit", "search", "update"
    };

    private final Pattern KEYWORD_PATTERN = Pattern.compile("\\b(" + String.join("|", KEYWORDS) + ")\\b");

    public InputFieldController() {
        inputField = new StyleClassedTextArea();
        inputField.setPrefHeight(100);
        inputField.getStylesheets().add("app/stylesheets/inputField.css");
        inputField.getStyleClass().add("input-field");
        inputField.setWrapText(true);

        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
//            System.out.println("TextField Text Changed (newValue: " + newValue + ")");
//            inputField.setStyle(0, inputField.getLength(), "-fx-fill: black;");
            inputField.setStyleSpans(0, computeHighlighting(newValue));
            if (inputField.getText().startsWith("search ")) {
                String query = inputField.getText().substring(7);
                System.out.println("query: " + query);
                main.getCommandController().updateView(main.getCommandController().instantSearch(query));
            } else {
                main.getCommandController().updateView();
            }
        });

        inputField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                if (!inputField.getText().equals("")) {
                    lastCommand = inputField.getText();
                    inputField.clear();
                    main.getCommandController().parseCommand(lastCommand);
                    main.getCommandController().updateView();
                }
            } else if (event.getCode() == KeyCode.TAB) {
                event.consume();
                System.out.println("TAB: \"" + inputField.getText() + "\"");
            }
        });
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


    public StyleClassedTextArea getInputField() {
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
