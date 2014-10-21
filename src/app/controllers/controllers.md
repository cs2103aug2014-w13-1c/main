Controller API for wat do
=========
What it can do
------------
Act as a "communication bridge" between the View and the Model. It parses commands.

Constructors
------------
* public CommandController()

CommandController
-----------------
### Command parsing
* public void parseCommand(String inputString)
* public ArrayList<Keyword> parseKeywords(String inputString)

### View updating
* public ObservableList<TodoItem> convertList(ArrayList<TodoItem> todoList)
* public void updateView()
* public void updateView(ArrayList<TodoItem> todoItems)
* public ArrayList<TodoItem> getTaskList()
* public void setTaskList(ArrayList<TodoItem> todoList)
* public void resetTaskList()