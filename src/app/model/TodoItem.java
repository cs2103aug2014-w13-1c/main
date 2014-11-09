package app.model;
//@author A0116703N
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItem {
    // Data fields
	private String taskName;
	private Date startDate;
	private Date endDate;
	private String priority;
	private Boolean doneStatus;
	
	// Unique UUID tag
	private UUID itemID;
	
	// Task types
	public static final String EVENT = "Event";
	public static final String DEADLINE = "Deadline";
	public static final String FLOATING = "Floating";
	public static final String ENDLESS = "Endless";
	public static final String INVALID = "Invalid";
	
	// Priority levels
	public static final String HIGH = "1. High";
	public static final String MEDIUM = "2. Medium";
	public static final String LOW = "3. Low";
	
	// Constructors
	/**
	 * The standard constructor of a TodoItem. Upon calling this method,
	 * an immutable UUID will be assigned to the TodoItem.
	 * 
	 * @param newTaskName The task name of the new task. Can be null.
	 * @param newStartDate The start date of the new task. Can be null.
	 * @param newEndDate The end date of the new task. Can be null.
	 * @param newPriority The priority of the new task. Can be null. If not HIGH/MEDIUM/LOW, the new task's priority will be set at MEDIUM.
	 * @param newDoneStatus The done status of the new task. Can be null. If null, the new task's priority will be set at false.
	 */
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) {
	    if (newTaskName != null) {
	        this.taskName = newTaskName; 
	    }
	    
	    if (newStartDate != null) {
	        this.startDate = newStartDate;
	    }
	    
	    if (newEndDate != null) {
	        this.endDate = newEndDate;
	    }

	    this.priority = MEDIUM;
	    if (newPriority != null) {
	        if (newPriority.equals(HIGH) || newPriority.equals(LOW) || newPriority.equals(MEDIUM)) {
	            this.priority = newPriority;
	        }
	    }
	    
	    if (newDoneStatus != null) {
	        this.doneStatus = newDoneStatus; 
	    } else {
	        this.doneStatus = false;
	    }
	    
	    this.itemID = UUID.randomUUID();
	}
	
	/**
	 * Auxiliary constructor for a TodoItem, requiring only the start date
	 * and end date. The priority is assumed to be MEDIUM, while the done
	 * status is assumed to be false.
	 * 
	 * @param newTaskName The task name of the new item. Can be null.
	 * @param newStartDate The start date of the new item. Can be null.
	 * @param newEndDate The end date of the new item. Can be null.
	 */
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate) {
	    this(newTaskName, newStartDate, newEndDate, MEDIUM, false);
	}
	
	// Attribute getters and setters 
	/**
	 * @return The name of the task.
	 */
    public String getTaskName() {
        return taskName;
    }
    
    /**
     * @param newTaskName The new name of the task.
     */
    protected void setTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }
    
    /**
     * @return The start date of the task.
     */
    public Date getStartDate() {
        return startDate;
    }
    
    /**
     * @param newStartDate The new start date of the task.
     */
    protected void setStartDate(Date newStartDate) {
        this.startDate = newStartDate;
    }
    
    /**
     * @return The end date of the task.
     */
    public Date getEndDate() {
        return endDate;
    }
    
    /**
     * @param newEndDate The new end date of the task.
     */
    protected void setEndDate(Date newEndDate) {
        this.endDate = newEndDate;
    }
	
    /**
     * @return The priority level of the task.
     */
	public String getPriority() {
	    return this.priority;
	}
	
	/**
	 * @param newPriority The new priority of the task. Unlike the constructor, this
	 * must be HIGH/MEDIUM/LOW.
	 */
	protected void setPriority(String newPriority) {
	    assert newPriority != null;
	    assert (newPriority.equals(HIGH) || newPriority.equals(LOW) || newPriority.equals(MEDIUM));
	    
	    this.priority = newPriority;
	}

	/**
	 * @return The done status of the task.
	 */
    public Boolean isDone() {
        return doneStatus;
    }
    
    /**
     * @param newDoneStatus The new done status of the task.
     */
    protected void setDoneStatus(Boolean newDoneStatus) {
        assert newDoneStatus != null;
        
        this.doneStatus = newDoneStatus;
    }
    
    /**
     * The UUID associated with the task is immutable. It cannot be changed
     * once the TodoItem has been created.
     * 
     * @return The UUID tag of the task.
     */
    public UUID getUUID() {
        return itemID;
    }
	
    // Miscellaneous convenience methods requested by View and Controller
    /**
     * @return A custom formatted string representing the start date.
     */
	public String getStartDateString() {
	    return getDateString(startDate);
	}
	
	/**
	 * @return A custom formatted string representing the end date.
	 */
	public String getEndDateString() {
	    return getDateString(endDate);
	}
	
	/**
	 * Formats a Date object into a custom string representing that date.
	 * 
	 * @param date The date to be formatted.
	 * @return The custom formatted string.
	 */
	private String getDateString(Date date) {
	    if (date == null) {
	        return null;
	    }
	    
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month = theMonth(cal.get(Calendar.MONTH));
        int year = cal.get(Calendar.YEAR);
        String hoursAndMinutes = String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
        return day + " " + month + " " + year + " " + hoursAndMinutes;
    }

	/**
	 * Auxiliary method to help the getDateString method. Converts a month into
	 * its name.
	 * 
	 * @param month The month to be converted.
	 * @return The month's name.
	 */
    private static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month].toUpperCase();
    }

    /**
     * Returns the task type of the TodoItem. Possible types are FLOATING, DEADLINE, ENDLESS and EVENT.
     * 
     * @return The task type of the TodoItem.
     */
    public String getTodoItemType() {
        if (startDate == null) {
            if (endDate == null) {
                return FLOATING;
            } else {
                return DEADLINE;
            }
        } else {
            if (endDate == null) {
                return ENDLESS;
            } else {
                return EVENT;
            }
        }
    }
    
    /**
     * A convenience method to check whether a TodoItem is overdue based on its end date.
     * 
     * @return Whether the current task is overdue.
     */
    public boolean isOverdue() {
        if (endDate == null) {
            return false;
        }
        
        Date currentDate = new Date();
        return (currentDate.getTime() > endDate.getTime());
    }
    
}