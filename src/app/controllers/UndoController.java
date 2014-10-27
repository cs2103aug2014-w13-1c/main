package app.controllers;

import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Stack;

/**
 * in charge of undos and redos
 * implemented as a singleton
 *
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
        undoStack.push(list);
        redoStack = new Stack<ArrayList<TodoItem>>();
    }

    protected boolean isUndoEmpty() {
        return undoStack.isEmpty();
    }

    protected boolean isRedoEmpty() {
        return redoStack.isEmpty();
    }

    protected ArrayList<TodoItem> loadUndo() {
        redoStack.push(undoStack.peek());
        return undoStack.pop();
    }

    protected ArrayList<TodoItem> loadRedo() {
        undoStack.push(redoStack.peek());
        return redoStack.pop();
    }

}
