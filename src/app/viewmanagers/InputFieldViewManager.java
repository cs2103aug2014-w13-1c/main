//@author A0111987X
package app.viewmanagers;

import app.services.KeywordDetectorService;
import app.services.ParsingService;
import app.exceptions.InvalidInputException;
import app.helpers.Keyword;
import app.services.LoggingService;
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
 * View Manager for the inputField.
 * inputField is implemented using RichTextFX to allow for per character styling, which is then used
 * for keyword highlighting.
 *
 * InputFieldViewManager is also in charge of the following things:
 * - real-time search: task list is updated with search results as the user types, only works for task name queries,
 * and not with date queries
 * - real-time cell highlighting: highlights (and scrolls to if the command was entered by the user) the relevant cell
 * to indicate the task which the command will have an effect on (used for update/delete/done/undone)
 * - pressing the up key in the inputField populates it with the last entered command (regardless of its validity)
 * - auto-complete for keywords: using tab for auto-complete, which only works for the first word,
 * having multiple matches will pop up a notification telling the user the possible keywords that match
 */
public class InputFieldViewManager {

    private String lastCommand;
    private StyleClassedTextArea inputField;
    private RootViewManager rootViewManager;
    private Boolean searchState;
    private Boolean isFromButton;

    /**
     * Constructor for InputFieldViewManager, initialises the relevant components for InputFieldViewManager,
     * then adds two listeners, one for input changes and one for key presses.
     */
    public InputFieldViewManager() {
        initInputFieldViewManager();
        inputField.textProperty().addListener(this::keyListener);
        inputField.addEventFilter(KeyEvent.KEY_PRESSED, this::keyPressListener);
    }

    /**
     * Initialises the relevant components for InputFieldViewManager, such as the relevant styles from the css file.
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
     * Listener for changes in inputField.
     * Removes spaces (" ") if they are the first character in the command.
     * Calls the methods in charge of keyword highlighting, instant search and cell highlighting.
     *
     * @param observable    Observable entity which isn't used.
     * @param oldValue      Old value of the inputField.
     * @param newValue      New value of the inputField.
     */
    private void keyListener(javafx.beans.Observable observable, String oldValue, String newValue) {
        if (newValue.length() > 0 && newValue.substring(0, 1).equals(" ")) {
            // this doesn't allow for spaces (" ") as the first character in the command.
            inputField.replaceText(newValue.substring(1));
        } else {
            inputField.setStyleSpans(0, keywordDetection(newValue));
        }
        instantSearchAndHighlight();
    }

    /**
     * Listener for key presses in the inputField.
     * If the [ENTER] key is pressed, sends current user input to the CommandController for execution.
     * If the [UP] key is pressed, replaces the current text with the last entered command.
     * If the [TAB] key is pressed, calls auto-complete method.
     *
     * @param event KeyEvent
     */
    private void keyPressListener(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            if (!inputField.getText().equals("")) {
                lastCommand = inputField.getText();
            }
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
     * Calls relevant methods when a real-time search or cell highlight situation is detected.
     * This detection is purely based on the first word of the current user input.
     * Also resets the placeholder of the task list to the welcome splash page.
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
     * Highlights the task based on the identified index in the current user input.
     *
     * Scroll to task is only enabled if the user types the command that triggers cell highlighting,
     * if the inputField is populated using a button (done, update or delete), the task list does not
     * scroll to the highlighted task in order not to confuse the user.
     *
     * @param index Index of the task to highlight.
     * @param fromButton True if the command came from pressing a button rather than the user typing it.
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
     * For real-time searching for only task name queries. Does not apply to queries with dates.
     * Updates task list with real-time results based on search query.
     * Updates task list placeholder if there are no search results.
     *
     * @param query search query
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
     * Generates a list of matching keywords taken from the ParsingService based on the current user input.
     *
     * @param command user input
     * @return Only match for keyword or null if there is no match or more than one match.
     */
    private String autoComplete(String command) {
        ArrayList<String> results = new ArrayList<>();
        for (String keyword : ParsingService.commandKeywords) {
            if (command.length() < keyword.length() && command.equals(keyword.substring(0, command.length()))) {
                results.add(keyword);
            }
        }
        return autoCompleteResults(results);
    }

    /**
     * Based on the ArrayList of possible matches of keywords, if there is just one, it returns that keyword.
     * If there are more than one, it displays all possibilities in a notification and then returns null.
     * Also returns null if there are no results.
     *
     * @param results ArrayList of Strings which are the matching keywords.
     * @return Only match for keyword or null if there is no match or more than one match.
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
     * Checks to make sure the command is not empty, if not it throws an Invalid Input Exception.
     *
     * @param command user input
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
     * Gets the ArrayList of Keywords from CommandController (which passes it to ParsingService) to figure out
     * which keywords to highlight based on the current user input string. Passes the ArrayList to KeywordDetectorService
     * to get the StyleSpans collection for use with RichTextFX.
     *
     * @param command input string
     * @return StyleSpans collection with the information needed to determine which keywords to highlight.
     */
    private StyleSpans<Collection<String>> keywordDetection(String command) {
        ArrayList<Keyword> keywords = rootViewManager.getMainApp().getCommandController().parseKeywords(command);
        return KeywordDetectorService.getStyleSpans(keywords, command);
    }

    /**
     * Setter for isFromButton.
     * Used for cell highlighting to determine if the task list should scroll to the highlighted task.
     *
     * @param newIsFromButton isFromButton
     */
    protected void setFromButton(boolean newIsFromButton) {
        isFromButton = newIsFromButton;
    }

    /**
     * Getter for the inputField. Used mainly by rootViewManager to interact with the inputField.
     *
     * @return inputField
     */
    public StyleClassedTextArea getInputField() {
        return inputField;
    }

    /**
     * Setter for rootViewManager
     *
     * @param rootViewManager The rootViewManager that initialises InputFieldViewManager.
     */
    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }
}
