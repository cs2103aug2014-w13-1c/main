package app.viewmanagers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Created by jolly on 10/10/14.
 */
public class HelpViewManager {

    @FXML
    private Button okButton;

    private RootViewManager rootViewManager;

    @FXML
    private Label helpCommands;

    @FXML
    private void initialize() {
        helpCommands.setText("Here are the other commands you can use:\n" +
                "\n" +
                "add <task name> [start | end | priority]\n" +
                "delete <task index>\n" +
                "done <task index>\n" +
                "undone <task index>\n" +
                "update <task index> [task name | start | end | priority]\n" +
                "search <task name>\n" +
                "search [start | end]\n" +
                "sort [start / end / priority]\n" +
                "display [done / all / overdue]\n" +
                "undo\n" +
                "redo\n" +
                "help\n" +
                "settings\n" +
                "clear\n" +
                "exit");
        okButton.setOnAction((event) -> rootViewManager.closeHelp());
    }

    public void setRootViewManager(RootViewManager rootViewManager) {
        this.rootViewManager = rootViewManager;
    }

    public void focusOnButton() {
        okButton.setDefaultButton(true);
        okButton.requestFocus();
    }

    public void cancelFocusOnButton() {
        okButton.setDefaultButton(false);
    }
}
