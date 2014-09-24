package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.ArrayList;
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
	public final String defaultFileName = "watdo.json";
	
	public TodoItemList() {
		todoItems = new ArrayList<TodoItem>();
		fileName = defaultFileName;
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
		this.fileName = fileName;
		try {
		    // loadFile(this.fileName);
		    loadStatus = LOAD_SUCCESS;
		    writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    loadStatus = LOAD_FAILED;
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public void changeFile(String fileName) {
		this.fileName = fileName;
		try {
		    // loadFile(this.fileName);
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
		    // updateFile();
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
		
		if (taskName != null) {
			updatedItem.setTaskName(taskName);
		}
		if (startDate != null) {
			updatedItem.setStartDate(startDate);
		}
		if (endDate != null) {
			updatedItem.setEndDate(endDate);
		}
		try {
		    // updateFile();
		    writeStatus = WRITE_SUCCESS;
		} catch (Exception e) {
		    writeStatus = WRITE_FAILED;
		}
	}
	
	public TodoItem deleteTodoItem(int index) {
		try {
			TodoItem removed = todoItems.remove(index);
			// updateFile();
			writeStatus = WRITE_SUCCESS;
			return removed;
		} catch (Exception e) {
		    writeStatus = WRITE_FAILED;
		    return null;
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
		    if (currentTaskName != null) {
		        fileObject.put("TaskName", currentTodoItem.getTaskName());
		    }
		    if (currentStartDate != null) {
		        fileObject.put("startDate", currentTodoItem.getStartDate().toString());
		    }
		    if (currentEndDate != null) {
		        fileObject.put("endDate", currentTodoItem.getEndDate().toString());
		    }
		    
		    fileArray.add(fileObject);
		}
		writer.write(fileArray.toString());
		fileToWrite.close();
	}
	
	private void loadFile(String fileToLoad) throws IOException, ParseException {
		FileReader fileToRead = new FileReader(fileToLoad);
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
		    String currentTaskName = (String) currentJSONObject.get("TaskName");
		    Date currentStartDate = (Date) currentJSONObject.get("startDate");
		    Date currentEndDate = (Date) currentJSONObject.get("endDate");
		    
		    todoItems.add(new TodoItem(currentTaskName, currentStartDate, currentEndDate));
		} 
	}
}