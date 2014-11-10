package app.helpers;

//@author A0111764L
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

/**
 * This is a singleton class for Logging.
 *
 * The logger can only be referenced by calling getLogger on this class, and
 * because it's a singleton, it'll always refer back to the same logger object.
 *
 */
public class LoggingService {

    private Logger logger;
    private static LoggingService self;

    /**
     * Private constructor of the Service object.
     */
    private LoggingService() {
       logger = Logger.getLogger(this.getClass().getName());
        try {
            File target = new File("./logs");
            target.mkdir();
            Handler fh = new FileHandler("./logs/watdo.log");
            logger.addHandler(fh);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return instance of Logger object
     */
    private static LoggingService getInstance() {
        if (self == null) {
            self = new LoggingService();
        }
        return self;
    }

    public static Logger getLogger() {
        return getInstance().logger;
    }

}
