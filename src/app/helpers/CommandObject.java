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
    private boolean startDateUpdated;
    private boolean endDateUpdated;
    private String priority;
    private String[] inputStringArray;

    /**
     * Constructor for CommandObject.
     * Sets setStartDateUpdated and setEndDateUpdated to false.
     * Sets endIndex to 1000000000 (a very large number).
     */
    public CommandObject() {
        setStartDateUpdated(false);
        setEndDateUpdated(false);
        endIndex = 1000000000;
    }

    /**
     * Setter for inputString.
     *
     * @param string inputString
     */
    public void setInputString(String string) {
        inputString = string;
    }

    /**
     * Getter for inputString.
     *
     * @return inputString
     */
    public String getInputString() {
        return inputString;
    }

    /**
     * Setter for inputStringArray.
     *
     * @param array inputStringArray
     */
    public void setInputStringArray(String[] array) {
        inputStringArray = array;
    }

    /**
     * Getter for inputStringArray.
     *
     * @return inputStringArray
     */
    public String[] getInputStringArray() {
        return inputStringArray;
    }

    /**
     * Setter for commandWord.
     *
     * @param word commandWord
     */
    public void setCommandWord(String word) {
        commandWord = word;
    }

    /**
     * Getter for commandWord.
     *
     * @return commandWord
     */
    public String getCommandWord() {
        return commandWord;
    }

    /**
     * Setter for commandString.
     *
     * @param string commandString
     */
    public void setCommandString(String string) {
        commandString = string;
    }

    /**
     * Getter for commandString.
     *
     * @return commandString
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Setter for startDate.
     *
     * @param date startDate
     */
    public void setStartDate(Date date) {
        startDate = date;
    }

    /**
     * Getter for startDate.
     *
     * @return startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for endDate.
     *
     * @param date endDate
     */
    public void setEndDate(Date date) {
        endDate = date;
    }

    /**
     * Getter for endDate.
     *
     * @return endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for priority.
     *
     * @param string priority
     */
    public void setPriority(String string) {
        priority = string;
    }

    /**
     * Getter for priority.
     *
     * @return priority
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Getter for startDateUpdated.
     *
     * @return startDateUpdated
     */
    public boolean isStartDateUpdated() {
        return startDateUpdated;
    }

    /**
     * Setter for startDateUpdated.
     *
     * @param updated startDateUpdated
     */
    public void setStartDateUpdated(boolean updated) {
        startDateUpdated = updated;
    }

    /**
     * Getter for endDateUpdated.
     *
     * @return endDateUpdated
     */
    public boolean isEndDateUpdated() {
        return endDateUpdated;
    }

    /**
     * Setter for endDateUpdated.
     *
     * @param updated endDateUpdated
     */
    public void setEndDateUpdated(boolean updated) {
        endDateUpdated = updated;
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
