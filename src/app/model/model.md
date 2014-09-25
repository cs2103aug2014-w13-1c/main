Model API for wat do
=========
What it can do
------------
Stores data in program memory. No File I/O yet.

Constructors
------------
* public TodoItemList() _creates empty list of todo items with file name watdo.txt_
* public TodoItemList(String fileName) _loads todo items from provided file name_ **still implementing**
* public TodoItem (String taskName, Date startDate, Date endDate)
 * taskName, startDate, endDate can be null
 * taskName shouldn't be null
 * _startDate_ + _endDate_ = **Event**
 * no _startDate_ + _endDate_ = **Deadline**
 * no _startDate_ +  no _endDate_ = **Floating**
 * _startDate_ + no _endDate_ = **Floating**

CRUD methods: TodoItemList
-----------
* TodoItem
 * public void addTodoItem(TodoItem newItem)
 * public TodoItem readTodoItem(int index) _returns null if index out of bounds_
 * public void updateTodoItem(int index, String taskName, Date startDate, Date endDate)
 * public TodoItem deleteTodoItem(int index) _returns null if index out of bounds_
 * public void clearTodoItems()
* TodoItemList
 * public ArrayList<TodoItem> getTodoItems() 
 * public ListIterator<TodoItem> getTodoItemIterator() 

GET methods: TodoItem
-----------
Return null if not set.

* taskName
 * public String getTaskName()
 * public StringProperty getTaskNameProperty()
* startDate
 * public Date getStartDate()
 * public ObjectProperty<Date> getStartDateProperty()
* endDate
 * public LocalDate getEndDate()
 * public ObjectProperty<Date> getEndDateProperty()

SET Methods: TodoItem
------------
Create new Property if not set.

* taskName
  * public void setTaskName(String taskName)
  * public void setTaskNameProperty(StringProperty taskName)
* startDate
  * public void setStartDate(LocalDate startDate)
  * public void setStartDateProperty(ObjectProperty<Date> startDate)
* endDate
  * public void setEndDate(LocalDate endDate)
  * public void setEndDateProperty(ObjectProperty<Date> endDate)

Other methods: TodoItem
-------------
* public String getTodoItemType()
 * returns "Event" for Event
 * returns "Deadline" for Deadline
 * returns "Floating" for Floating
 * returns "Invalid" otherwise
* public String getStartDateString()
 * returns the start date as a string
 * if not set, returns null
* public String getEndDateString()
 * returns the end date as a string
 * if not set, returns null
 
Other methods: TodoItemList
-------------
* public void changeFile() **still implementing**
 * Switches the current file to a different file
* public String getFileName()
 * Returns the name of the current file being used
* public ArrayList<TodoItem> getTodoItems()
 * Returns the entire list of TodoItems
* public String getLoadStatus()
 * Gets the load status of the file. For I/O error checking.
* public String getWriteStatus()
 * Gets the write status of the file. For I/O error checking.
* public int countTodoItems()
 * Gets the number of TodoItems