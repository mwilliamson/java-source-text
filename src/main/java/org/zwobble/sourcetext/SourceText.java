package org.zwobble.sourcetext;

import java.nio.CharBuffer;

public class SourceText {
    public static SourceText fromString(String string) {
        return new SourceText(CharBuffer.wrap(string));
    }

    private final CharBuffer charBuffer;

    private SourceText(CharBuffer charBuffer) {
        this.charBuffer = charBuffer;
    }

    public CharBuffer charBuffer(
        int startCharacterIndex,
        int endCharacterIndex
    ) {
        return this.charBuffer.subSequence(
            startCharacterIndex,
            endCharacterIndex
        );
    }

    public int characterLength() {
        return this.charBuffer.length();
    }

    public int getCharacter(int characterIndex) {
        return this.charBuffer.get(characterIndex);
    }
}
