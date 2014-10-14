package app.helpers;

import java.util.logging.Logger;

/**
 * This is a singleton class for Logging.
 *
 * The logger can only be referenced by calling getLogger on this class, and
 * because it's a singleton, it'll always refer back to the same logger object.
 *
 * Created by jin on 14/10/14.
 */
public class LoggingService {

    private Logger logger;
    private static LoggingService self;

    private LoggingService() {
       logger = Logger.getLogger(this.getClass().getName());
    }

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
