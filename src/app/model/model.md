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
* public TodoItemList() _creates empty list of todo items with file name watdo.txt_
* public TodoItemList(String fileName) _loads todo items from provided file name_

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

TodoItemList (MAJOR OVERHAUL INCOMING)
-----------
### CRUD methods: TodoItemList
* TodoItem
 * public void addTodoItem(TodoItem newItem)
 * public TodoItem readTodoItem(int index) _returns null if index out of bounds_
 * public void updateTodoItem(int index, String taskName, Date startDate, Date endDate)
 * public void updateTodoItem(int index, String taskName, Date startDate, Date endDate, String priority)
  * If specified priority is not TodoItem.HIGH/MEDIUM/LOW, current priority will not be changed
 * public TodoItem deleteTodoItem(int index) _returns null if index out of bounds_
 * public void clearTodoItems()

### GET methods: TodoItemList
-----------
* public ArrayList<TodoItem> getTodoItems() 
* public ListIterator<TodoItem> getTodoItemIterator()
* public String getFileName()
* public String getLoadStatus()
* public String getWriteStatus()

### Other methods: TodoItemList
* public void changeFile()
 * Switches the current file in use to a different file
* public int countTodoItems()
 * Gets the number of TodoItems
* public void markDoneStatus(int index, Boolean doneStatus)
 * Marks the TodoItem's doneStatus based on the doneStatus in the argument