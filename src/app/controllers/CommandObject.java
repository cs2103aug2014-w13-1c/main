package app.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import app.helpers.Keyword;

public class CommandObject {
    private String inputString;
    private String commandWord;
    private String commandString;
    private Date startDate;
    private Date endDate;
    
    private ArrayList<String> keywords = new ArrayList<String>();
    private ArrayList<String> startDateKeywords = new ArrayList<String>();
    private ArrayList<String> endDateKeywords = new ArrayList<String>();
    
    // String manipulation methods
    public static int nextSpacePosition(String inputString, int startIndex) {
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
    
    private void init() {
        commandWord = "";
        commandString = "";
        startDate = null;
        endDate = null;
        setKeywords();
    }
    
    private void setKeywords() {
        startDateKeywords.clear();
        startDateKeywords.add("start");
        
        endDateKeywords.clear();
        endDateKeywords.add("end");
        
        keywords.clear();
        keywords.add("add");
        keywords.add("delete");
        keywords.add("display");
        keywords.add("clear");
        keywords.add("exit");
        keywords.add("search");
        keywords.add("update");
        keywords.add("help");
        keywords.add("settings");
        keywords.addAll(startDateKeywords);
        keywords.addAll(endDateKeywords);
    }
    
    public CommandObject(String inputString) {
        init();
        this.inputString = inputString;
        setCommandWord(inputString);
        setCommandString(inputString);
        setDates(inputString);
    }
    
    public String getInputString() {
        return inputString;
    }
    
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
    
    private void setCommandString(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if (firstWordPos != -1) {
            String inputStringArray[] = inputString.trim().split(" ");
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
    
    private void setDates(String inputString) {
        String inputStringArray[] = inputString.trim().split(" ");
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
    
    private Date getDate(String toBeParsed) {
        StringTokenizer st = new StringTokenizer(toBeParsed);
        int date = Integer.valueOf(st.nextToken());
        int month = getMonth(st.nextToken());
        int year = Integer.valueOf(st.nextToken());
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        return cal.getTime();
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    
}
