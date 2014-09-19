Model API for wat do
=========
What it can do
------------
Stores data in program memory. No File I/O yet.

Constructors
------------
* public TodoItemList() _creates empty list of todo items_
* public TodoItem (String itemAction, LocalDate startDate, LocalDate endDate)
 * itemAction, startDate, endDate can be null
 * itemAction shouldn't be null
 * _startDate_ + _endDate_ = **Event**
 * no _startDate_ + _endDate_ = **Deadline**
 * no _startDate_ +  no _endDate_ = **Floating**

CRUD methods: TodoItemList
-----------
* public void addTodoItem(TodoItem newItem)
* public TodoItem readTodoItem(int index) _returns null if index out of bounds_
* public void updateTodoItem(int index, String itemAction, LocalDate startDate, LocalDate endDate)
* public TodoItem deleteTodoItem(int index) _returns null if index out of bounds_

GET methods: TodoItem
-----------
Return null if not set.

* itemAction
 * public String getItemAction()
 * public StringProperty getItemActionProperty()
* startDate
 * public LocalDate getStartDate()
 * public ObjectProperty<LocalDate> getStartDateProperty()
* endDate
 * public LocalDate getEndDate()
 * public ObjectProperty<LocalDate> getEndDateProperty()

SET Methods: TodoItem
------------
Create new Property if not set.

* itemAction
  * public void setItemAction(String itemAction)
  * public void setItemActionProperty(StringProperty itemAction)
* startDate
  * public void setStartDate(LocalDate startDate)
  * public void setStartDateProperty(ObjectProperty<LocalDate> startDate)
* endDate
  * public void setEndDate(LocalDate endDate)
  * public void setEndDateProperty(ObjectProperty<LocalDate> endDate)

Other methods: TodoItem
-------------
* public String getTodoItemType()
 * returns "Event" for Event
 * returns "Deadline" for Deadline
 * returns "Floating" for Floating
 * returns "Invalid" otherwise