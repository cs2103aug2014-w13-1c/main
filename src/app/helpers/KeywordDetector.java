package app.helpers;

import app.viewmanagers.InputFieldViewManager;
import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Takes in an arraylist of keywords and returns a stylespans collection.
 *
 * Created by jolly on 15/10/14.
 */
public class KeywordDetector {

    private static KeywordDetector self;
    private static InputFieldViewManager inputFieldViewManager;

    private KeywordDetector() {
        // do nothing
    }

    public static KeywordDetector getKeywordDetector() {
        if (self == null) {
            self = new KeywordDetector();
        }
        return self;
    }

    public static StyleSpans<Collection<String>> getStyleSpans(ArrayList<Keyword> keywords, String command) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        inputFieldViewManager.setSelectIndex(-1);
        int lastWordEnd = 0;
        for (Keyword keyword : keywords) {
            if (keyword.getSelectIndex() == -1) {
                spansBuilder.add(Collections.emptyList(), keyword.getStartIndex() - lastWordEnd);
//            System.out.println("keyword length: " + (keyword.getEndIndex() - keyword.getStartIndex() + 1));
                spansBuilder.add(Collections.singleton("keyword"), keyword.getEndIndex() - keyword.getStartIndex() + 1);
                lastWordEnd = keyword.getEndIndex() + 1;
            } else {
                inputFieldViewManager.setSelectIndex(keyword.getSelectIndex());
            }
        }
        spansBuilder.add(Collections.emptyList(), command.length() - lastWordEnd);
        return spansBuilder.create();
    }

    public void setInputFieldViewManager(InputFieldViewManager manager) {
        inputFieldViewManager = manager;
    }

}
