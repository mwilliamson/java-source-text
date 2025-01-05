package org.zwobble.sourcetext;

import org.zwobble.precisely.Matcher;

import static org.zwobble.precisely.Matchers.*;

public class SourcePositionMatchers {
    private SourcePositionMatchers() {
    }

    public static Matcher<SourcePosition> isJson5SourcePosition(
        int characterIndex
    ) {
        return has("characterIndex", x -> x.characterIndex(), equalTo(characterIndex));
    }
}
