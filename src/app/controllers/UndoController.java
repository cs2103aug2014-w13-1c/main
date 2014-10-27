package app.controllers;

import app.model.TodoItem;

import java.util.ArrayList;
import java.util.Stack;

/**
 * in charge of undos
 * implemented as a singleton
 *
 * Created by jolly on 27/10/14.
 */
public class UndoController {

    private static UndoController self;
    private static Stack<ArrayList<TodoItem>> undoStack;

    private UndoController() {
        undoStack = new Stack<ArrayList<TodoItem>>();
    }

    protected static UndoController getUndoController() {
        if (self == null) {
            self = new UndoController();
        }
        return self;
    }

    protected void saveUndo(ArrayList<TodoItem> list) {
        undoStack.push(list);
    }

    protected boolean isEmpty() {
        return undoStack.isEmpty();
    }

    protected ArrayList<TodoItem> loadUndo() {
        return undoStack.pop();
    }

}
