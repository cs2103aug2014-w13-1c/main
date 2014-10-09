package app.controllers;

/**
 * Class CommandParser
 * 
 * This class is used to parse the command string and return command object.
 *
 * @author ryan
 */

public class CommandParser {
    protected enum COMMAND_TYPE {
        ADD, DELETE, DISPLAY, CLEAR, EXIT, INVALID, SEARCH, UPDATE
    }
    
    // String manipulation methods
    protected void printString(String message) {
        System.out.print(message);
    }

    protected int nextSpacePosition(String inputString, int startIndex) {
        return inputString.indexOf(" ", startIndex);
    }

    protected String getFirstWord(String inputString) {
        int firstWordPos = nextSpacePosition(inputString, 0);
        if(firstWordPos == -1) {
            return inputString;
        }
        return inputString.substring(0, firstWordPos);
    }
    
    // Command parser method
    public CommandObject commandParser(String inputString) {
        String commandWord = getFirstWord(inputString);
        int toBeInsertedIndex = nextSpacePosition(inputString, 0);
        int startDateIndex = inputString.indexOf("start");
        int startDateEndIndex = nextSpacePosition(inputString, startDateIndex);
        int endDateIndex = inputString.indexOf("end");
        int endDateEndIndex = nextSpacePosition(inputString, endDateIndex);
        String toBeInserted;
        String startDate;
        String endDate;
        if (startDateIndex > 0 && endDateIndex > 0) {
            if (startDateIndex < endDateIndex) {
                toBeInserted = inputString.substring(nextSpacePosition(inputString, 0), startDateIndex - 1);
                startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex), endDateIndex - 1);
                endDate = inputString.substring(nextSpacePosition(inputString, endDateIndex), inputString.length() - 1);
                return new CommandObject(commandWord, toBeInsertedIndex, toBeInserted, startDateIndex, startDate, endDateIndex, endDate);
            }
            else {
                toBeInserted = inputString.substring(nextSpacePosition(inputString, 0), endDateIndex - 1);
                startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex), inputString.length() - 1);
                endDate = inputString.substring(nextSpacePosition(inputString, endDateIndex), startDateIndex - 1);
                return new CommandObject(commandWord, toBeInsertedIndex, toBeInserted, startDateIndex, startDate, endDateIndex, endDate);
            }
        }
        else if (startDateIndex > 0 && endDateIndex == -1) {
            toBeInserted = inputString.substring(nextSpacePosition(inputString, 0), startDateIndex - 1);
            startDate = inputString.substring(nextSpacePosition(inputString, startDateIndex), inputString.length() - 1);
        }
        else if (startDateIndex == -1 && endDateIndex > 0) {
            
        }
        else {
            
        }
    }
}