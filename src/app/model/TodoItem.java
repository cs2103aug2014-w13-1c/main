package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.Date;

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
	public TodoItem(String taskName, Date startDate, Date endDate) {
		if (taskName != null) { 
			this.taskName = new SimpleStringProperty(taskName);
		} else {
			this.taskName = null;
		}
		
		if (startDate != null) {
			this.startDate = new SimpleObjectProperty<Date>(startDate);
		} else {
			startDate = null;
		}
		if (endDate != null) {
			this.endDate = new SimpleObjectProperty<Date>(endDate);
		} else {
			endDate = null;
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
	
	public void setTaskName(String taskName) {
		if (taskName != null) {
			this.taskName.set(taskName);
		} else {
			this.taskName = new SimpleStringProperty(taskName);
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
	
	public void setStartDate(Date startDate) {
		if (startDate != null) {
			this.startDate.set(startDate);
		} else {
			this.startDate = new SimpleObjectProperty<Date>(startDate);
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
	
	public void setEndDate(Date endDate) {
		if (endDate != null) {
			this.endDate.set(endDate);
		} else {
			this.endDate = new SimpleObjectProperty<Date>(endDate);
		}
	}
	
	public void setEndDateProperty(ObjectProperty<Date> endDate) {
		this.endDate = endDate;
	}
	 
}