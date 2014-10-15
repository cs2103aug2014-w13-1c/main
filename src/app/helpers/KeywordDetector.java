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

        // for testing
//        ArrayList<Keyword> keywords = new ArrayList<Keyword>();
//        if (text.length() >= 3) {
//            keywords.add(new Keyword(0, 2));
//        }
//
//        if (text.length() >= 5) {
//            keywords.add(new Keyword(3, 4));
//        }
//
//        if (text.length() >= 10) {
//            keywords.add(new Keyword(7, 9));
//        }

        // for debugging
        if (keywords.size() == 0) {
            System.out.println("no keywords");
        }
        for (Keyword keyword : keywords) {
            System.out.println(keyword.getWord() + " " + keyword.getStartIndex() + " " + keyword.getEndIndex());
        }

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
