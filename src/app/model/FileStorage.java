package app.model;

import app.helpers.LoggingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.logging.Level;

public class FileStorage {
    
    private String fileName;
    private String fileDirectory;
    
    private Boolean notificationsEnabled;
    private Boolean randomColorsEnabled;
    
    private String loadStatus;
    private String writeStatus;
    
    public static final String LOAD_SUCCESS = " file load success";
    public static final String LOAD_FAILED = " file load failed";
    public static final String WRITE_SUCCESS = " file write success";
    public static final String WRITE_FAILED = " file write failed";

    public static final String DEFAULT_FILE_NAME = "watdo.json";
    public static final String DEFAULT_FILE_DIRECTORY = ".";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    
    public FileStorage() {
        this.fileDirectory = DEFAULT_FILE_DIRECTORY;
        this.fileName = DEFAULT_FILE_NAME;
        this.notificationsEnabled = true;
        this.randomColorsEnabled = true;
    }
    
    public ArrayList<TodoItem> loadFile() throws IOException, JSONException, ParseException {
        LoggingService.getLogger().log(Level.INFO, "Loading file " + fileDirectory + fileName);
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(fileDirectory + fileName);
        } catch (FileNotFoundException e) { // if no file found at stated path, return
            LoggingService.getLogger().log(Level.INFO, "No file found at target destination.");
            return null;
        }
        
        BufferedReader reader = new BufferedReader(fileToRead);
        
        String fileString = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            fileString += line;
        }
        
        JSONArray fileArray = new JSONArray(new JSONTokener(fileString));
        
        ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();  

        for (int i = 0; i < fileArray.length(); i++) {
            JSONObject currentJSONObject = fileArray.getJSONObject(i);
            
            String currentTaskName = null;
            Date currentStartDate = null;
            Date currentEndDate = null;
            String currentPriority = null;
            Boolean currentDoneStatus = false;
            
            String JSONTaskName = currentJSONObject.optString("taskName");
            String JSONStartDate = currentJSONObject.optString("startDate");
            String JSONEndDate = currentJSONObject.optString("endDate");
            String JSONPriority = currentJSONObject.optString("priority"); 
            Boolean JSONDoneStatus = currentJSONObject.optBoolean("doneStatus");
            
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            
            if (JSONTaskName.length() > 0) {
                currentTaskName = JSONTaskName;
            }

            if (JSONStartDate.length() > 0) {
                currentStartDate = df.parse(JSONStartDate);
            }
            
            if (JSONEndDate.length() > 0) {
                currentEndDate = df.parse(JSONEndDate);
            }
            
            if (JSONPriority.length() > 0) {
                currentPriority = JSONPriority;
            }
            
            if (JSONDoneStatus) {
                currentDoneStatus = JSONDoneStatus;
            }
            
            todoItems.add(new TodoItem(currentTaskName, currentStartDate, currentEndDate, currentPriority, currentDoneStatus));
        }
        reader.close();
        
        return todoItems;
    }
    
    /**
     * @throws IOException
     */
    public void updateFile(ArrayList<TodoItem> todoItems) throws IOException {
        FileWriter fileToWrite;
        
        try {
            File targetFile = new File(fileDirectory);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            fileToWrite = new FileWriter(fileDirectory + fileName);
        } catch (Exception e) {
            throw new IOException(fileName + LOAD_FAILED);
        }

        BufferedWriter writer = new BufferedWriter(fileToWrite);
        
        JSONArray fileArray = new JSONArray();
        
        ListIterator<TodoItem> todoListIterator = todoItems.listIterator();

        SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
        
        try {
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
                    fileObject.put("startDate", df.format(currentStartDate).toString());
                }
                if (currentEndDate != null) {
                    fileObject.put("endDate", df.format(currentEndDate).toString());
                }
                if (currentPriority != null) {
                    fileObject.put("priority", currentPriority);
                }
                if (currentDoneStatus != null) {
                    fileObject.put("doneStatus", currentDoneStatus);
                }
                fileArray.put(fileObject);
            }
        } catch (JSONException e) {
            throw new IOException("Failed to write JSON data.");
        }
        LoggingService.getLogger().log(Level.INFO, "Updating file " + fileDirectory + fileName);
        
        try {
            writer.write(fileArray.toString(2));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            LoggingService.getLogger().log(Level.SEVERE, fileName + WRITE_FAILED);
            throw new IOException(fileName + WRITE_FAILED);
        }
        
        LoggingService.getLogger().log(Level.INFO, "Successfully updated file " + fileDirectory + fileName);
        
        try {
            fileToWrite.close();
        } catch (Exception e) {
            throw new IOException(fileName + LOAD_FAILED);
        }
    }
    
    public ArrayList<TodoItem> changeSettings(String fileDirectory, Boolean newRandomColorsEnabled, Boolean newNotificationsEnabled) throws IOException {
        assert fileDirectory != null;
        
        String tempFileDirectory = this.fileDirectory;
        Boolean tempRandomColorsEnabled = this.randomColorsEnabled;
        Boolean tempNotificationsEnabled = this.notificationsEnabled;
        ArrayList<TodoItem> loadResult;

        if (newRandomColorsEnabled != null) {
            this.randomColorsEnabled = newRandomColorsEnabled;
        }
        
        if (newNotificationsEnabled != null) {
            this.notificationsEnabled = newNotificationsEnabled;
        }
    
        if (fileDirectory.length() > 0) {
            if (fileDirectory.charAt(fileDirectory.length() - 1) != '/') {
                fileDirectory = fileDirectory + "/";
            }
        }
        
        this.fileDirectory = fileDirectory; 
        try {
            loadResult = loadFile();
            this.loadStatus = LOAD_SUCCESS;
            updateSettings();
            return loadResult;
        } catch (Exception e) {
            this.fileDirectory = tempFileDirectory;
            this.randomColorsEnabled = tempRandomColorsEnabled;
            this.notificationsEnabled = tempNotificationsEnabled;
            this.loadStatus = LOAD_FAILED;
            throw new IOException(fileName + LOAD_FAILED);
        }
    }
    
    public void loadSettings() throws IOException, JSONException {
        LoggingService.getLogger().log(Level.INFO, "Loading settings file.");
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(SETTINGS_FILE_NAME);
            LoggingService.getLogger().log(Level.INFO, "Loaded settings file.");
        } catch (FileNotFoundException e) { // if no file found at stated path, create new settings file
            LoggingService.getLogger().log(Level.INFO, "No settings file found at target destination, creating new settings.json.");
            updateSettings();
            return;
        }
        BufferedReader reader = new BufferedReader(fileToRead);
        
        String fileString = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            fileString += line;
        }
        
        JSONObject settingsObject = new JSONObject(fileString);
        
        String JSONfileDirectory = settingsObject.optString("fileDirectory");
        Boolean JSONrandomColorsEnabled = settingsObject.optBoolean("randomColorsEnabled");
        Boolean JSONnotificationsEnabled = settingsObject.optBoolean("notificationsEnabled");
        
        fileDirectory = JSONfileDirectory;
        randomColorsEnabled = JSONrandomColorsEnabled;
        notificationsEnabled = JSONnotificationsEnabled;
        
        reader.close();
    }
    
    public void updateSettings() throws IOException, JSONException {
        FileWriter fileToWrite;
        
        try {
            fileToWrite = new FileWriter(SETTINGS_FILE_NAME);
        } catch (Exception e) {
            throw new IOException(SETTINGS_FILE_NAME + LOAD_FAILED);
        }

        BufferedWriter writer = new BufferedWriter(fileToWrite);
        
        JSONObject settingsObject = new JSONObject();
        settingsObject.put("fileDirectory", fileDirectory);
        settingsObject.put("randomColorsEnabled", randomColorsEnabled);
        settingsObject.put("notificationsEnabled", notificationsEnabled);
        
        LoggingService.getLogger().log(Level.INFO, "Updating settings file.");
        
        try {
            writer.write(settingsObject.toString(2));
            writer.flush();
        } catch (Exception e) {
            LoggingService.getLogger().log(Level.SEVERE, SETTINGS_FILE_NAME + WRITE_FAILED);
            throw new IOException(SETTINGS_FILE_NAME + WRITE_FAILED);
        }
        
        LoggingService.getLogger().log(Level.INFO, "Successfully updated settings file.");
        
        try {
            fileToWrite.close();
        } catch (Exception e) {
            throw new IOException(SETTINGS_FILE_NAME + WRITE_FAILED);
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
    
    public Boolean areRandomColorsEnabled() {
        return this.randomColorsEnabled;
    }
    
    public Boolean areNotificationsEnabled() {
        return this.notificationsEnabled;
    }

    public String getLoadStatus() {
        return this.loadStatus;
    }
    
    public String getWriteStatus() {
        return this.writeStatus;
    }
}
