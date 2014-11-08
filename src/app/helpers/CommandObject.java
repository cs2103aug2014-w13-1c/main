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
     * @param string
     */
    public void setInputString(String string) {
        inputString = string;
    }

    /**
     * Getter for inputString.
     *
     * @return
     */
    public String getInputString() {
        return inputString;
    }

    /**
     * Setter for inputStringArray.
     *
     * @param array
     */
    public void setInputStringArray(String[] array) {
        inputStringArray = array;
    }

    /**
     * Getter for inputStringArray.
     *
     * @return
     */
    public String[] getInputStringArray() {
        return inputStringArray;
    }

    /**
     * Setter for commandWord.
     *
     * @param word
     */
    public void setCommandWord(String word) {
        commandWord = word;
    }

    /**
     * Getter for commandWord.
     *
     * @return
     */
    public String getCommandWord() {
        return commandWord;
    }

    /**
     * Setter for commandString.
     *
     * @param string
     */
    public void setCommandString(String string) {
        commandString = string;
    }

    /**
     * Getter for commandString.
     *
     * @return
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Setter for startDate.
     *
     * @param date
     */
    public void setStartDate(Date date) {
        startDate = date;
    }

    /**
     * Getter for startDate.
     *
     * @return
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Setter for endDate.
     *
     * @param date
     */
    public void setEndDate(Date date) {
        endDate = date;
    }

    /**
     * Getter for endDate.
     *
     * @return
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Setter for priority.
     *
     * @param string
     */
    public void setPriority(String string) {
        priority = string;
    }

    /**
     * Getter for priority.
     *
     * @return
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Getter for startDateUpdated.
     *
     * @return
     */
    public boolean isStartDateUpdated() {
        return startDateUpdated;
    }

    /**
     * Setter for startDateUpdated.
     *
     * @param updated
     */
    public void setStartDateUpdated(boolean updated) {
        startDateUpdated = updated;
    }

    /**
     * Getter for endDateUpdated.
     *
     * @return
     */
    public boolean isEndDateUpdated() {
        return endDateUpdated;
    }

    /**
     * Setter for endDateUpdated.
     *
     * @param updated
     */
    public void setEndDateUpdated(boolean updated) {
        endDateUpdated = updated;
    }

    /**
     * Getter for endIndex.
     *
     * @return
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * Setter for endIndex.
     * Only updates endIndex if existing endIndex is larger than the new one.
     *
     * @param index
     */
    public void setEndIndex(int index) {
        if (endIndex > index) {
            endIndex = index;
        }
    }
}
