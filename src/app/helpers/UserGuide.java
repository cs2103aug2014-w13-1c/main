//@author A0111987X
package app.helpers;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;

/**
 * Created by jolly on 10/10/14.
 */
public class UserGuide {

    private GridPane userGuide;

    public UserGuide() {
        userGuide = new GridPane();
        userGuide.setAlignment(Pos.CENTER);
        String family = "Helvetica";
        double size = 30;
        TextFlow textFlow = new TextFlow();
        textFlow.setTextAlignment(TextAlignment.CENTER);
        textFlow.setLineSpacing(10);

        Text text1 = new Text("Welcome to wat do\n\n");
        text1.setFont(Font.font(family, FontWeight.BOLD, size + 10));

        Text text2 = new Text("To add a new task, enter:\n");
        text2.setFont(Font.font(family, size));

        Text text3 = new Text("add ");
        text3.setFont(Font.font(family, FontWeight.BOLD, size));

        Text text4 = new Text("<task name>\n\n");
        text4.setFont(Font.font(family, FontPosture.ITALIC, size));

        Text text5 = new Text("Type ");
        text5.setFont(Font.font(family, size));

        Text text6 = new Text("help");
        text6.setFont(Font.font(family, FontWeight.BOLD, size));

        Text text7 = new Text(" for more commands");
        text7.setFont(Font.font(family, size));

        textFlow.getChildren().addAll(text1, text2, text3, text4, text5, text6, text7);
        userGuide.add(textFlow, 0, 0);
    }

    public GridPane getUserGuide() {
        return userGuide;
    }

}
