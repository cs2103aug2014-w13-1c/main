package app.model;
//@author A0116703N

import app.services.LoggingService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 * This class is in charge of all I/O operations from the Model, and
 * manages program settings.
 *
 */
public class FileStorage {
    
    // Settings data
    private String fileName;
    private String fileDirectory;
    private Boolean notificationsEnabled;
    private Boolean randomColorsEnabled;
    
    // Load/write statuses. Mainly for debugging convenience.
    private String loadStatus;
    private String writeStatus;
    
    // Error messages
    public static String LOAD_SUCCESS = ModelManager.LOAD_SUCCESS;
    public static String LOAD_FAILED = ModelManager.LOAD_FAILED;
    public static String PARSE_FAILED = ModelManager.PARSE_FAILED;
    public static String WRITE_SUCCESS = ModelManager.WRITE_SUCCESS;
    public static String WRITE_FAILED = ModelManager.WRITE_FAILED;
    public static String WRITE_SETTINGS_FAILED = ModelManager.WRITE_SETTINGS_FAILED;
    public static String LOAD_SETTINGS_FAILED = ModelManager.LOAD_SETTINGS_FAILED;

    // Default file names
    public static final String DEFAULT_FILE_NAME = "watdo.json";
    public static final String DEFAULT_FILE_DIRECTORY = "./";
    public static final String SETTINGS_FILE_NAME = "settings.json";
    
    // Logger messages
    private static final String LOADING_FILE_MESSAGE = "Loading file ";
    private static final String WRITING_FILE_MESSAGE = "Writing to file ";
    private static final String WRITE_FILE_SUCCESS_MESSAGE = "Successfully updated file ";
    private static final String CHANGING_SETTINGS_MESSAGE = "Changing settings. New directory is ";
    private static final String LOADING_SETTINGS_MESSAGE = "Loading settings file.";
    private static final String WRITING_SETTINGS_MESSAGE = "Writing to settings file.";
    private static final String WRITE_SETTINGS_SUCCESS_MESSAGE = "Successfully updated settings file.";
    private static final String NO_FILE_FOUND_MESSAGE = "No file found at target destination.";
    private static final String NO_SETTINGS_FILE_FOUND_MESSAGE = "No settings file found at target destination, creating new settings.json.";
    
    // Fields in JSON objects
    private static final String JSON_TASK_NAME_FIELD = "taskName";
    private static final String JSON_START_DATE_FIELD = "startDate";
    private static final String JSON_END_DATE_FIELD = "endDate";
    private static final String JSON_PRIORITY_FIELD = "priority";
    private static final String JSON_DONE_STATUS_FIELD = "doneStatus";
    private static final String JSON_DIRECTORY_FIELD = "fileDirectory";
    private static final String JSON_RANDOM_COLORS_FIELD = "randomColorsEnabled";
    private static final String JSON_NOTIFICATIONS_FIELD = "notificationsEnabled";
    
    // Date formatting
    private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy";
    
    // Slash character for concatSlash
    private static final char SLASH_CHARACTER = '/';
    
    /**
     * Constructor
     * 
     * Initializes internal state and settings.
     * Does NOT attempt to do any file I/O operations yet. Instead, we
     * let ModelManager control when to fire I/O operations.
     */
    public FileStorage() {
        this.fileDirectory = DEFAULT_FILE_DIRECTORY;
        this.fileName = DEFAULT_FILE_NAME;
        this.notificationsEnabled = true;
        this.randomColorsEnabled = true;
    }
    
    /**
     * Loads watdo.json file data as a String, and then parses it and returns
     * the data as an ArrayList of TodoItems. 
     * 
     * @return Loaded ArrayList of TodoItems from file
     * 
     * @throws IOException Undefined message, caught by ModelManager constructor
     * @throws JSONException Undefined message, caught by ModelManager constructor
     * @throws ParseException Undefined message, caught by ModelManager constructor
     */
    public ArrayList<TodoItem> loadFile() throws IOException, JSONException, ParseException {
        // First, log the method call
        LoggingService.getLogger().log(Level.INFO, LOADING_FILE_MESSAGE + fileDirectory + fileName);
        
        // Try to open the file!
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(fileDirectory + fileName);
        } catch (FileNotFoundException e) { // if no file found at stated path, return
            LoggingService.getLogger().log(Level.INFO, NO_FILE_FOUND_MESSAGE);
            return null;
        }
        
        // Successfully opened the file, now we get the data as a string.
        BufferedReader reader = new BufferedReader(fileToRead);
        String fileString = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            fileString += line;
        }
        reader.close();
        
        // Now we parse the string.
        JSONArray fileArray = new JSONArray(new JSONTokener(fileString));
        
        ArrayList<TodoItem> todoItems = new ArrayList<TodoItem>();  

        for (int i = 0; i < fileArray.length(); i++) {
            JSONObject currentJSONObject = fileArray.getJSONObject(i);
            
            String currentTaskName = null;
            Date currentStartDate = null;
            Date currentEndDate = null;
            String currentPriority = null;
            Boolean currentDoneStatus = false;
            
            String JSONTaskName = currentJSONObject.optString(JSON_TASK_NAME_FIELD);
            String JSONStartDate = currentJSONObject.optString(JSON_START_DATE_FIELD);
            String JSONEndDate = currentJSONObject.optString(JSON_END_DATE_FIELD);
            String JSONPriority = currentJSONObject.optString(JSON_PRIORITY_FIELD); 
            Boolean JSONDoneStatus = currentJSONObject.optBoolean(JSON_DONE_STATUS_FIELD);
            
            SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
            
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
        
        return todoItems;
    }
    
    /**
     * Updates the content of the watdo.json file with the given data.
     * 
     * @param todoItems The data to write to the file.
     * @throws IOException Messages are LOAD_FAILED, PARSE_FAILED, WRITE_FAILED
     */
    public void updateFile(ArrayList<TodoItem> todoItems) throws IOException {
        // First, log the method call
        LoggingService.getLogger().log(Level.INFO, WRITING_FILE_MESSAGE + fileDirectory + fileName);
        
        // Then try to access the file destination!
        FileWriter fileToWrite;
        try {
            File targetFile = new File(fileDirectory);
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            fileToWrite = new FileWriter(fileDirectory + fileName);
        } catch (Exception e) {
            throw new IOException(LOAD_FAILED);
        }

        // Access successful, now we start writing the data to a single JSONArray.
        BufferedWriter writer = new BufferedWriter(fileToWrite);
        JSONArray fileArray = new JSONArray();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        
        try {
            for(TodoItem currentTodoItem : todoItems) {
                JSONObject fileObject = new JSONObject();
                
                String currentTaskName = currentTodoItem.getTaskName();
                Date currentStartDate = currentTodoItem.getStartDate();
                Date currentEndDate = currentTodoItem.getEndDate();
                String currentPriority = currentTodoItem.getPriority();
                Boolean currentDoneStatus = currentTodoItem.isDone();
                
                if (currentTaskName != null) {
                    fileObject.put(JSON_TASK_NAME_FIELD, currentTaskName);
                }
                if (currentStartDate != null) {
                    fileObject.put(JSON_START_DATE_FIELD, df.format(currentStartDate).toString());
                }
                if (currentEndDate != null) {
                    fileObject.put(JSON_END_DATE_FIELD, df.format(currentEndDate).toString());
                }
                if (currentPriority != null) {
                    fileObject.put(JSON_PRIORITY_FIELD, currentPriority);
                }
                if (currentDoneStatus != null) {
                    fileObject.put(JSON_DONE_STATUS_FIELD, currentDoneStatus);
                }
                fileArray.put(fileObject);
            }
        } catch (JSONException e) {
            throw new IOException(PARSE_FAILED);
        }
        
        // Now that we have the JSONArray, we convert it into an indented string,
        // and then write to the file.
        try {
            writer.write(fileArray.toString(2));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            LoggingService.getLogger().log(Level.SEVERE, fileName + WRITE_FAILED);
            throw new IOException(WRITE_FAILED);
        }
        
        // Log again to confirm the success of the operation.
        LoggingService.getLogger().log(Level.INFO, WRITE_FILE_SUCCESS_MESSAGE + fileDirectory + fileName);
        
        // Finally close the target file.
        try {
            fileToWrite.close();
        } catch (Exception e) {
            throw new IOException(LOAD_FAILED);
        }
    }
    
    /**
     * Changes this object's internal settings to the given ones,
     * and then update the settings.json file with the new settings.
     * Finally, this method will load the watdo.json data at the new
     * directory.
     * 
     * @param fileDirectory The new directory to load watdo.json at. Must not be null.
     * @param newRandomColorsEnabled The new random color display setting
     * @param newNotificationsEnabled The new notification setting
     * @return ArrayList of TodoItems after loading the file
     * @throws IOException with WRITE_SETTINGS_FAILED
     */
    public ArrayList<TodoItem> changeSettings(String fileDirectory, Boolean newRandomColorsEnabled, Boolean newNotificationsEnabled) throws IOException {
        // First, log the method call.
        LoggingService.getLogger().log(Level.INFO, CHANGING_SETTINGS_MESSAGE + fileDirectory);
        
        assert fileDirectory != null; //fileDirectory must not be null.
        
        // Store the old data in temp variables in case of errors.
        String tempFileDirectory = this.fileDirectory;
        Boolean tempRandomColorsEnabled = this.randomColorsEnabled;
        Boolean tempNotificationsEnabled = this.notificationsEnabled;
        ArrayList<TodoItem> loadResult;

        // Now change the internal values.
        if (newRandomColorsEnabled != null) {
            this.randomColorsEnabled = newRandomColorsEnabled;
        }
        if (newNotificationsEnabled != null) {
            this.notificationsEnabled = newNotificationsEnabled;
        }
        this.fileDirectory = fileDirectory;
        concatSlash();
        
        // Try to load the file at the new directory.
        // If successful, we write the new settings to the settings.json file
        // and then return the loaded data.
        try {
            loadResult = loadFile();
            this.loadStatus = LOAD_SUCCESS;
            updateSettings();
            return loadResult;
        } catch (Exception e) {
            // If there are any issues during the process, we change the internal values
            // back to the old values, and then throw an exception.
            this.fileDirectory = tempFileDirectory;
            this.randomColorsEnabled = tempRandomColorsEnabled;
            this.notificationsEnabled = tempNotificationsEnabled;
            this.loadStatus = LOAD_FAILED;
            throw new IOException(WRITE_SETTINGS_FAILED);
        }
    }
    
    /**
     * Loads settings from the settings.json file. Usually called only at
     * program startup.
     * 
     * @throws IOException Message undefined, caught by ModelManager.
     * @throws JSONException Message undefined, caught by ModelManager.
     */
    public void loadSettings() throws IOException, JSONException {
        // First, log the method call.
        LoggingService.getLogger().log(Level.INFO, LOADING_SETTINGS_MESSAGE);
        
        // Then try to open the settings file.
        FileReader fileToRead;
        try {
            fileToRead = new FileReader(SETTINGS_FILE_NAME);
        } catch (FileNotFoundException e) { // if no file found at stated path, create new settings file
            LoggingService.getLogger().log(Level.INFO, NO_SETTINGS_FILE_FOUND_MESSAGE);
            updateSettings();
            return;
        }
        
        // When settings file is found, begin parsing the data.
        BufferedReader reader = new BufferedReader(fileToRead);
        
        String fileString = "";
        String line = "";
        while ((line = reader.readLine()) != null) {
            fileString += line;
        }
        
        JSONObject settingsObject = new JSONObject(fileString);
        
        String JSONfileDirectory = settingsObject.optString(JSON_DIRECTORY_FIELD);
        Boolean JSONrandomColorsEnabled = settingsObject.optBoolean(JSON_RANDOM_COLORS_FIELD);
        Boolean JSONnotificationsEnabled = settingsObject.optBoolean(JSON_NOTIFICATIONS_FIELD);
        
        fileDirectory = JSONfileDirectory;
        randomColorsEnabled = JSONrandomColorsEnabled;
        notificationsEnabled = JSONnotificationsEnabled;
        
        // Close the file when we're done.
        reader.close();
    }
    
    /**
     * Writes this object's current settings data to the settings.json file.
     * 
     * @throws IOException Message LOAD_SETTINGS_FAILED, WRITE_SETTINGS_FAILED
     * @throws JSONException Messsage undefined, caught by changeSettings
     */
    public void updateSettings() throws IOException, JSONException {
        // First, log the method call.
        LoggingService.getLogger().log(Level.INFO, WRITING_SETTINGS_MESSAGE);
        
        // Then, try to access the file.
        FileWriter fileToWrite;
        try {
            fileToWrite = new FileWriter(SETTINGS_FILE_NAME);
        } catch (Exception e) {
            throw new IOException(LOAD_SETTINGS_FAILED);
        }

        // Now we convert the settings data into a JSONObject.
        BufferedWriter writer = new BufferedWriter(fileToWrite);
        concatSlash();
        
        JSONObject settingsObject = new JSONObject();
        settingsObject.put(JSON_DIRECTORY_FIELD, fileDirectory);
        settingsObject.put(JSON_RANDOM_COLORS_FIELD, randomColorsEnabled);
        settingsObject.put(JSON_NOTIFICATIONS_FIELD, notificationsEnabled);
        
        // And then convert the JSONObject into an indented string
        // And then write it to the file.
        try {
            writer.write(settingsObject.toString(2));
            writer.flush();
            writer.close();
        } catch (Exception e) {
            LoggingService.getLogger().log(Level.SEVERE, SETTINGS_FILE_NAME + WRITE_FAILED);
            throw new IOException(LOAD_SETTINGS_FAILED);
        }
        
        // Log again to confirm operation success.
        LoggingService.getLogger().log(Level.INFO, WRITE_SETTINGS_SUCCESS_MESSAGE);
        
        // Finally we close the file.
        try {
            fileToWrite.close();
        } catch (Exception e) {
            throw new IOException(WRITE_SETTINGS_FAILED);
        }
    }
    
    // Methods for acquiring settings data
    /**
     * @return The filename in use by the program. Always watdo.json.
     */
    public String getFileName() {
        return this.fileName;
    }

    /**
     * @return The filepath in use by the program.
     */
    public String getFileDirectory() {
        return this.fileDirectory;
    }
    
    /**
     * @return The filepath and filename in use by the program.
     */
    public String getFullFileName() {
        return this.fileDirectory + this.fileName;
    }
    
    /**
     * @return The settings value for whether random color task display is enabled.
     */
    public Boolean areRandomColorsEnabled() {
        return this.randomColorsEnabled;
    }
    
    /**
     * @return The settings value for whether notification display is enabled.
     */
    public Boolean areNotificationsEnabled() {
        return this.notificationsEnabled;
    }

    // File directory formatting
    /**
     * If there is no forward slash at the end of the target directory,
     * concatenate the target directory string with a forward slash.
     */
    private void concatSlash() {
        if (fileDirectory.length() > 0) {
            if (fileDirectory.charAt(fileDirectory.length() - 1) != SLASH_CHARACTER) {
                fileDirectory = fileDirectory + SLASH_CHARACTER;
            }
        }
    }
    
    // Debugging methods
    /**
     * @return The current file load status of the program.
     */
    public String getLoadStatus() {
        return this.loadStatus;
    }
    
    /**
     * @return The current file write status of the program.
     */
    public String getWriteStatus() {
        return this.writeStatus;
    }
    
}
