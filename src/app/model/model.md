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
 * 1 = taskName then endDate
 * 2 = startDate then priority
 * 3 = endDate then priority
 * 4 = priority then endDate
 * Sort is stable.
 * Null values are pushed to the end of the list.
 
### Getters
* public ArrayList<TodoItem> getTodoItemList() {
* public ListIterator<TodoItem> getTodoItemIterator() {

### Count
* public int countTasks()

### Display done tasks
* public void setDoneDisplay(Boolean displayDone)
 * If _True_, subsequent getTodoItemList()/getTodoItemIterator() calls will return the entire list of tasks
 * If _False_, subsequent getTodoItemList()/getTodoItemIterator() calls will return just the not-done list of tasks
 * At program startup, displayDone is false. You will not see the entire list of tasks unless "display all" is typed.

### Last modified
* public int getLastModifiedIndex()

TodoItem
-----------
### Attribute getters and setters
* getTaskName()
* getStartDate()
* getEndDate()
* getPriority()
* isDone()
* getUUID()

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
 * returns the start/end date as a string
 * if not set, returns null