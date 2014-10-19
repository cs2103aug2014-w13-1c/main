package app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import app.helpers.Keyword;

public class CommandParser {
    private String inputString;
    private String commandWord;
    private String commandString;
    private Date startDate;
    private Date endDate;
    private String priority;
    
    private String[] inputStringArray;
    
    private static ArrayList<String> keywords = new ArrayList<String>();
    private static ArrayList<String> commandKeywords = new ArrayList<String>();
    private static ArrayList<String> startDateKeywords = new ArrayList<String>();
    private static ArrayList<String> endDateKeywords = new ArrayList<String>();
    
    // String manipulation methods
   private int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    private int getMonth(String monthInput) {
        String[] monthName = {
            "january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"
        };
        int month = -1;
        for (int i = 0; i < 12; i++) {
            if (monthName[i].equalsIgnoreCase(monthInput)) {
                month = i;
                break;
            }
        }
        return month;
    }

    // Constructor and initialization
    public CommandParser(String inputString) {
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
        priority = "medium";
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
    	  
    	startDateKeywords.clear();
        startDateKeywords.add("start");
        
        endDateKeywords.clear();
        endDateKeywords.add("end");
        
        keywords.clear();
        keywords.add("priority");
        keywords.addAll(commandKeywords);
        keywords.addAll(startDateKeywords);
        keywords.addAll(endDateKeywords);
    }
    
    public static ArrayList<Keyword> getKeywords(String inputString) {
        setKeywords();
        ArrayList<Keyword> currentKeywords = new ArrayList<Keyword>();
        String[] inputStringArray = inputString.trim().split(" ");
        int startIndex = 0, endIndex = 0;
        for (int i = 0; i < inputStringArray.length; i++) {
            endIndex = startIndex + inputStringArray[i].length() - 1;
            if (keywords.contains(inputStringArray[i])) {
                currentKeywords.add(new Keyword(startIndex, endIndex));
            }
            startIndex = endIndex + 2;
        }
//        for (int i = 0; i < currentKeywords.size(); i++) {
//            System.out.println(currentKeywords.get(i).getStartIndex() + " " + currentKeywords.get(i).getEndIndex());
//        }
        return currentKeywords;
    }

    // Return the unparsed input string
    public String getInputString() {
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
    
    public String getCommandWord() {
        return commandWord;
    }
    
    // Command string (string after the command word) parser
    private void setCommandString(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if (firstWordPos != -1) {
            int i = 1;
            while (i < inputStringArray.length && !keywords.contains(inputStringArray[i])) {
                commandString = commandString.concat(inputStringArray[i] + " ");
                i++;
            }
        }
        commandString = commandString.trim();
    }
    
    public String getCommandString() {
        return commandString;
    }
    
    // Date parser
    private void setDates(String inputString) {
        for (int i = 0; i < inputStringArray.length; i++) {
            if (startDateKeywords.contains(inputStringArray[i])) {
                String toBeParsed = "";
                i++;
                while (i < inputStringArray.length && !keywords.contains(inputStringArray[i])) {
                    toBeParsed = toBeParsed.concat(inputStringArray[i] + " ");
                    i++;
                }
                startDate = getDate(toBeParsed.trim());
                i--;
            }
            else if (endDateKeywords.contains(inputStringArray[i])) {
                String toBeParsed = "";
                i++;
                while (i < inputStringArray.length && !keywords.contains(inputStringArray[i])) {
                    toBeParsed = toBeParsed.concat(inputStringArray[i] + " ");
                    i++;
                }
                endDate = getDate(toBeParsed.trim());
                i--;
            }
        }
    }
    
    private void checkDate() {
        if (startDate != null) {
            if (startDate.before(new Date())) {
                commandWord = "dateError";
            }
        }
        if (endDate != null) {
            if (endDate.before(new Date())) {
                commandWord = "dateError";
            }
        }
        if (startDate != null && endDate != null) {
            if (endDate.before(startDate)) {
                commandWord = "dateError";
            }
        }
    }
    
    private Date getDate(String toBeParsed) {
        try {
            DateFormat df = new SimpleDateFormat("dd MMMM yyyy HH:mm");
            System.out.println(df.parseObject(toBeParsed));
            return df.parse(toBeParsed);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
        }
        return null;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    // Priority parser
    private void setPriority() {
        for (int i = 0; i < inputStringArray.length; i++) {
            if (inputStringArray[i].equalsIgnoreCase("priority")) {
                i++;
                priority = inputStringArray[i];
            }
        }
    }
    
    public String getPriority() {
        return priority;
    }
}
