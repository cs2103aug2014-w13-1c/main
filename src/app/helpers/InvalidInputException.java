package app.helpers;

/**
 * Custom exception class to handle invalid inputs.
 * Created by jolly on 14/10/14.
 */
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) {
        super("Invalid Input Exception: " + message);
    }
}
