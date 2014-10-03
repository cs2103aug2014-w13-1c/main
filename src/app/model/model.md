Model API for wat do
=========
What it can do
------------
Stores data in program memory. No File I/O yet.

Constructors
------------
TodoItemList constructors will load data from the file.
If no fileName is specified, watdo.json will be used. 

* public TodoItemList() _creates empty list of todo items with file name watdo.txt_
* public TodoItemList(String fileName) _loads todo items from provided file name_ **still implementing**
* public TodoItem (String taskName, Date startDate, Date endDate)
* public TodoItem (String taskName, Date startDate, Date endDate, String priority)
 * taskName, startDate, endDate, priority can be null
 * taskName shouldn't be null
 * _startDate_ + _endDate_ = **Event**
 * no _startDate_ + _endDate_ = **Deadline**
 * no _startDate_ +  no _endDate_ = **Floating**
 * _startDate_ + no _endDate_ = **Floating**
 * Use priority as TodoItem.HIGH, TodoItem.MEDIUM and TodoItem.LOW
 * If no priority is specified, automatically assumed to be Medium

CRUD methods: TodoItemList
-----------
* TodoItem
 * public void addTodoItem(TodoItem newItem)
 * public TodoItem readTodoItem(int index) _returns null if index out of bounds_
 * public void updateTodoItem(int index, String taskName, Date startDate, Date endDate)
 * public void updateTodoItem(int index, String taskName, Date startDate, Date endDate, String priority)
  * If specified priority is not TodoItem.HIGH/MEDIUM/LOW, current priority will not be changed
 * public TodoItem deleteTodoItem(int index) _returns null if index out of bounds_
 * public void clearTodoItems()

GET methods: TodoItemList
-----------
* public ArrayList<TodoItem> getTodoItems() 
* public ListIterator<TodoItem> getTodoItemIterator()
* public String getFileName()
* public String getLoadStatus()
* public String getWriteStatus()

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
* priority
 * public String getPriority()

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
* priority
  * public String setPriority(String priority) _if not TodoItem.HIGH/MEDIUM/LOW, no change made_

Other methods: TodoItem
-------------
* public boolean isOverdue()
 * true if end date exists and current date is later than end date
 * false otherwise
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
* public void changeFile()
 * Switches the current file in use to a different file
* public int countTodoItems()
 * Gets the number of TodoItems