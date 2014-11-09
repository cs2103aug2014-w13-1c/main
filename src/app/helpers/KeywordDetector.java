//@author A0111987X
package app.helpers;

import org.fxmisc.richtext.StyleSpans;
import org.fxmisc.richtext.StyleSpansBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Helper class in charge of converting an ArrayList of Keywords which contain indexes to words that the
 * CommandParser has recognised, to a StyleSpans collection which is used to tell the inputField which
 * words to highlight.
 */
public class KeywordDetector {

    /**
     * Takes in an ArrayList of Keywords and returns a StyleSpans collection which is then used
     * for keyword highlighting.
     *
     * Code is adapted from:
     * https://github.com/TomasMikula/RichTextFX/blob/master/richtextfx-demos/src/main/java/org/fxmisc/richtext/demo/JavaKeywords.java
     *
     * @param keywords  ArrayList of Keywords.
     * @param command   Command string that was entered.
     * @return          StyleSpans collection.
     */
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
