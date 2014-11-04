package app.controllers;

import app.helpers.CommandObject;
import app.helpers.Keyword;
import app.model.TodoItem;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommandParser {
    private static CommandObject currentCommandObject;
    
    public static ArrayList<String> commandKeywords = new ArrayList<String>();
    private static ArrayList<String> addKeywords = new ArrayList<String>();
    private static ArrayList<String> updateKeywords = new ArrayList<String>();
    private static ArrayList<String> startDateKeywords = new ArrayList<String>();
    private static ArrayList<String> endDateKeywords = new ArrayList<String>();
    private static ArrayList<String> displayKeywords = new ArrayList<String>();
    private static ArrayList<String> searchKeywords = new ArrayList<String>();
    
    // String manipulation methods
   private int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    // Constructor and initialization
    protected CommandParser() {
        setKeywords();
    }

    protected CommandObject parseCommand(String inputString) {
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

 // Keywords parser
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
                
        searchKeywords.clear();
        searchKeywords.add("start");
        searchKeywords.add("end");
        searchKeywords.add("priority");
    }

    protected static ArrayList<Keyword> getKeywords(String inputString) {
        setKeywords();
        ArrayList<Keyword> currentKeywords = new ArrayList<Keyword>();
        String[] inputStringArray = inputString.trim().split(" ");
        int startIndex = 0;
        int endIndex = inputStringArray[0].length() - 1;;
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
        if (inputStringArray[0].equalsIgnoreCase("sort") || inputStringArray[0].equalsIgnoreCase("search")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (searchKeywords.contains(inputStringArray[i])) {
                    currentKeywords.add(new Keyword(startIndex, endIndex));
                }
                startIndex = endIndex + 2;
            }
        }
        return currentKeywords;
    }

    // Command word parser
    private String parseCommandWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            return inputString;
        } else {
            return inputString.substring(0, nextSpacePosition(inputString, 0));
        }
    }

    // Command string (string after the command word) parser
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

    // Date parser
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
                        currentCommandObject.setUpdateStartDate(true);
                        currentCommandObject.setEndIndex(i);
                    }
                    else {
                        currentCommandObject.setStartDate(getDate(dateKeyword, toBeParsed));
                        if (currentCommandObject.isUpdateStartDate()) {
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
                        currentCommandObject.setUpdateEndDate(true);
                    }
                    else {
                        currentCommandObject.setEndDate(getDate(dateKeyword, toBeParsed));
                        if (currentCommandObject.isUpdateEndDate()) {
                            currentCommandObject.setEndIndex(i);
                        }
                    }
                }
                endDateFlag = true;
            }
        }
    }
    
    private void checkDate() {
        if (currentCommandObject.getStartDate() != null && currentCommandObject.getEndDate() != null) {
            if (currentCommandObject.getEndDate().before(currentCommandObject.getStartDate())) {
                currentCommandObject.setCommandWord("dateError");
            }
        }
    }

    private Date getDate(String dateKeyword, String toBeParsed) {
        Parser dateParser = new Parser();
        List<Date> dateList = new ArrayList<Date>();
        List<DateGroup> groups = dateParser.parse(toBeParsed);
        for (DateGroup group : groups) {
            if (group.getDates() != null) {
                    dateList.addAll(group.getDates());
            }
        }
        if (!dateList.isEmpty()) {
            if (startDateKeywords.contains(dateKeyword)) {
                currentCommandObject.setUpdateStartDate(true);
            }
            else {
                currentCommandObject.setUpdateEndDate(true);
            }
            return dateList.get(0);
        } else {
            return null;
        }
    }

    // Priority parser
    private void setPriority() {
        if (currentCommandObject.getCommandWord().equalsIgnoreCase("add") || currentCommandObject.getCommandWord().equalsIgnoreCase("update"))
            for (int i = currentCommandObject.getInputStringArray().length - 1; i > 0; i--) {
                if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("priority")) {
                    if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("low")) {
                        currentCommandObject.setPriority(TodoItem.LOW);
                    }
                    else if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("medium")) {
                        currentCommandObject.setPriority(TodoItem.MEDIUM);
                    }
                    else if (currentCommandObject.getInputStringArray()[i + 1].equalsIgnoreCase("high")) {
                        currentCommandObject.setPriority(TodoItem.HIGH);
                    }
                    else {
                        currentCommandObject.setCommandString(
                                currentCommandObject.getCommandString().concat(" priority " + currentCommandObject.getInputStringArray()[i]));
                    }
                    currentCommandObject.setEndIndex(i);
                    break;
                }
            }
    }
}
