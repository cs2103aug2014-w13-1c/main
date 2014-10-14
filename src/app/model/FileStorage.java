package app.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class FileStorage {
    
    private static FileStorage currentInstance;
    
    private String fileName;
    private String fileDirectory;
    
    private String loadStatus;
    private String writeStatus;
    
    public static final String LOAD_SUCCESS = " file load success";
    public static final String LOAD_FAILED = " file load failed";
    public static final String WRITE_SUCCESS = " file write success";
    public static final String WRITE_FAILED = " file write failed";

    public static final String DEFAULT_FILE_NAME = "watdo.json";
    public static final String DEFAULT_FILE_DIRECTORY = "";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    
    public FileStorage() {
        this.fileDirectory = DEFAULT_FILE_DIRECTORY;
        this.fileName = DEFAULT_FILE_NAME;
    }
    
    public void loadFile(TodoItemList todoItems) throws IOException, ParseException {
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(fileDirectory + fileName);
        } catch (FileNotFoundException e) { // if no file found at stated path, return
            return;
        }
        BufferedReader reader = new BufferedReader(fileToRead);
        
        String fileString = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            fileString += line;
        }
        
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
            
            todoItems.addTodoItem(new TodoItem(currentTaskName, currentStartDate, currentEndDate, currentPriority, currentDoneStatus));
        }
        
        reader.close();
    }
    
    /**
     * @throws IOException
     */
    public void updateFile(ArrayList<TodoItem> todoItems) throws IOException {
        FileWriter fileToWrite;
        
        try {
            fileToWrite = new FileWriter(fileDirectory + fileName);
        } catch (Exception e) {
            throw new IOException(fileName + LOAD_FAILED);
        }

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
        
        try {
            writer.write(fileArray.toJSONString());
            writer.flush();
        } catch (Exception e) {
            throw new IOException(fileName + WRITE_FAILED);
        }
        
        try {
            fileToWrite.close();
        } catch (Exception e) {
            throw new IOException(fileName + LOAD_FAILED);
        }
    }
    
    /**
     * When file is changed, load not save. Pretty much that.
     */
    public void changeDirectory(String fileDirectory, TodoItemList todoItems) throws IOException {
        this.fileDirectory = fileDirectory;
        try {
            loadFile(todoItems);
            this.loadStatus = LOAD_SUCCESS;
        } catch (Exception e) {
            this.loadStatus = LOAD_FAILED;
            throw new IOException(fileName + LOAD_FAILED);
        }
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public String getFullFileName() {
        return this.fileDirectory + this.fileName;
    }
    
    public String getFileDirectory() {
        return this.fileDirectory;
    }

    public String getLoadStatus() {
        return this.loadStatus;
    }
    
    public String getWriteStatus() {
        return this.writeStatus;
    }
}
