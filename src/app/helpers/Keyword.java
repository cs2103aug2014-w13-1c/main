package app.helpers;

/**
 * Created by jolly on 10/10/14.
 */
public class Keyword {

    private int startIndex;
    private int endIndex;

    public Keyword(int start, int end) {
        startIndex = start;
        endIndex = end;
    }
        
    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
}
