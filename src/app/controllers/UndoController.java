//@author A0111987X
package app.controllers;

import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Stack;

/**
 * In charge of undos and redos.
 * Implements undos and redos as two stacks of ArrayLists of TodoItems.
 * Implemented as a singleton.
 */
public class UndoController {

    private static UndoController self;
    private static Stack<ArrayList<TodoItem>> undoStack;
    private static Stack<ArrayList<TodoItem>> redoStack;

    /**
     * Initialises both the undo and redo stacks.
     */
    private UndoController() {
        undoStack = new Stack<ArrayList<TodoItem>>();
        redoStack = new Stack<ArrayList<TodoItem>>();
    }

    /**
     * Getter for UndoController.
     *
     * @return UndoController (singleton pattern)
     */
    protected static UndoController getUndoController() {
        if (self == null) {
            self = new UndoController();
        }
        return self;
    }

    /**
     * Saves an ArrayList of TodoItems to the undo stack using deep cloning.
     *
     * @param list ArrayList of TodoItems to be saved.
     */
    protected void saveUndo(ArrayList<TodoItem> list) {
        ArrayList<TodoItem> undo = new ArrayList<TodoItem>();
        for (TodoItem todo : list) {
            undo.add(new TodoItem(todo.getTaskName(),
                    todo.getStartDate(),
                    todo.getEndDate(),
                    todo.getPriority(),
                    todo.isDone()));
        }
        undoStack.push(undo);
    }

    /**
     * Saves an ArrayList of TodoItems to the redo stack using deep cloning.
     *
     * @param list ArrayList of TodoItems to be saved.
     */
    protected void saveRedo(ArrayList<TodoItem> list) {
        ArrayList<TodoItem> redo = new ArrayList<TodoItem>();
        for (TodoItem todo : list) {
            redo.add(new TodoItem(todo.getTaskName(),
                    todo.getStartDate(),
                    todo.getEndDate(),
                    todo.getPriority(),
                    todo.isDone()));
        }
        redoStack.push(redo);
    }

    /**
     * Checks if undo stack is empty.
     *
     * @return Status of undo stack.
     */
    public boolean isUndoEmpty() {
        return undoStack.isEmpty();
    }

    /**
     * Checks if redo stack is empty.
     *
     * @return Status of redo stack.
     */
    public boolean isRedoEmpty() {
        return redoStack.isEmpty();
    }

    /**
     * Pops an ArrayList of TodoItems from the undo stack.
     *
     * @return ArrayList of TodoItems
     */
    protected ArrayList<TodoItem> loadUndo() {
        return undoStack.pop();
    }

    /**
     * Pops an ArrayList of TodoItems from the redo stack.
     *
     * @return ArrayList of TodoItems
     */
    protected ArrayList<TodoItem> loadRedo() {
        return redoStack.pop();
    }

    /**
     * Clears both the undo and redo stack.
     */
    protected void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    /**
     * Clears the redo stack.
     */
    protected void clearRedo() {
        redoStack.clear();
    }

}
