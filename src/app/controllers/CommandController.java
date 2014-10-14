package app.controllers;

import app.Main;
import app.model.TodoItem;
import app.model.TodoItemList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Class CommandController
 *
 * Skeleton based on jolly's CE2.
 *
 * @author ryan
 */

public class CommandController {
    protected enum COMMAND_TYPE {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID, SEARCH, UPDATE, HELP, SETTINGS
    }

    // Errors
    private final String ERROR_FILE_EMPTY = "Task list is empty.\n";
    private final String ERROR_WRONG_COMMAND_FORMAT = "Command error.\n";
    private final String ERROR_SEARCH_TERM_NOT_FOUND = "Search term not found.\n";

    // Messages
    private final String MESSAGE_ADD_COMPLETE = "added: \"%1$s\"\n";
    private final String MESSAGE_CLEAR_COMPLETE = "todo cleared\n";
    private final String MESSAGE_DELETE_COMPLETE = "deleted: \"%1$s\"\n";
    private final String MESSAGE_SEARCH_COMPLETE = "Search result(s):\n%1$s";
    private final String MESSAGE_UPDATE_COMPLETE = "updated: \"%1$s\"\n";

    // Class variables
    private TodoItemList taskList;
    private Main main;
    private ArrayList<TodoItem> currentList;

    // String manipulation methods
    protected void printString(String message) {
        System.out.print(message);
    }

    protected int firstSpacePosition(String command) {
        return command.indexOf(" ");
    }

    protected String getFirstWord(String command) {
        int firstWordPos = firstSpacePosition(command);
        if(firstWordPos == -1) {
            return command;
        }
        return command.substring(0, firstWordPos);
    }

    // Individual command methods
    // Add command method(s)
    protected String addNewLine(String command){
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeChecked = command.substring(firstWordPos + 1);
        timeParser(toBeChecked);
        return showInfoDialog(String.format(MESSAGE_ADD_COMPLETE, toBeChecked));
    }

    protected void timeParser(String toBeChecked) {
        StringTokenizer st = new StringTokenizer(toBeChecked);
        String toBeInserted = "";
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        boolean startFlag = false;
        boolean endFlag = false;
        while (st.hasMoreTokens()) {
            String check = st.nextToken();
            if (check.equalsIgnoreCase("start")) {
                startCalendar.setTime(getDate(st));
                startFlag = true;
            } else if (check.equalsIgnoreCase("end")) {
                endCalendar.setTime(getDate(st));
                endFlag = true;
            } else {
                toBeInserted = toBeInserted.concat(check + " ");
            }
        }
        addTodo(toBeInserted, startCalendar, endCalendar, startFlag, endFlag);
    }
    
    protected void addTodo(String toBeInserted, Calendar startCalendar, Calendar endCalendar, boolean startFlag, boolean endFlag) {
        if (endFlag) {
            if (startFlag) {
                taskList.addTodoItem(new TodoItem(toBeInserted, startCalendar.getTime(), endCalendar.getTime()));
            } else {
                taskList.addTodoItem(new TodoItem(toBeInserted, null, endCalendar.getTime()));
            }
        } else {
            taskList.addTodoItem(new TodoItem(toBeInserted, null, null));
        }
        resetTaskList();
    }
    
    protected Date getDate(StringTokenizer st) {
        int date = Integer.valueOf(st.nextToken());
        int month = getMonth(st.nextToken());
        int year = Integer.valueOf(st.nextToken());
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        return cal.getTime();
    }
    
    protected int getMonth(String monthInput) {
        String[] monthName = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};
        int month = -1;
        for (int i = 0; i < 12; i++) {
            if (monthName[i].equalsIgnoreCase(monthInput)) {
                month = i;
                break;
            }
        }
        return month;
    }
    
    // Display command method(s)
    protected String display(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos != -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        currentList = taskList.getTodoItems();
        main.getPrimaryStage().setTitle("wat do");
        updateView();
        return "displaying tasks\n";
    }

    protected ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList) {
        ObservableList<TodoItem> taskData = FXCollections.observableArrayList();
        int index = 1;
        for (TodoItem todo : todoList) {
            taskData.add(new TodoItem(index + ". " + todo.getTaskName(), todo.getStartDate(), todo.getEndDate()));
            index++;
        }
        return taskData;
    }

    // Clear command method(s)
    protected String clear() {
        taskList.clearTodoItems();
        resetTaskList();
        return MESSAGE_CLEAR_COMPLETE;
    }
    
    // Delete command method(s)
    protected String deleteEntry(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        if(isInt(command.substring(firstWordPos + 1))) {
            index = Integer.parseInt(command.substring(firstWordPos + 1)) - 1;
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeDeleted = todoList.get(index).getTaskName();
        taskList.deleteTodoItem(index);
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_DELETE_COMPLETE, toBeDeleted));
    }

    protected boolean isInt(String number) {
        try {
            Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    // Search command method(s)
    public ArrayList<TodoItem> instantSearch(String query) {
        ArrayList<TodoItem> results = new ArrayList<TodoItem>();
        for (TodoItem todo : taskList.getTodoItems()) {
            if (todo.getTaskName().toLowerCase().
                    contains(query.toLowerCase())) {
                results.add(todo);
            }
        }
        return results;
    }

    protected String search(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
        if (todoList.isEmpty()) {
            return showErrorDialog(String.format(ERROR_FILE_EMPTY));
        }
        ArrayList<TodoItem> results = instantSearch(command.substring(firstWordPos + 1));
        if (results.isEmpty()) {
            return showErrorDialog(ERROR_SEARCH_TERM_NOT_FOUND);
        } else {
            currentList = results;
            main.getPrimaryStage().setTitle("Search results for: \"" + command.substring(firstWordPos + 1) + "\"");
            updateView();
            return String.format(MESSAGE_SEARCH_COMPLETE, "updating task list view with results\n");
        }
//        String returnString = searchList(command.substring(firstWordPos + 1), todoList);
//        if (returnString.equals("") || returnString.equals(" ")) {
//            return showErrorDialog(ERROR_SEARCH_TERM_NOT_FOUND);
//        } else {
//            return showInfoDialog(String.format(MESSAGE_SEARCH_COMPLETE, returnString));
//        }
    }

//    protected String searchList(String query, ArrayList<TodoItem> todoList) {
//        String returnString = "";
//        int index = 1;
//        for (TodoItem todo : todoList) {
//            if (todo.getTaskName().toLowerCase().
//                    contains(query.toLowerCase())) {
//                returnString += index + ". " + todo.getTaskName() + "\n";
//            }
//            index++;
//        }
//        return returnString;
//    }

    // Update command method(s)
    protected String update(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        int index = -1;
        String secondCommand = command.substring(firstWordPos + 1);
        int secondWordPos = firstSpacePosition(secondCommand);
        if (secondWordPos == -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        if(isInt(secondCommand.substring(0, secondWordPos))) {
            index = Integer.parseInt(secondCommand.substring(0, secondWordPos)) - 1;
        }
        ArrayList<TodoItem> todoList = taskList.getTodoItems();
        if (index < 0 || index >= todoList.size()) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        String toBeUpdated = command.substring(firstWordPos + secondWordPos + 2);
        taskList.updateTodoItem(index, toBeUpdated, new Date(), new Date());
        resetTaskList();
        return showInfoDialog(String.format(MESSAGE_UPDATE_COMPLETE, toBeUpdated));
    }

    // Help method
    protected String help(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos != -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openHelp();
        return "showing help\n";
    }

    // Settings method
    protected String settings(String command) {
        int firstWordPos = firstSpacePosition(command);
        if (firstWordPos != -1) {
            return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
        main.getRootViewManager().openSettings();
        return "showing settings\n";
    }

    // Command processing methods
    protected COMMAND_TYPE determineCommandType(String commandWord) {
        if (commandWord.equalsIgnoreCase("add")) {
            return COMMAND_TYPE.ADD;
        } else if (commandWord.equalsIgnoreCase("delete")) {
            return COMMAND_TYPE.DELETE;
        } else if (commandWord.equalsIgnoreCase("display")) {
            return COMMAND_TYPE.DISPLAY;
        } else if (commandWord.equalsIgnoreCase("clear")) {
            return COMMAND_TYPE.CLEAR;
        } else if (commandWord.equalsIgnoreCase("exit")) {
            return COMMAND_TYPE.EXIT;
        } else if (commandWord.equalsIgnoreCase("search")) {
            return COMMAND_TYPE.SEARCH;
        } else if (commandWord.equalsIgnoreCase("update")) {
            return COMMAND_TYPE.UPDATE;
        } else if (commandWord.equalsIgnoreCase("help")) {
            return COMMAND_TYPE.HELP;
        } else if (commandWord.equalsIgnoreCase("settings")) {
            return COMMAND_TYPE.SETTINGS;
        } else {
            return COMMAND_TYPE.INVALID;
        }
    }

    protected String processCommand(String command) {
        String commandWord = getFirstWord(command);
        COMMAND_TYPE commandType = determineCommandType(commandWord);
        switch (commandType) {
            case ADD :
                return addNewLine(command);
            case DELETE :
                return deleteEntry(command);
            case DISPLAY :
                return display(command);
            case CLEAR :
                return clear();
            case EXIT :
                showInfoDialog("Bye!");
                System.exit(0);
            case SEARCH :
                return search(command);
            case UPDATE :
                return update(command);
            case HELP :
                return help(command);
            case SETTINGS :
                return settings(command);
            default :
                return showErrorDialog(ERROR_WRONG_COMMAND_FORMAT);
        }
    }

    public CommandController() {
        taskList = new TodoItemList();
        currentList = new ArrayList<TodoItem>();
    }

    public void parseCommand(String command) {
        printString("Parsing: \"" + command + "\"\n");
        printString(processCommand(command));
    }

    public void updateView() {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(currentList));
    }

    public void updateView(ArrayList<TodoItem> todoItems) {
        main.getRootViewManager().getTaskListViewManager().updateView(convertList(todoItems));
    }

    public ArrayList<TodoItem> getTaskList() {
        return taskList.getTodoItems();
    }

    public void setTaskList(ArrayList<TodoItem> todoList) {
        currentList = todoList;
    }

    public void resetTaskList() {
        main.getPrimaryStage().setTitle("wat do");
        setTaskList(getTaskList());
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

    public String showErrorDialog(String error) {
        main.showErrorDialog("Error", error);
        return error;
    }

    public String showInfoDialog(String message) {
        main.showInfoDialog("Information", message);
        return message;
    }
}
