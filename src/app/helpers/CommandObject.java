package app.helpers;

import java.util.Date;

/**
 * Created by jolly on 29/10/14.
 */
public class CommandObject {
    private String inputString;
    private String commandWord;
    private String commandString;
    private int endIndex;
    private Date startDate;
    private Date endDate;
    private boolean updateStartDate;
    private boolean updateEndDate;
    private String priority;
    private String[] inputStringArray;

    public CommandObject() {
        setUpdateStartDate(false);
        setUpdateEndDate(false);
        endIndex = 1000000000;
    }

    public void setInputString(String string) {
        inputString = string;
    }

    public String getInputString() {
        return inputString;
    }

    public void setInputStringArray(String[] array) {
        inputStringArray = array;
    }

    public String[] getInputStringArray() {
        return inputStringArray;
    }

    public void setCommandWord(String word) {
        commandWord = word;
    }

    public String getCommandWord() {
            return commandWord;
        }

    public void setCommandString(String string) {
        commandString = string;
    }

    public String getCommandString() {
            return commandString;
        }

    public void setStartDate(Date date) {
        startDate = date;
    }

    public Date getStartDate() {
            return startDate;
        }

    public void setEndDate(Date date) {
        endDate = date;
    }

    public Date getEndDate() {
            return endDate;
        }

    public void setPriority(String string) {
        priority = string;
    }

    public String getPriority() {
        return priority;
    }

    public boolean isUpdateStartDate() {
        return updateStartDate;
    }

    public void setUpdateStartDate(boolean updateStartDate) {
        this.updateStartDate = updateStartDate;
    }

    public boolean isUpdateEndDate() {
        return updateEndDate;
    }

    public void setUpdateEndDate(boolean updateEndDate) {
        this.updateEndDate = updateEndDate;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        if (this.endIndex > endIndex) {
            this.endIndex = endIndex;
        }
    }
}
