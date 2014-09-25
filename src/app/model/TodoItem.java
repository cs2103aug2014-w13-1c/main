package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Date;
import java.util.Calendar;

/**
 * Class TodoItem
 * 
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItem {
	private StringProperty taskName;
	private ObjectProperty<Date> startDate;
	private ObjectProperty<Date> endDate;
	
	public static final String EVENT = "Event";
	public static final String DEADLINE = "Deadline";
	public static final String FLOATING = "Floating";
	public static final String INVALID = "Invalid";
	
	// Constructors
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate) {
		if (newTaskName != null) { 
			this.taskName = new SimpleStringProperty(newTaskName);
		} else {
			this.taskName = null;
		}
		
		if (newStartDate != null) {
			this.startDate = new SimpleObjectProperty<Date>(newStartDate);
		} else {
			this.startDate = null;
		}
		if (newEndDate != null) {
			this.endDate = new SimpleObjectProperty<Date>(newEndDate);
		} else {
			this.endDate = null;
		}
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
				return INVALID;
			} else {
				return EVENT;
			}
		}
	}
	
	public String getStartDateString() {
	    return getDateString(getStartDate());
	}
	
	public String getEndDateString() {
	    return getDateString(getEndDate());
	}
	
	private String getDateString(Date date) {
	    Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        String month = theMonth(cal.get(Calendar.MONTH));
        int year = cal.get(Calendar.YEAR);
        return day + " " + month + " " + year;
    }

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month].toUpperCase();
    }
	
	// RESTful (lel)
	// Not recommended to use the set[..]Property methods. Just use the set methods.
	public String getTaskName() {
		if (taskName != null) {
			return taskName.get();
		}
		return null;
	}
	
	public StringProperty gettaskNameProperty() {
		return taskName;
	}
	
	public void setTaskName(String newTaskName) {
	    if (newTaskName == null) {
	        this.taskName = null;
	    } else {
	        if (this.taskName != null) {
	            this.taskName.set(newTaskName);
	        } else {
	            this.taskName = new SimpleStringProperty(newTaskName);
		    }
		}
	}
	
	public void setTaskNameProperty(StringProperty taskName) {
		this.taskName = taskName;
	}
	
	public Date getStartDate() {
		if (startDate != null) {
			return startDate.get();
		}
		return null;
	}
	
	public ObjectProperty<Date> getStartDateProperty() {
		return startDate;
	}
	
	public void setStartDate(Date newStartDate) {
	    if (newStartDate == null) {
	        this.startDate = null;
	    } else {
	        if (this.startDate != null) {
	            this.startDate.set(newStartDate);
	        } else {
		        this.startDate = new SimpleObjectProperty<Date>(newStartDate);
		    }
		}
	}
	
	public void setStartDateProperty(ObjectProperty<Date> startDate) {
		this.startDate = startDate;
	}
	
	public Date getEndDate() {
		if (endDate != null) {
			return endDate.get();
		}
		return null;
	}
	
	public ObjectProperty<Date> getEndDateProperty() {
		return endDate;
	}
	
	public void setEndDate(Date newEndDate) {
	    if (newEndDate == null) {
	        this.endDate = null;
	    } else {
	        if (this.endDate != null) {
	            this.endDate.set(newEndDate);
	        } else {
		        this.endDate = new SimpleObjectProperty<Date>(newEndDate);
		    }
		}
	}
	
	public void setEndDateProperty(ObjectProperty<Date> endDate) {
		this.endDate = endDate;
	}
	 
}