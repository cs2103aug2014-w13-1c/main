package app.services;
//@author A0114914L

import app.helpers.CommandObject;
import app.helpers.Keyword;
import app.model.TodoItem;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParsingService {
    private static CommandObject currentCommandObject;
    private static Parser dateParser;
    
    public static ArrayList<String> commandKeywords = new ArrayList<String>();
    private static ArrayList<String> addKeywords = new ArrayList<String>();
    private static ArrayList<String> updateKeywords = new ArrayList<String>();
    private static ArrayList<String> startDateKeywords = new ArrayList<String>();
    private static ArrayList<String> endDateKeywords = new ArrayList<String>();
    private static ArrayList<String> displayKeywords = new ArrayList<String>();
    private static ArrayList<String> searchKeywords = new ArrayList<String>();
    private static ArrayList<String> sortKeywords = new ArrayList<String>();
    
    /**
     * This is a string manipulation method to get the index of the next space position from a string.
     * 
     * @param inputString
     * @param startIndex
     * @return index of the next space position.
     */
    private int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    // Constructor and initialization
    /**
     * Constructor for ParsingService. It sets and initialises keywords. It also initialise the date parser.
     */
    public ParsingService() {
        setKeywords();
        dateParser = new Parser();
    }

    /**
     * The parsing command method. It creates a CommandObject. And set all the parameters of the CommandObject
     * from the inputString.
     * 
     * @param inputString
     * @return a CommandObject to be used by CommandController (which will be passed to ActionController)
     */
    public CommandObject parseCommand(String inputString) {
        currentCommandObject = new CommandObject();
        currentCommandObject.setInputString(inputString);
        currentCommandObject.setInputStringArray(inputString.trim().split(" "));
        currentCommandObject.setEndIndex(currentCommandObject.getInputStringArray().length);
        currentCommandObject.setCommandWord(parseCommandWord(inputString));
        if (commandKeywords.contains(currentCommandObject.getCommandWord())) {
            setDates();
            checkDate();
            setPriority();
            currentCommandObject.setCommandString(parseCommandString(inputString));
        }
        return currentCommandObject;
    }

    // Keyword(s) methods
    /**
     * Initialisation of the keywords.
     */
    private static void setKeywords() {
        commandKeywords.clear();
        commandKeywords.add("add");
        commandKeywords.add("delete");
        commandKeywords.add("display");
        commandKeywords.add("clear");
        commandKeywords.add("exit");
        commandKeywords.add("sort");
        commandKeywords.add("search");
        commandKeywords.add("update");
        commandKeywords.add("help");
        commandKeywords.add("settings");
    	commandKeywords.add("saveto");
        commandKeywords.add("done");
        commandKeywords.add("undone");
        commandKeywords.add("undo");
        commandKeywords.add("redo");
        
    	startDateKeywords.clear();
        startDateKeywords.add("start");
        startDateKeywords.add("begin");
        startDateKeywords.add("from");
        
        endDateKeywords.clear();
        endDateKeywords.add("end");
        endDateKeywords.add("due");
        endDateKeywords.add("by");
        
        addKeywords.clear();
        addKeywords.add("priority");
        addKeywords.addAll(startDateKeywords);
        addKeywords.addAll(endDateKeywords);
        
        updateKeywords.clear();
        updateKeywords.add("remove");
        updateKeywords.addAll(addKeywords);
        
        displayKeywords.clear();
        displayKeywords.add("all");
        displayKeywords.add("done");
        displayKeywords.add("overdue");
        displayKeywords.add("undone");
                
        searchKeywords.clear();
        searchKeywords.addAll(startDateKeywords);
        searchKeywords.addAll(endDateKeywords);
        
        sortKeywords.clear();
        sortKeywords.addAll(addKeywords);
        sortKeywords.add("name");
    }

    /**
     * This method is used to get the keywords from the current inputString. It is used by the InputFieldViewManager
     * to detect the keywords for keywords highlighting.
     * 
     * @param inputString
     * @return an ArrayList of Keyword
     */
    public static ArrayList<Keyword> getKeywords(String inputString) {
        ArrayList<Keyword> currentKeywords = new ArrayList<Keyword>();
        String[] inputStringArray = inputString.trim().split(" ");
        int startIndex = 0;
        int endIndex = inputStringArray[0].length() - 1;
        if (commandKeywords.contains(inputStringArray[0])) {
            currentKeywords.add(new Keyword(0, endIndex));
            startIndex = endIndex + 2;
        }
        if (inputStringArray[0].equalsIgnoreCase("add")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (addKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        if (inputStringArray[0].equalsIgnoreCase("update")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (updateKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        if (inputStringArray[0].equalsIgnoreCase("display")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (displayKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        if (inputStringArray[0].equalsIgnoreCase("search")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (searchKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        if (inputStringArray[0].equalsIgnoreCase("sort")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (sortKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        return currentKeywords;
    }

    // Parsing method(s)
    /**
     * This method will parse the command word from the input string.
     * 
     * @param inputString
     * @return the command word
     */
    private String parseCommandWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            return inputString;
        } else {
            return inputString.substring(0, nextSpacePosition(inputString, 0));
        }
    }

    /**
     * This method will parse the command string (argument string) from the input string.
     * 
     * @param inputString
     * @return the command string
     */
    private String parseCommandString(String inputString) {
        String result = "";
        int firstWordPos = nextSpacePosition(inputString, 0);
        if (firstWordPos != -1) {
            int i = 1;
            while (i < currentCommandObject.getEndIndex()) {
                result = result.concat(currentCommandObject.getInputStringArray()[i] + " ");
                i++;
            }
        }

        return result.trim();
    }

    /**
     * This method will parse the date from the input string if available. It will set the dates of the
     * CommandObject to the given value (by the input string) or leave it as null.
     */
    private void setDates() {
        boolean startDateFlag = false;
        boolean endDateFlag = false;
        for (int i = currentCommandObject.getInputStringArray().length - 1; i > 0 ; i--) {
            if (startDateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                if (!startDateFlag) {
                    String toBeParsed = "";
                    String dateKeyword = currentCommandObject.getInputStringArray()[i];
                    int j = i + 1;
                    while (j < currentCommandObject.getInputStringArray().length &&
                           !addKeywords.contains(currentCommandObject.getInputStringArray()[j])) {
                        toBeParsed = toBeParsed.concat(currentCommandObject.getInputStringArray()[j] + " ");
                        j++;
                    }
                    if (toBeParsed.trim().equals("remove")) {
                        currentCommandObject.setStartDateKeyword(true);
                        currentCommandObject.setEndIndex(i);
                    }
                    else {
                        currentCommandObject.setStartDate(getDate(dateKeyword, toBeParsed));
                        if (currentCommandObject.hasStartDateKeyword()) {
                            currentCommandObject.setEndIndex(i);
                        }
                    }
                }
                startDateFlag = true;
            }
            else if (endDateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                if (!endDateFlag) {
                    String toBeParsed = "";
                    String dateKeyword = currentCommandObject.getInputStringArray()[i];
                    int j = i + 1;
                    while (j < currentCommandObject.getInputStringArray().length &&
                           !addKeywords.contains(currentCommandObject.getInputStringArray()[j])) {
                        toBeParsed = toBeParsed.concat(currentCommandObject.getInputStringArray()[j] + " ");
                        j++;
                    }
                    if (toBeParsed.trim().equals("remove")) {
                        currentCommandObject.setEndDateKeyword(true);
                        currentCommandObject.setEndIndex(i);
                    }
                    else {
                        currentCommandObject.setEndDate(getDate(dateKeyword, toBeParsed));
                        if (currentCommandObject.hasEndDateKeyword()) {
                            currentCommandObject.setEndIndex(i);
                        }
                    }
                }
                endDateFlag = true;
            }
        }
    }

    /**
     * This method will check that the end date of the CommandObject is not before the start date.
     */
    private void checkDate() {
        if (currentCommandObject.getStartDate() != null && currentCommandObject.getEndDate() != null) {
            if (currentCommandObject.getEndDate().before(currentCommandObject.getStartDate())) {
                currentCommandObject.setCommandWord("dateError");
            }
        }
    }

    /**
     * This method uses natty library to parse the date from toBeParsed string. Hence, it can parse date with 
     * a natural language input.
     * 
     * @param dateKeyword
     * @param toBeParsed
     * @return the date
     */
    private Date getDate(String dateKeyword, String toBeParsed) {
        List<Date> dateList = new ArrayList<Date>();
        List<DateGroup> groups = dateParser.parse(toBeParsed);
        for (DateGroup group : groups) {
            if (group.getDates() != null) {
                    dateList.addAll(group.getDates());
            }
        }
        if (!dateList.isEmpty()) {
            if (startDateKeywords.contains(dateKeyword)) {
                currentCommandObject.setStartDateKeyword(true);
            }
            else {
                currentCommandObject.setEndDateKeyword(true);
            }
            return dateList.get(0);
        } else {
            return null;
        }
    }

    /**
     * This method will parse the priority and set the priority of the CommandObject accordingly.
     */
    private void setPriority() {
        if (currentCommandObject.getCommandWord().equalsIgnoreCase("add") || currentCommandObject.getCommandWord().equalsIgnoreCase("update")) {
            for (int i = currentCommandObject.getInputStringArray().length - 1; i > 0; i--) {
                if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("priority") && i != currentCommandObject.getInputStringArray().length - 1) {
                    if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("low")) {
                        currentCommandObject.setPriority(TodoItem.LOW);
                        currentCommandObject.setEndIndex(i);
                    }
                    else if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("medium") || currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("med")) {
                        currentCommandObject.setPriority(TodoItem.MEDIUM);
                        currentCommandObject.setEndIndex(i);
                    }
                    else if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("high")) {
                        currentCommandObject.setPriority(TodoItem.HIGH);
                        currentCommandObject.setEndIndex(i);
                    }
                    
                    break;
                }
            }
        }
    }
}
