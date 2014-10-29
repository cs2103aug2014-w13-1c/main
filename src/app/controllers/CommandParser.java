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
    private CommandObject currentCommandObject;
    
    public static ArrayList<String> commandKeywords = new ArrayList<String>();
    private static ArrayList<String> addUpdateKeywords = new ArrayList<String>();
    private static ArrayList<String> startDateKeywords = new ArrayList<String>();
    private static ArrayList<String> endDateKeywords = new ArrayList<String>();
    private static ArrayList<String> displayKeywords = new ArrayList<String>();
    
    // String manipulation methods
   private int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    // Constructor and initialization
    protected CommandParser() {
    }

    protected CommandObject parseCommand(String inputString) {
        currentCommandObject = new CommandObject();
        currentCommandObject.setInputString(inputString);
        currentCommandObject.setInputStringArray(inputString.trim().split(" "));
        currentCommandObject.setCommandWord(parseCommandWord(inputString));
        if (commandKeywords.contains(currentCommandObject.getCommandWord())) {
            currentCommandObject.setCommandString(parseCommandString(inputString));
            setDates();
            checkDate();
            setPriority();
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
        commandKeywords.add("search");
        commandKeywords.add("update");
        commandKeywords.add("help");
        commandKeywords.add("settings");
    	commandKeywords.add("saveto");
        commandKeywords.add("done");
        commandKeywords.add("undone");
        
    	startDateKeywords.clear();
        startDateKeywords.add("start");
        
        endDateKeywords.clear();
        endDateKeywords.add("end");
        endDateKeywords.add("due");
        endDateKeywords.add("by");
        
        addUpdateKeywords.clear();
        addUpdateKeywords.add("priority");
        addUpdateKeywords.addAll(startDateKeywords);
        addUpdateKeywords.addAll(endDateKeywords);
        
        displayKeywords.clear();
        displayKeywords.add("all");
        displayKeywords.add("done");
        displayKeywords.add("overdue");
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
        if (inputStringArray[0].equalsIgnoreCase("add") || inputStringArray[0].equalsIgnoreCase("update")) {
            for (int i = 1; i < inputStringArray.length; i++) {
                endIndex = startIndex + inputStringArray[i].length() - 1;
                if (addUpdateKeywords.contains(inputStringArray[i])) {
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
            while (i < currentCommandObject.getInputStringArray().length &&
                   !addUpdateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                result = result.concat(currentCommandObject.getInputStringArray()[i] + " ");
                i++;
            }
        }
        return result.trim();
    }

    // Date parser
    private void setDates() {
        for (int i = 0; i < currentCommandObject.getInputStringArray().length; i++) {
            if (startDateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                String toBeParsed = "";
                String dateKeyword = currentCommandObject.getInputStringArray()[i];
                i++;
                while (i < currentCommandObject.getInputStringArray().length &&
                       !addUpdateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                    toBeParsed = toBeParsed.concat(currentCommandObject.getInputStringArray()[i] + " ");
                    i++;
                }
                currentCommandObject.setStartDate(getDate(dateKeyword, toBeParsed.trim()));
                i--;
            }
            else if (endDateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                String toBeParsed = "";
                String dateKeyword = currentCommandObject.getInputStringArray()[i];
                i++;
                while (i < currentCommandObject.getInputStringArray().length &&
                       !addUpdateKeywords.contains(currentCommandObject.getInputStringArray()[i])) {
                    toBeParsed = toBeParsed.concat(currentCommandObject.getInputStringArray()[i] + " ");
                    i++;
                }
                currentCommandObject.setEndDate(getDate(dateKeyword, toBeParsed.trim()));
                i--;
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
            return dateList.get(0);
        } else {
            currentCommandObject.setCommandString(
                    currentCommandObject.getCommandString().concat(" " + dateKeyword + " " + toBeParsed));
            return null;
        }
    }

    // Priority parser
    private void setPriority() {
        for (int i = 0; i < currentCommandObject.getInputStringArray().length; i++) {
            if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("priority")) {
                i++;
                if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("low")) {
                    currentCommandObject.setPriority(TodoItem.LOW);
                }
                if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("medium")) {
                    currentCommandObject.setPriority(TodoItem.MEDIUM);
                }
                if (currentCommandObject.getInputStringArray()[i].equalsIgnoreCase("high")) {
                    currentCommandObject.setPriority(TodoItem.HIGH);
                }
            }
        }
    }
}
