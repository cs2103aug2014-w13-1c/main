package app.model;

//import javafx.beans.property.IntegerProperty;
//import javafx.beans.property.ObjectProperty;
//import javafx.beans.property.SimpleIntegerProperty;
//import javafx.beans.property.SimpleObjectProperty;
//import javafx.beans.property.SimpleStringProperty;
//import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.ListIterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class TodoItemList
 * 
 * The model class to hold one to-do task of wat do.
 * 
 * @author Nguyen Quoc Dat (A0116703N)
 */

public class TodoItemList {
	private ArrayList<TodoItem> todoItems;
	
	private String fileName;
	private String loadStatus;
	private String writeStatus;
	
	public static final String LOAD_SUCCESS = "File load success";
	public static final String LOAD_FAILED = "File load failed";
	public static final String WRITE_SUCCESS = "File write success";
	public static final String WRITE_FAILED = "File write failed";
	
	public static final String DEFAULT_FILE_NAME = "watdo.json";
	
	public TodoItemList() {
		todoItems = new ArrayList<TodoItem>();
		fileName = DEFAULT_FILE_NAME;
		try {
		    loadFile(this.fileName);
		    loadStatus = LOAD_SUCCESS;
	        writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    System.out.println(e);
		    loadStatus = LOAD_FAILED;
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public TodoItemList(String fileName) {
        this.fileName = fileName; // The ordering of these two statements is very important.
	    todoItems = new ArrayList<TodoItem>();
		try {
		    loadFile(this.fileName);
		    loadStatus = LOAD_SUCCESS;
		    writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    System.out.println(e);
		    loadStatus = LOAD_FAILED;
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public void changeFile(String fileName) {
		this.fileName = fileName;
		try {
		    loadFile(this.fileName);
		    loadStatus = LOAD_SUCCESS;
		} catch (Exception e) {
		    loadStatus = LOAD_FAILED;
		}
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public ArrayList<TodoItem> getTodoItems() {
		return todoItems;
	}
	
	public ListIterator<TodoItem> getTodoItemsIterator() {
	    return todoItems.listIterator();
	}
	
	public String getLoadStatus() {
	    return loadStatus;
	}
	
	public String getWriteStatus() {
	    return writeStatus;
	}
	
	// CRUD
	public void addTodoItem(TodoItem newItem) {
		todoItems.add(newItem);
		
		try {
		    updateFile();
		    writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public TodoItem readTodoItem(int index) {
		try {
			return todoItems.get(index);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public void updateTodoItem(int index, String taskName, Date startDate, Date endDate) {
		TodoItem updatedItem = todoItems.get(index);
		
		updatedItem.setTaskName(taskName);
		updatedItem.setStartDate(startDate);
		updatedItem.setEndDate(endDate);
		try {
		    updateFile();
		    writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public void updateTodoItem(int index, String taskName, Date startDate, Date endDate, String priority) {
        TodoItem updatedItem = todoItems.get(index);
        
        updatedItem.setTaskName(taskName);
        updatedItem.setStartDate(startDate);
        updatedItem.setEndDate(endDate);
        updatedItem.setPriority(priority);
        try {
            updateFile();
            writeStatus = WRITE_SUCCESS;
        } catch (Exception e) {
            writeStatus = WRITE_FAILED;
        }
	}
	
	public void markDoneStatus(int index, Boolean newDoneStatus) {
        TodoItem updatedItem = todoItems.get(index);
        
        updatedItem.setDoneStatus(newDoneStatus);
        try {
            updateFile();
            writeStatus = WRITE_SUCCESS;
        } catch (Exception e) {
            writeStatus = WRITE_FAILED;
        }
    }
	
	public TodoItem deleteTodoItem(int index) {
		try {
			TodoItem removed = todoItems.remove(index);
			updateFile();
			writeStatus = WRITE_SUCCESS;
			return removed;
		} catch (Exception e) {
		    writeStatus = WRITE_FAILED;
		    return null;
		}
	}
	
	public void clearTodoItems() {
        todoItems = new ArrayList<TodoItem>();
	    try {
	        updateFile();
	        writeStatus = WRITE_SUCCESS;
	    } catch (Exception e) {
	        writeStatus = WRITE_FAILED;
	    }
	}
	
	public int countTodoItems() {
	    return todoItems.size();
	}
	
	private void updateFile() throws IOException {
	    FileWriter fileToWrite = new FileWriter(fileName);
		BufferedWriter writer = new BufferedWriter(fileToWrite);
		
		JSONArray fileArray = new JSONArray();
		
		ListIterator<TodoItem> todoListIterator = todoItems.listIterator();
		
		while (todoListIterator.hasNext()) {
		    TodoItem currentTodoItem = todoListIterator.next();
		    JSONObject fileObject = new JSONObject();
		    
		    String currentTaskName = currentTodoItem.getTaskName();
		    Date currentStartDate = currentTodoItem.getStartDate();
		    Date currentEndDate = currentTodoItem.getEndDate();
		    String currentPriority = currentTodoItem.getPriority();
		    Boolean currentDoneStatus = currentTodoItem.isDone();
		    if (currentTaskName != null) {
		        fileObject.put("taskName", currentTaskName);
		    }
		    if (currentStartDate != null) {
		        fileObject.put("startDate", currentStartDate.getTime());
		    }
		    if (currentEndDate != null) {
		        fileObject.put("endDate", currentEndDate.getTime());
		    }
		    if (currentPriority != null) {
		        fileObject.put("priority", currentPriority);
		    }
		    if (currentDoneStatus != null) {
		        fileObject.put("doneStatus", currentDoneStatus);
		    }
		    fileArray.add(fileObject);
		}
		
		writer.write(fileArray.toJSONString());
		writer.flush();
		fileToWrite.close();
	}
	
	private void loadFile(String fileToLoad) throws IOException, ParseException, java.text.ParseException {
	    FileReader fileToRead;
	    try {
	        fileToRead = new FileReader(fileToLoad);
	    } catch (FileNotFoundException e) { // if no file found at stated path, create new file
	        File fileToBeCreated = new File(fileToLoad);
	        fileToBeCreated.createNewFile();
	        updateFile();
	        fileToRead = new FileReader(fileToLoad);
	    }
	    BufferedReader reader = new BufferedReader(fileToRead);
	    
		String fileString = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
		    fileString += line;
		}
		
		todoItems = new ArrayList<TodoItem>();
		
		JSONParser parser = new JSONParser();
		
		JSONArray fileArray = (JSONArray) parser.parse(fileString);
		for (int i = 0; i < fileArray.size(); i++) {
		    JSONObject currentJSONObject = (JSONObject) fileArray.get(i);
		    String currentTaskName = null;
		    Date currentStartDate = null;
		    Date currentEndDate = null;
		    String currentPriority = null;
		    Boolean currentDoneStatus = null;
		    
		    Object JSONTaskName = currentJSONObject.get("taskName");
		    Object JSONStartDate = currentJSONObject.get("startDate");
		    Object JSONEndDate = currentJSONObject.get("endDate");
		    Object JSONPriority = currentJSONObject.get("priority"); 
		    Object JSONDoneStatus = currentJSONObject.get("doneStatus");
		    
		    if (JSONTaskName != null) {
		        currentTaskName = (String) JSONTaskName;
		    }
		    if (JSONStartDate != null) {
		        currentStartDate = new Date((Long) JSONStartDate);
		    }
		    if (JSONEndDate != null) {
		        currentEndDate = new Date((Long) JSONEndDate);
		    }
		    if (JSONPriority != null) {
		        currentPriority = (String) JSONPriority;
		    }
		    if (JSONDoneStatus != null) {
		        currentDoneStatus = (Boolean) JSONDoneStatus;
		    }
		    
		    todoItems.add(new TodoItem(currentTaskName, currentStartDate, currentEndDate, currentPriority));
		}
		
		reader.close();
	}
	
	public static Comparator<TodoItem> nameComparator = new Comparator<TodoItem>() {

	    @Override
        public int compare(TodoItem todoItem1, TodoItem todoItem2) {
            if ((todoItem1 == null || todoItem1.getTaskName() == null) && (todoItem2 == null || todoItem2.getTaskName() == null)) {
                return 0;
            }
            if (todoItem1 == null || todoItem1.getTaskName() == null) {
                return -1;
            }
            if (todoItem2 == null || todoItem2.getTaskName() == null) {
                return 1;
            }
            return todoItem1.getTaskName().compareTo(todoItem2.getTaskName());
        }
	    
	};
	
	public static Comparator<TodoItem> startComparator = new Comparator<TodoItem>() {

	    @Override
        public int compare(TodoItem todoItem1, TodoItem todoItem2) {
            if ((todoItem1 == null || todoItem1.getStartDate() == null) && (todoItem2 == null || todoItem2.getStartDate() == null)) {
                return 0;
            }
            if (todoItem1 == null || todoItem1.getStartDate() == null) {
                return -1;
            }
            if (todoItem2 == null || todoItem2.getStartDate() == null) {
                return 1;
            }
            return ((Long) todoItem1.getStartDate().getTime()).compareTo(todoItem2.getStartDate().getTime());
        }
        
    };
    
    public static Comparator<TodoItem> endComparator = new Comparator<TodoItem>() {

        @Override
        public int compare(TodoItem todoItem1, TodoItem todoItem2) {
            if ((todoItem1 == null || todoItem1.getEndDate() == null) && (todoItem2 == null || todoItem2.getEndDate() == null)) {
                return 0;
            }
            if (todoItem1 == null || todoItem1.getEndDate() == null) {
                return -1;
            }
            if (todoItem2 == null || todoItem2.getEndDate() == null) {
                return 1;
            }
            return ((Long) todoItem1.getEndDate().getTime()).compareTo(todoItem2.getEndDate().getTime());
        }
        
    };
    
    public static Comparator<TodoItem> priorityComparator = new Comparator<TodoItem>() {

        @Override
        public int compare(TodoItem todoItem1, TodoItem todoItem2) {
            if ((todoItem1 == null || todoItem1.getPriority() == null) && (todoItem2 == null || todoItem2.getPriority() == null)) {
                return 0;
            }
            if (todoItem1 == null || todoItem1.getPriority() == null) {
                return -1;
            }
            if (todoItem2 == null || todoItem2.getPriority() == null) {
                return 1;
            }
            return ((Long) todoItem1.getEndDate().getTime()).compareTo(todoItem2.getEndDate().getTime());
        }
        
    };
}