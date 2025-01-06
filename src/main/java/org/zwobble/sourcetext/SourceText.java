package org.zwobble.sourcetext;

import java.nio.CharBuffer;

public class SourceText {
    public static SourceText fromString(String name, String string) {
        return new SourceText(name, CharBuffer.wrap(string));
    }

    private final String name;
    private final CharBuffer charBuffer;

    private SourceText(String name, CharBuffer charBuffer) {
        this.name = name;
        this.charBuffer = charBuffer;
    }

    public String name() {
        return this.name;
    }

    public CharSequence charSequence(
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

    public SourceCharacterIterator characterIterator() {
        return new SourceCharacterIterator(this);
    }
}
