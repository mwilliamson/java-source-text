package org.zwobble.sourcetext;

import org.zwobble.precisely.Matcher;

import static org.zwobble.sourcetext.SourcePositionMatchers.isJson5SourcePosition;
import static org.zwobble.precisely.Matchers.*;

public class SourceRangeMatchers {
    private SourceRangeMatchers() {
    }

    public static Matcher<SourceRange> isJson5SourceRange(
        int startCharacterIndex,
        int endCharacterIndex
    ) {
        return allOf(
            has("start", x -> x.start(), isJson5SourcePosition(startCharacterIndex)),
            has("end", x -> x.end(), isJson5SourcePosition(endCharacterIndex))
        );
    }
}
