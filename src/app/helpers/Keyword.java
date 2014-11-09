//@author A0111987X
package app.helpers;

/**
 * Class to store start and end indexes of words. Indexes are positions in the command string.
 * Used for keyword highlighting to pass indexes of words to tell the inputField which words to highlight.
 */
public class Keyword {

    private int startIndex;
    private int endIndex;

    /**
     * Constructor, takes in the start and end indexes of a word.
     * The word itself is not stored.
     *
     * @param start Start index of word.
     * @param end   End index of word.
     */
    public Keyword(int start, int end) {
        startIndex = start;
        endIndex = end;
    }

    /**
     * Getter for the startIndex.
     *
     * @return startIndex
     */
    public int getStartIndex() {
        return startIndex;
    }

    /**
     * Getter for the endIndex.
     *
     * @return endIndex
     */
    public int getEndIndex() {
        return endIndex;
    }
}
