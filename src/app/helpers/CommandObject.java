//@author A0111987X
package app.helpers;

import java.util.Date;

/**
 * CommandObject is a class purely to encapsulate all the details related to a command.
 * The class is used only to store information and thus is mainly made out of
 * getter and setter methods.
 */
public class CommandObject {
    private String inputString;
    private String commandWord;
    private String commandString;
    private int endIndex;
    private Date startDate;
    private Date endDate;
    private boolean startDateKeyword;
    private boolean endDateKeyword;
    private String priority;
    private String[] inputStringArray;

    /**
     * Constructor for CommandObject.
     * Sets setStartDateUpdated and setEndDateUpdated to false.
     * Sets endIndex to 1000000000 (a very large number).
     */
    public CommandObject() {
        setStartDateKeyword(false);
        setEndDateKeyword(false);
        endIndex = 1000000000;
    }

    /**
     * Setter for inputString.
     * Used to store the whole user command.
     *
     * @param string inputString
     */
    public void setInputString(String string) {
        inputString = string;
    }

    /**
     * Getter for inputString.
     * Used to store the whole user command.
     *
     * @return inputString
     */
    public String getInputString() {
        return inputString;
    }

    /**
     * Setter for inputStringArray.
     * Used to store the whole user command that has been tokenized into an array.
     *
     * @param array inputStringArray
     */
    public void setInputStringArray(String[] array) {
        inputStringArray = array;
    }

    /**
     * Getter for inputStringArray.
     * Used to store the whole user command that has been tokenized into an array.
     *
     * @return inputStringArray
     */
    public String[] getInputStringArray() {
        return inputStringArray;
    }

    /**
     * Setter for commandWord.
     * Used to store the command keyword.
     *
     * @param word commandWord
     */
    public void setCommandWord(String word) {
        commandWord = word;
    }

    /**
     * Getter for commandWord.
     * Used to store the command keyword.
     *
     * @return commandWord
     */
    public String getCommandWord() {
        return commandWord;
    }

    /**
     * Setter for commandString.
     * Used to store the command parameter.
     *
     * @param string commandString
     */
    public void setCommandString(String string) {
        commandString = string;
    }

    /**
     * Getter for commandString.
     * Used to store the command parameter.
     *
     * @return commandString
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Setter for startDate.
     * Used to store the start date of the task.
     *
     * @param date startDate
     */
    public void setStartDate(Date date) {
        startDate = date;
    }

    /**
     * Getter for startDate.
     * Used to store the start date of the task.
     *
     * @return startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for endDate.
     * Used to store the end date of the task.
     *
     * @param date endDate
     */
    public void setEndDate(Date date) {
        endDate = date;
    }

    /**
     * Getter for endDate.
     * Used to store the end date of the task.
     *
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for priority.
     * Used to store the priority of the task.
     *
     * @param string priority
     */
    public void setPriority(String string) {
        priority = string;
    }

    /**
     * Getter for priority.
     * Used to store the priority of the task.
     *
     * @return priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Getter for startDateUpdated.
     * Used to determine if start date needs to be updated.
     *
     * @return startDateUpdated
     */
    public boolean hasStartDateKeyword() {
        return startDateKeyword;
    }

    /**
     * Setter for startDateUpdated.
     * Used to determine if start date needs to be updated.
     *
     * @param updated startDateUpdated
     */
    public void setStartDateKeyword(boolean keyword) {
        startDateKeyword = keyword;
    }

    /**
     * Getter for endDateUpdated.
     * Used to determine if end date needs to be updated.
     *
     * @return endDateUpdated
     */
    public boolean hasEndDateKeyword() {
        return endDateKeyword;
    }

    /**
     * Setter for endDateUpdated.
     * Used to determine if end date needs to be updated.
     *
     * @param updated endDateUpdated
     */
    public void setEndDateKeyword(boolean keyword) {
        endDateKeyword = keyword;
    }

    /**
     * Getter for endIndex.
     *
     * @return endIndex
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Setter for endIndex.
     * Used to store the end index of the commandString.
     * Only updates endIndex if the new one is smaller than the existing one.
     *
     * @param index endIndex
     */
    public void setEndIndex(int index) {
        if (endIndex > index) {
            endIndex = index;
        }
    }
}
