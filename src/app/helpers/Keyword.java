package app.helpers;

/**
 * Created by jolly on 10/10/14.
 */
public class Keyword {

    private int startIndex;
    private int endIndex;
    private int selectIndex;
    
    // do we want odd the ability to specify colour of keyword?

    public Keyword(int start, int end) {
        startIndex = start;
        endIndex = end;
        selectIndex = -1;
    }
        
    public int getStartIndex() {
        return startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setSelectIndex(int index) {
        selectIndex = index;
    }

    public int getSelectIndex() {
        return selectIndex;
    }
}
