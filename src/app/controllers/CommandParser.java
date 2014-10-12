package app.controllers;

/**
 * Class CommandParser
 * 
 * This class is used to parse the command string and return command object.
 *
 * @author ryan
 */

public class CommandParser {    
    // String manipulation methods
    protected void printString(String message) {
        System.out.print(message);
    }

    
    
    protected static int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    protected static String getFirstWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            return inputString;
        }
        return inputString.substring(0, firstWordPos);
    }
    
    // Command parser method
    public static CommandObject parse(String inputString) {
        String commandWord = getFirstWord(inputString);
        int inputStringIndex = nextSpacePosition(inputString, 0) + 1;
        int startDateIndex = inputString.indexOf("start");
        int startDateEndIndex = nextSpacePosition(inputString, startDateIndex);
        int endDateIndex = inputString.indexOf("end");
        int endDateEndIndex = nextSpacePosition(inputString, endDateIndex);
        String toBeInserted = "";
        String startDate = "";
        String endDate = "";
        if (inputStringIndex != -1) {
	        if (startDateIndex > 0 && endDateIndex > 0) {
	            if (startDateIndex < endDateIndex) {
	                toBeInserted = inputString.substring(inputStringIndex, startDateIndex - 1);
	                startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex) + 1, endDateIndex - 1);
	                endDate = inputString.substring(nextSpacePosition(inputString, endDateIndex) + 1);
	            }
	            else {
	                toBeInserted = inputString.substring(inputStringIndex, endDateIndex - 1);
	                startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex) + 1);
	                endDate = inputString.substring(nextSpacePosition(inputString, endDateIndex) + 1, startDateIndex - 1);
	            }
	        }
	        else if (startDateIndex > 0 && endDateIndex == -1) {
	            toBeInserted = inputString.substring(inputStringIndex, startDateIndex - 1);
	            startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex) + 1);
	        }
	        else if (startDateIndex == -1 && endDateIndex > 0) {
	        	toBeInserted = inputString.substring(inputStringIndex, endDateIndex - 1);
	        	endDate = inputString.substring(nextSpacePosition(inputString, endDateIndex) + 1);
	        }
	        else {
	        	toBeInserted = inputString.substring(inputStringIndex);
	        }
        }
        return new CommandObject(commandWord, inputStringIndex, toBeInserted, startDateIndex, startDateEndIndex, startDate, endDateIndex, endDateEndIndex, endDate);
    }
}