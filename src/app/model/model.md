Model API for wat do
=========
What it can do
------------
Stores data in program memory and do file I/O

Constructors
------------
* public ModelManager()
* public TodoItem (String taskName, Date startDate, Date endDate, String priority, Boolean doneStatus)
 * Everything can be null
 * Use priority as TodoItem.HIGH, TodoItem.MEDIUM and TodoItem.LOW
 * If no priority is specified, automatically assumed to be Medium
* public TodoItem (String taskName, Date startDate, Date endDate)
 * Calls above method with MEDIUM priority and false doneStatus
* public TodoItemList() _creates empty list of todo items with file name watdo.txt_
* public TodoItemList(ArrayList<TodoItem> todoItemArrayList) _initializes to todoItemArrayList_

ModelManager
-----------
### CRUD
* public void addTask(String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException
 * Resorts based on current sorting style after add
* public void updateTask(UUID itemID, Boolean[] parameters, String newTaskName, Date newStartDate, Date newEndDate, String newPriority, Boolean newDoneStatus) throws IOException
 * Resorts based on current sorting style after update
 * Set parameters[0] to true if you wish to update taskName
 * Set parameters[1] to true if you wish to update startDate
 * etc.
 * parameters should always be 5 elements long
* public TodoItem deleteTask(UUID itemID) throws IOException
* public void clearTasks() throws IOException

### File storage
* public void changeFileDirectory(String fileDirectory) throws IOException

### Sorting
* public void setSortingStyle(int newSortingStyle)
 * 1 = task name
 * 2 = start date
 * 3 = end date
 * 4 = priority
 
### Getters
* public ArrayList<TodoItem> getTodoItemList() {
* public ListIterator<TodoItem> getTodoItemIterator() {

### Count
* public int countTasks()

TodoItem
-----------
### Attribute getters and setters
* getTaskName() - setTaskName(String newTaskName)
* getStartDate() - setStartDate(Date newStartDate)
* getEndDate() - setEndDate(Date newEndDate)
* getPriority() - setPriority(String newPriority)
 * newPriority must be TodoItem.HIGH/MID/LOW
* isDone() - setDoneStatus(Boolean newDoneStatus)
 * newDoneStatus must not be null
* getUUID()
 * No setter for UUID (defensive coding)

### Convenience methods for View & Controller
* public boolean isOverdue()
 * true if end date exists and current date is later than end date
 * false otherwise
* public String getTodoItemType()
 * _startDate_ + _endDate_ = **"Event"**
 * no _startDate_ + _endDate_ = **"Deadline"**
 * no _startDate_ +  no _endDate_ = **"Floating"**
 * _startDate_ + no _endDate_ = **"Endless"**
* public String getStartDateString() - getEndDateString()
 * returns the start date as a string
 * if not set, returns null