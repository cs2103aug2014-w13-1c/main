package app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;

/**
 * Class TodoItem
 * 
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItem {
	private StringProperty itemAction;
	private ObjectProperty<LocalDate> startDate;
	private ObjectProperty<LocalDate> endDate;
	
	public final String EVENT = "Event";
	public final String DEADLINE = "Deadline";
	public final String FLOATING = "Floating";
	public final String INVALID = "Invalid";
	
	// Constructors
	public TodoItem(String itemAction, LocalDate startDate, LocalDate endDate) {
		if (itemAction != null) { 
			this.itemAction = new SimpleStringProperty(itemAction);
		} else {
			this.itemAction = null;
		}
		
		if (startDate != null) {
			this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		} else {
			startDate = null;
		}
		if (endDate != null) {
			this.endDate = new SimpleObjectProperty<LocalDate>(endDate);
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
	
	// RESTful
	// Not recommended to use the set[..]Property methods. Just use the set methods.
	public String getItemAction() {
		if (itemAction != null) {
			return itemAction.get();
		}
		return null;
	}
	
	public StringProperty getItemActionProperty() {
		return itemAction;
	}
	
	public void setItemAction(String itemAction) {
		if (itemAction != null) {
			this.itemAction.set(itemAction);
		} else {
			this.itemAction = new SimpleStringProperty(itemAction);
		}
	}
	
	public void setItemActionProperty(StringProperty itemAction) {
		this.itemAction = itemAction;
	}
	
	public LocalDate getStartDate() {
		if (startDate != null) {
			return startDate.get();
		}
		return null;
	}
	
	public ObjectProperty<LocalDate> getStartDateProperty() {
		return startDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		if (startDate != null) {
			this.startDate.set(startDate);
		} else {
			this.startDate = new SimpleObjectProperty<LocalDate>(startDate);
		}
	}
	
	public void setStartDateProperty(ObjectProperty<LocalDate> startDate) {
		this.startDate = startDate;
	}
	
	public LocalDate getEndDate() {
		if (endDate != null) {
			return endDate.get();
		}
		return null;
	}
	
	public ObjectProperty<LocalDate> getEndDateProperty() {
		return endDate;
	}
	
	public void setEndDate(LocalDate endDate) {
		if (endDate != null) {
			this.endDate.set(endDate);
		} else {
			this.endDate = new SimpleObjectProperty<LocalDate>(endDate);
		}
	}
	
	public void setEndDateProperty(ObjectProperty<LocalDate> endDate) {
		this.endDate = endDate;
	}
	 
}