package app.helpers;

/**
 * Created by jolly on 10/10/14. Edited by Ryan on 14/10/14.
 */
public class Keyword {

    private int startIndex;
    private int endIndex;
    private String word;
    
    // do we want odd the ability to specify colour of keyword?

    public Keyword(int start, int end) {
        startIndex = start;
        endIndex = end;
        word = "";
    }
    
    public void setWord(String key) {
        word = key;
    }
    
    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }
    
    public String getWord() {
        return word;
    }
}
