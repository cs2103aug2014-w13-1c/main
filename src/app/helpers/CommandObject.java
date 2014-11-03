package app.helpers;

import java.util.Date;

/**
 * Created by jolly on 29/10/14.
 */
public class CommandObject {
    private String inputString;
    private String commandWord;
    private String commandString;
    private Date startDate;
    private Date endDate;
    private String priority;
    private String[] inputStringArray;

    public CommandObject() {
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
}
