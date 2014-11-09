//@author A0111987X
package tests;

import app.model.TodoItem;
import app.viewmanagers.TaskListViewManager;
import javafx.collections.ObservableList;

/**
 * TaskListViewManager stub created for TaskControllerTest.
 */
public class TaskListViewManagerStub extends TaskListViewManager {

    /**
     * Empty constructor.
     */
    TaskListViewManagerStub() {
        // do nothing
    }

    /**
     * Override for updateView that does nothing.
     *
     * @param list ObservableList of TodoItems to update the list with
     */
    @Override
    public void updateView(ObservableList<TodoItem> list) {
        // do nothing
    }
}
