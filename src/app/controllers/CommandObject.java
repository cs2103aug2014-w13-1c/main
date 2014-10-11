package app.controllers;

/**
 * Class CommandObject
 * 
 * The model class for command object containing the indexes of key words from the command string
 * and their respective key words.
 *
 * @author ryan
 */

public class CommandObject {
    private int inputStringIndex;
    private int startDateIndex;
    private int startDateEndIndex;
    private int endDateIndex;
    private int endDateEndIndex;
    private String commandWord;
    private String inputString;
    private String startDate;
    private String endDate;
    
    public CommandObject() {
    		inputStringIndex = 0;
        startDateIndex = -1;
        startDateEndIndex = -1;
        endDateIndex = -1;
        endDateEndIndex = -1;
        commandWord = "";
        inputString = "";
        startDate = "";
        endDate = "";
    }
    
    public CommandObject(String newCommandWord, int newInputStringIndex, String newInputString,
                         int newStartDateIndex, int newStartDateEndIndex, String newStartDate,
                         int newEndDateIndex, int newEndDateEndIndex, String newEndDate) {
        this.inputStringIndex = newInputStringIndex;
        this.startDateIndex = newStartDateIndex;
        this.startDateEndIndex = newStartDateEndIndex;
        this.endDateIndex = newEndDateIndex;
        this.endDateEndIndex = newEndDateEndIndex;
        this.commandWord = newCommandWord;
        this.inputString = newInputString;
        this.startDate = newStartDate;
        this.endDate = newEndDate;
    }
    
    public int getInputStringIndex() {
        return inputStringIndex;
    }
    
    public int getStartDateIndex() {
        return startDateIndex;
    }
    
    public int getStartDateEndIndex() { 
        return startDateEndIndex;
    }
    
    public int getEndDateIndex() {
        return endDateIndex;
    }
    
    public int getEndDateEndIndex() { 
        return endDateEndIndex;
    }
    
    public String getCommand() {
        return commandWord;
    }
    
    public String getInputString() {
        return inputString;
    }
    
    public String getStartDate() {
        return startDate;
    }
    
    public String getEndDate() {
        return endDate;
    }
    
    public void setInputStringIndex(int newCommandIndex) {
        this.inputStringIndex = newCommandIndex;
    }
    
    public void setStartDateIndex(int newStartIndex) {
        this.startDateIndex= newStartIndex;
    }
    
    public void setStartDateEndIndex(int newStartDateEndIndex) {
        this.startDateEndIndex = newStartDateEndIndex;
    }
    
    public void setEndDateIndex(int newEndDateIndex) {
        this.endDateIndex = newEndDateIndex;
    }
    
    public void setEndDateEndIndex(int newEndDateEndIndex) {
        this.endDateEndIndex = newEndDateEndIndex;
    }

    public void setCommandWord(String newCommandWord) {
        this.commandWord = newCommandWord;
    }
    
    public void setInputString(String newInputString) {
        this.inputString = newInputString;
    }
    
    public void setStartDate(String newStartDate) {
        this.startDate = newStartDate;
    }
    
    public void setEndDate(String newEndDate) {
        this.endDate = newEndDate;
    }
}