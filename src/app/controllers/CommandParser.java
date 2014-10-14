package app.controllers;

import app.helpers.Keyword;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Class CommandParser
 * 
 * This class consists of methods to parse the command string.
 *
 * @author ryan
 */

public class CommandParser {    
    // String manipulation methods
    public static int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    protected static int getMonth(String monthInput) {
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
    
    // Index getter  method(s)
    protected static int getStartDateStartIndex(String inputString) {
        return inputString.indexOf("start");
    }
    
    protected static int getStartDateEndIndex(String inputString) {
        if (getStartDateStartIndex(inputString) == -1) {
            return -1;
        }
        return nextSpacePosition(" ", getStartDateStartIndex(inputString));
    }
    
    protected static int getEndDateStartIndex(String inputString) {
        return inputString.indexOf("end");
    }
    
    protected static int getEndDateEndIndex(String inputString) {
        if (getEndDateStartIndex(inputString) == -1) {
            return -1;
        }
        return nextSpacePosition(" ", getEndDateStartIndex(inputString));
    }
    
    // Parser method(s)
    public static String getCommandWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            return inputString;
        }
        return inputString.substring(0, nextSpacePosition(inputString, 0));
    }
    
    public static String getCommandString(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if (firstWordPos == -1) {
            return "";
        }
        StringTokenizer st = new StringTokenizer(inputString.substring(0, firstWordPos));
        String commandWord = "";
        while (st.hasMoreTokens()) {
            String nextWord = st.nextToken();
            if (nextWord.equals("start") || nextWord.equals("end")) {
                break;
            }
            commandWord = commandWord.concat(nextWord + " ");
        }
        return commandWord.trim();       
    }
    
    public static Date getDate(String toBeParsed) {
        StringTokenizer st = new StringTokenizer(toBeParsed.substring(getStartDateEndIndex(toBeParsed)));
        int date = Integer.valueOf(st.nextToken());
        int month = getMonth(st.nextToken());
        int year = Integer.valueOf(st.nextToken());
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, date);
        return cal.getTime();
    }
    
    public static ArrayList<Keyword> parseKeywords(String inputString) {
    	ArrayList<Keyword> currentKeywords = new ArrayList<Keyword>();
    	currentKeywords.add(new Keyword(0, nextSpacePosition(" ", 0)));
    	currentKeywords.get(currentKeywords.size() - 1).setWord(getCommandString(inputString));
    	if (getStartDateStartIndex(inputString) != -1) {
            currentKeywords.add(new Keyword(getStartDateStartIndex(inputString), getStartDateEndIndex(inputString)));
            currentKeywords.get(currentKeywords.size() - 1).setWord("start");
        }
    	if (getEndDateStartIndex(inputString) != -1) {
    	    currentKeywords.add(new Keyword(getEndDateStartIndex(inputString), getEndDateEndIndex(inputString)));
    	    currentKeywords.get(currentKeywords.size() - 1).setWord("end");
        }
    	return currentKeywords;
    }
}