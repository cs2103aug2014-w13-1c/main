package app.helpers;

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

    public static StyleSpans<Collection<String>> getStyleSpans(ArrayList<Keyword> keywords, String command) {
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        int lastWordEnd = 0;
        for (Keyword keyword : keywords) {
            spansBuilder.add(Collections.emptyList(), keyword.getStartIndex() - lastWordEnd);
            spansBuilder.add(Collections.singleton("keyword"), keyword.getEndIndex() - keyword.getStartIndex() + 1);
            lastWordEnd = keyword.getEndIndex() + 1;
        }
        spansBuilder.add(Collections.emptyList(), command.length() - lastWordEnd);
        return spansBuilder.create();
    }

}
