package app.controllers;

import app.helpers.Keyword;
import app.model.TodoItem;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.ParseLocation;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommandParser {
    private String inputString;
    private String commandWord;
    private String commandString;
    private Date startDate;
    private Date endDate;
    private String priority;
    
    private String[] inputStringArray;
    
    public static ArrayList<String> commandKeywords = new ArrayList<String>();
    private static ArrayList<String> addUpdateKeywords = new ArrayList<String>();
    private static ArrayList<String> startDateKeywords = new ArrayList<String>();
    private static ArrayList<String> endDateKeywords = new ArrayList<String>();
    private static ArrayList<String> displayKeywords = new ArrayList<String>();
    
    // String manipulation methods
   private int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

//    private int getMonth(String monthInput) {
//        String[] monthName = {
//            "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"
//        };
//        int month = -1;
//        for (int i = 0; i < 12; i++) {
//            if (monthName[i].equalsIgnoreCase(monthInput)) {
//                month = i;
//                break;
//            }
//        }
//        return month;
//    }

    // Constructor and initialization
    protected CommandParser(String inputString) {
        init();
        this.inputString = inputString;
        this.inputStringArray = inputString.trim().split(" ");
        setCommandWord(inputString);
        if (commandKeywords.contains(commandWord)) {
            setCommandString(inputString);
            setDates(inputString);
            checkDate();
            setPriority();
        }
    }

    private void init() {
        commandWord = "";
        commandString = "";
        startDate = null;
        endDate = null;
        priority = null;
        setKeywords();
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
//        for (int i = 0; i < currentKeywords.size(); i++) {
//            System.out.println(currentKeywords.get(i).getStartIndex() + " " + currentKeywords.get(i).getEndIndex());
//        }
        return currentKeywords;
    }

    // Return the unparsed input string
    protected String getInputString() {
        return inputString;
    }
    
    // Command word parser
    private void setCommandWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            commandWord = inputString;
        }
        else {
            commandWord = inputString.substring(0, nextSpacePosition(inputString, 0));
        }
    }
    
    protected String getCommandWord() {
        return commandWord;
    }
    
    // Command string (string after the command word) parser
    private void setCommandString(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if (firstWordPos != -1) {
            int i = 1;
            while (i < inputStringArray.length && !addUpdateKeywords.contains(inputStringArray[i])) {
                commandString = commandString.concat(inputStringArray[i] + " ");
                i++;
            }
        }
        commandString = commandString.trim();
    }
    
    protected String getCommandString() {
        return commandString;
    }
    
    // Date parser
    private void setDates(String inputString) {
        for (int i = 0; i < inputStringArray.length; i++) {
            if (startDateKeywords.contains(inputStringArray[i])) {
                String toBeParsed = "";
                String dateKeyword = inputStringArray[i];
                i++;
                while (i < inputStringArray.length && !addUpdateKeywords.contains(inputStringArray[i])) {
                    toBeParsed = toBeParsed.concat(inputStringArray[i] + " ");
                    i++;
                }
                startDate = getDate(dateKeyword, toBeParsed.trim());
                i--;
            }
            else if (endDateKeywords.contains(inputStringArray[i])) {
                String toBeParsed = "";
                String dateKeyword = inputStringArray[i];
                i++;
                while (i < inputStringArray.length && !addUpdateKeywords.contains(inputStringArray[i])) {
                    toBeParsed = toBeParsed.concat(inputStringArray[i] + " ");
                    i++;
                }
                endDate = getDate(dateKeyword, toBeParsed.trim());
                i--;
            }
        }
    }
    
    private void checkDate() {
        if (startDate != null && endDate != null) {
            if (endDate.before(startDate)) {
                commandWord = "dateError";
            }
        }
    }

    @SuppressWarnings("unused")
    private Date getDate(String dateKeyword, String toBeParsed) {
        Parser dateParser = new Parser();
        List<Date> dateList = new ArrayList<Date>();
        List<DateGroup> groups = dateParser.parse(toBeParsed);
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
            int line = group.getLine();
            int column = group.getPosition();
            String matchingValue = group.getText();
            String syntaxTree = group.getSyntaxTree().toStringTree();
            Map<String, List<ParseLocation>> parseMap = group.getParseLocations();
            boolean isRecurreing = group.isRecurring();
            Date recursUntil = group.getRecursUntil();

            /* if any Dates are present in current group then add them to dateList */
            if (group.getDates() != null) {
                    dateList.addAll(group.getDates());
            }
        }
        if (!dateList.isEmpty()) {
            return dateList.get(0);
        }
        else {
            commandString = commandString.concat(" " + dateKeyword + " " + toBeParsed);
            return null;
        }
    }
    
    protected Date getStartDate() {
        return startDate;
    }
    
    protected Date getEndDate() {
        return endDate;
    }
    
    // Priority parser
    private void setPriority() {
        for (int i = 0; i < inputStringArray.length; i++) {
            if (inputStringArray[i].equalsIgnoreCase("priority")) {
                i++;
                if (inputStringArray[i].equalsIgnoreCase("low")) {
                    priority = TodoItem.LOW;
                }
                if (inputStringArray[i].equalsIgnoreCase("medium")) {
                    priority = TodoItem.MEDIUM;
                }
                if (inputStringArray[i].equalsIgnoreCase("high")) {
                    priority = TodoItem.HIGH;
                }
            }
        }
    }
    
    protected String getPriority() {
        return priority;
    }
}
