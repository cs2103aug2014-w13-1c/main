//@author A0111987X
package app.helpers;

/**
 * Custom exception class to handle invalid inputs.
 */
public class InvalidInputException extends Exception {
    /**
     * Calls parent (Exception) with Invalid Input Exception message.
     *
     * @param message Error message
     */
    public InvalidInputException(String message) {
        super("Invalid Input Exception: " + message);
    }
}
