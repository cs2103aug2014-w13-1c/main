//@author A0111987X
package app.exceptions;

/**
 * Custom exception class to handle invalid inputs.
 */
public class InvalidInputException extends Exception {

    /**
     * Calls parent (Exception) with Invalid Input Exception message.
     *
     * @param message Error message that describes the exception.
     */
    public InvalidInputException(String message) {
        super("Invalid Input Exception: " + message);
    }
}
