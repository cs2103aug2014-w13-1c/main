Model API for wat do
=========
What it can do
------------
Stores data in program memory. No File I/O yet.

Constructors
------------
* public TodoItemList() _creates empty list of todo items with file name watdo.txt_
* public TodoItemList(String fileName) _loads todo items from provided file name_
* public TodoItem (String itemAction, Date startDate, Date endDate)
 * itemAction, startDate, endDate can be null
 * itemAction shouldn't be null
 * _startDate_ + _endDate_ = **Event**
 * no _startDate_ + _endDate_ = **Deadline**
 * no _startDate_ +  no _endDate_ = **Floating**

CRUD methods: TodoItemList
-----------
* public void addTodoItem(TodoItem newItem)
* public TodoItem readTodoItem(int index) _returns null if index out of bounds_
* public void updateTodoItem(int index, String itemAction, Date startDate, Date endDate)
* public TodoItem deleteTodoItem(int index) _returns null if index out of bounds_

GET methods: TodoItem
-----------
Return null if not set.

* itemAction
 * public String getItemAction()
 * public StringProperty getItemActionProperty()
* startDate
 * public Date getStartDate()
 * public ObjectProperty<Date> getStartDateProperty()
* endDate
 * public LocalDate getEndDate()
 * public ObjectProperty<Date> getEndDateProperty()

SET Methods: TodoItem
------------
Create new Property if not set.

* itemAction
  * public void setItemAction(String itemAction)
  * public void setItemActionProperty(StringProperty itemAction)
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