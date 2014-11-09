package tests.units;

//author A0111764L
import app.helpers.LoggingService;
import org.junit.Test;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoggingServiceTest {

    /**
     * Test if the logger object returned by LoggingService
     * is always the same object, therefore writing to the same
     * log file.
     */
    @Test
    public void testSingleton() {
        Logger logger = LoggingService.getLogger();
        Logger logger2 = LoggingService.getLogger();
        assertThat(logger, is(logger2));
    }

    /**
     * Test that the file is created by LoggingService
     * and it is readable.
     */
    @Test
    public void testLogfileCreation() {
        Logger logger = LoggingService.getLogger();
        logger.log(Level.INFO, "Test log");

        File log = new File("./logs/watdo.log"); // This does not create a file
        assert(log.isFile() && log.canRead());
        log.delete();
    }

    /**
     * Test that the logger successfully logs and entry into the file.
     */
    @Test
    public void testLogsWritten() {
        File log = new File("./logs/watdo.log");
        log.delete();
        assertThat(log.exists(), is(false));

        /* First test that the boundary case for empty file is true */
        assertThat(log.length() == 0, is(true));

        LoggingService.getLogger().log(Level.INFO, "foo");

        /* Then test that the boundary case for non-empty file is true */
        assertThat(log.length() > 0, is(true));
    }


}
