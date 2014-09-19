package app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Class TodoItemList
 * 
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItemList {
	private ArrayList<TodoItem> todoItems;
	
	public TodoItemList() {
		todoItems = new ArrayList<TodoItem>();
	}
	
	public ArrayList<TodoItem> getTodoItems() {
		return todoItems;
	}
	
	// CRUD
	public void addTodoItem(TodoItem newItem) {
		todoItems.add(newItem);
	}
	
	public TodoItem readTodoItem(int index) {
		try {
			return todoItems.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void updateTodoItem(int index, String itemAction, LocalDate startDate, LocalDate endDate) {
		TodoItem updatedItem = todoItems.get(index);
		
		if (itemAction != null) {
			updatedItem.setItemAction(itemAction);
		}
		if (startDate != null) {
			updatedItem.setStartDate(startDate);
		}
		if (endDate != null) {
			updatedItem.setEndDate(endDate);
		}
	}
	
	public TodoItem deleteTodoItem(int index) {
		try {
			return todoItems.remove(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
}