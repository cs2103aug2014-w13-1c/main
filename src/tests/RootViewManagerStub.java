//@author A0111987X
package tests;

import app.viewmanagers.RootViewManager;
import app.viewmanagers.TaskListViewManager;
import app.viewmanagers.TitleBarViewManager;

/**
 * RootViewManager stub created for TaskControllerTest.
 */
public class RootViewManagerStub extends RootViewManager {

    /**
     * Empty constructor.
     */
    RootViewManagerStub() {
        // do nothing
    }

    /**
     * Returns a new TitleBarViewManagerStub.
     *
     * @return TitleBarViewManagerStub
     */
    @Override
    public TitleBarViewManager getTitleBarViewManager() {
        return new TitleBarViewManagerStub();
    }

    /**
     * Returns a new TaskListViewManagerStub.
     *
     * @return TaskListViewManagerStub
     */
    @Override
    public TaskListViewManager getTaskListViewManager() {
        return new TaskListViewManagerStub();
    }
}
