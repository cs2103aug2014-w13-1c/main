package app.model;
//@author A0116703N
import java.util.*;

/**
 * The model class to hold a list of TodoItems.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItemList {
	private ArrayList<TodoItem> todoItems;
	
	public TodoItemList() {
		todoItems = new ArrayList<TodoItem>();
	}
	
	public TodoItemList(ArrayList<TodoItem> newTodoItems) {
	    if (newTodoItems != null) {
	        this.todoItems = newTodoItems;
	    } else {
	        this.todoItems = new ArrayList<TodoItem>();
	    }
	}
	
	public ArrayList<TodoItem> getTodoItems() {
		return todoItems;
	}
	
    public ListIterator<TodoItem> getTodoItemsIterator() {
        return todoItems.listIterator();
    }
	
	public TodoItem getByUUID(UUID itemID) {
        for (TodoItem currentItem : todoItems) {
            if (currentItem.getUUID().equals(itemID)) {
                return currentItem;
            }
        }
	    return null;
	}
	
    public int searchIndexByUUID(UUID itemID) {
        for (int i = 0; i < todoItems.size(); i++) {
            TodoItem currentItem = todoItems.get(i);
            if (currentItem.getUUID().equals(itemID)) {
                return i;
            }
        }
        return -1;
    }

    public int countTodoItems() {
        return todoItems.size();
    }
	
	// CRUD
	public void addTodoItem(TodoItem newItem) {
		todoItems.add(newItem);
	}
	
	public void clearTodoItems() {
        todoItems = new ArrayList<TodoItem>();
	}

    public TodoItem deleteByUUID(UUID itemID) {
        for (int i = 0; i < todoItems.size(); i++) {
            if (todoItems.get(i).getUUID().equals(itemID)) {
                return todoItems.remove(i);
            } 
        }
        return null;
    }
}