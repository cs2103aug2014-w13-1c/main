package app.model;
//@author A0116703N
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Class TodoItem
 * 
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItem {
	private String taskName;
	private Date startDate;
	private Date endDate;
	private String priority;
	private Boolean doneStatus;
	
	private UUID itemID;
	
	public static final String EVENT = "Event";
	public static final String DEADLINE = "Deadline";
	public static final String FLOATING = "Floating";
	public static final String ENDLESS = "Endless";
	public static final String INVALID = "Invalid";
	
	public static final String HIGH = "1. High";
	public static final String MEDIUM = "2. Medium";
	public static final String LOW = "3. Low";
	
	// Constructor
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
	
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate) {
	    this(newTaskName, newStartDate, newEndDate, MEDIUM, false);
	}
	
	// Attribute getters and setters 
    public String getTaskName() {
        return taskName;
    }
    
    protected void setTaskName(String newTaskName) {
        this.taskName = newTaskName;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    protected void setStartDate(Date newStartDate) {
        this.startDate = newStartDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    protected void setEndDate(Date newEndDate) {
        this.endDate = newEndDate;
    }
	
	public String getPriority() {
	    return this.priority;
	}
	
	protected void setPriority(String newPriority) {
	    assert newPriority != null;
	    assert (newPriority.equals(HIGH) || newPriority.equals(LOW) || newPriority.equals(MEDIUM));
	    
	    this.priority = newPriority;
	}

    public Boolean isDone() {
        return doneStatus;
    }
    
    protected void setDoneStatus(Boolean newDoneStatus) {
        assert newDoneStatus != null;
        
        this.doneStatus = newDoneStatus;
    }
    
    public UUID getUUID() {
        return itemID;
    }
	
    // Miscellaneous convenience methods requested by View and Controller
	public String getStartDateString() {
	    return getDateString(startDate);
	}
	
	public String getEndDateString() {
	    return getDateString(endDate);
	}
	
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

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month].toUpperCase();
    }

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
    
    public boolean isOverdue() {
        if (endDate == null) {
            return false;
        }
        
        Date currentDate = new Date();
        return (currentDate.getTime() > endDate.getTime());
    }
    
}