//@author A0111987X
package app.controllers;

import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Stack;

/**
 * in charge of undos and redos
 * implemented as a singleton
 * Created by jolly on 27/10/14.
 */
public class UndoController {

    private static UndoController self;
    private static Stack<ArrayList<TodoItem>> undoStack;
    private static Stack<ArrayList<TodoItem>> redoStack;

    private UndoController() {
        undoStack = new Stack<ArrayList<TodoItem>>();
        redoStack = new Stack<ArrayList<TodoItem>>();
    }

    protected static UndoController getUndoController() {
        if (self == null) {
            self = new UndoController();
        }
        return self;
    }

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

    public boolean isUndoEmpty() {
        return undoStack.isEmpty();
    }

    public boolean isRedoEmpty() {
        return redoStack.isEmpty();
    }

    protected ArrayList<TodoItem> loadUndo() {
        return undoStack.pop();
    }

    protected ArrayList<TodoItem> loadRedo() {
        return redoStack.pop();
    }

    protected void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    protected void clearRedo() {
        redoStack.clear();
    }

}
