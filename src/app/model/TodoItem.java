package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.Comparator;
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
	private String priority;
	private Boolean doneStatus;
	
	private Long epochTag;
	
	public static final String EVENT = "Event";
	public static final String DEADLINE = "Deadline";
	public static final String FLOATING = "Floating";
	public static final String ENDLESS = "Endless";
	public static final String INVALID = "Invalid";
	
	public static final String HIGH = "3. High";
	public static final String MEDIUM = "2. Medium";
	public static final String LOW = "1. Low";
	
	// Constructors
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate) {
        if (newTaskName != null) { 
            this.taskName = new SimpleStringProperty(newTaskName);
        } else {
            this.taskName = null;
        }
        
        // Error check, to see if start date is later than end date
        if (newStartDate != null && newEndDate != null) {
            if (newStartDate.getTime() > newEndDate.getTime()) {
                throw new IllegalArgumentException();
            }
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
        
        this.priority = MEDIUM;
        
        this.doneStatus = false;
        
        this.epochTag = (new Date()).getTime();
    }
	
	public TodoItem(String newTaskName, Date newStartDate, Date newEndDate, String newPriority) {
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
		
		if (newPriority != null && (newPriority.equals(HIGH) || newPriority.equals(LOW))) {
		    this.priority = newPriority;
		} else {
		    this.priority = MEDIUM;
		}
		
		this.doneStatus = false;
		
		this.epochTag = (new Date()).getTime();
	}

    public TodoItem(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) {
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
        
        if (newPriority != null && (newPriority.equals(HIGH) || newPriority.equals(LOW))) {
            this.priority = newPriority;
        } else {
            this.priority = MEDIUM;
        }
        
        if (newDoneStatus != null) {
            this.doneStatus = newDoneStatus;
        } else {
            this.doneStatus = false;
        }
        
        this.epochTag = (new Date()).getTime();
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
	
	public String getPriority() {
	    return this.priority;
	}
	
	public void setPriority(String newPriority) {
	    if (newPriority != null && (newPriority.equals(HIGH) || newPriority.equals(LOW) || newPriority.equals(MEDIUM))) {
            this.priority = newPriority;
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
    
    public boolean isOverdue() {
        Date endDate = getEndDate();
        
        if (endDate == null) {
            return false;
        }
        Date currentDate = new Date();
        if (currentDate.getTime() > endDate.getTime()) {
            return true;
        }
        
        return false;
    }
    
    public Long getEpochTag() {
        return epochTag;
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
	
	public boolean isDone() {
	    return doneStatus;
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
	
	public void setDoneStatus(Boolean newDoneStatus) {
	    this.doneStatus = newDoneStatus;
	}
}