package app.model;
//@author A0116703N

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.UUID;

/**
 * The model class to hold a list of TodoItems. Has utility methods like searching
 * for a TodoItem by its UUID, but aside from that, functions like an ArrayList of
 * TodoItems.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItemList {
    // Internal ArrayList
	private ArrayList<TodoItem> todoItems;
	
	/**
	 * Standard constructor. Initializes an empty TodoItemList. 
	 */
	public TodoItemList() {
		todoItems = new ArrayList<TodoItem>();
	}
	
	/**
	 * Constructor used for replacing mass amounts of task data. Mostly used for
	 * undo/redo functionalities.
	 * 
	 * @param newTodoItems The new task data to be used as the program data.
	 */
	public TodoItemList(ArrayList<TodoItem> newTodoItems) {
	    if (newTodoItems != null) {
	        this.todoItems = newTodoItems;
	    } else {
	        this.todoItems = new ArrayList<TodoItem>();
	    }
	}
	
	// Data exposure methods
	/**
	 * @return The internal ArrayList of TodoItems.
	 */
	public ArrayList<TodoItem> getTodoItems() {
		return todoItems;
	}
	
	/**
	 * @return The iterator of the internal ArrayList of TodoItems.
	 */
    public ListIterator<TodoItem> getTodoItemsIterator() {
        return todoItems.listIterator();
    }
	
    // UUID-related methods
    /**
     * Returns the TodoItem with the given UUID.
     * 
     * @param itemID The UUID of the item to search for.
     * @return The item with the given UUID.
     */
	public TodoItem getByUUID(UUID itemID) {
        for (TodoItem currentItem : todoItems) {
            if (currentItem.getUUID().equals(itemID)) {
                return currentItem;
            }
        }
	    return null;
	}
	
	/**
	 * Returns the index within the TodoItemList of the TodoItem with the given UUID.
	 * 
	 * @param itemID The UUID of the item to search for.
	 * @return The index of the item.
	 */
    public int searchIndexByUUID(UUID itemID) {
        for (int i = 0; i < todoItems.size(); i++) {
            TodoItem currentItem = todoItems.get(i);
            if (currentItem.getUUID().equals(itemID)) {
                return i;
            }
        }
        return -1;
    }

	
	// CRUD
    /**
     * Adds a new TodoItem into the internal ArrayList.
     * 
     * @param newItem The TodoItem to be added.
     */
	public void addTodoItem(TodoItem newItem) {
		todoItems.add(newItem);
	}

	/**
	 * Deletes a TodoItem by its UUID.
	 * 
	 * @param itemID The UUID of the item to be deleted.
	 * @return The deleted TodoItem.
	 */
    public TodoItem deleteByUUID(UUID itemID) {
        for (int i = 0; i < todoItems.size(); i++) {
            if (todoItems.get(i).getUUID().equals(itemID)) {
                return todoItems.remove(i);
            } 
        }
        return null;
    }
    
    /**
     * Clears the TodoItemList of its TodoItems.
     */
    public void clearTodoItems() {
        todoItems = new ArrayList<TodoItem>();
    }
    
    // Miscellaneous
    /**
     * @return The number of TodoItems inside the TodoItemList.
     */
    public int countTodoItems() {
        return todoItems.size();
    }
}