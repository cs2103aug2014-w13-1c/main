import app.Main;
import org.junit.Test;
import org.loadui.testfx.utils.FXTestUtils;

/**
 * Created by jolly on 28/10/14.
 */
public class TestFXTest {

    @Test
    public void test1() {
        // Commands to be carried out
        String directoryCommand1 = "saveto testDirectory/";
        String clearCommand = "clear";
        String addCommand1 = "add task 1";
        String addCommand2 = "add task 2 priority high";
        String addCommand3 = "add task 3 due tomorrow";
        String addCommand4 = "add task 4 start today end tomorrow";
        String[] testCommands = {directoryCommand1, clearCommand, addCommand1, addCommand2, addCommand3, addCommand4};

        FXTestUtils.launchApp(Main.class, testCommands);
    }
}
