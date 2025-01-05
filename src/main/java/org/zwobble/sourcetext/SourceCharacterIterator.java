package org.zwobble.sourcetext;

import java.nio.CharBuffer;

public class SourceCharacterIterator {
    public static SourceCharacterIterator from(String text) {
        return new SourceCharacterIterator(text);
    }

    private final SourceText sourceText;
    private int characterIndex;

    private SourceCharacterIterator(String text) {
        this.sourceText = SourceText.fromString(text);
        this.characterIndex = 0;
    }

    public boolean isEnd() {
        return this.characterIndex >= this.sourceText.characterLength();
    }

    public int remaining() {
        return this.sourceText.characterLength() - this.characterIndex;
    }

    public int peek() {
        if (this.characterIndex >= this.sourceText.characterLength()) {
            return -1;
        }

        return this.sourceText.getCharacter(this.characterIndex);
    }

    public CharBuffer peekSequence(int length) {
        return this.sourceText.charBuffer(
            this.characterIndex,
            this.characterIndex + length
        );
    }

    public void skip() {
        if (this.characterIndex < this.sourceText.characterLength()) {
            this.characterIndex += 1;
        }
    }

    public void skip(int length) {
        this.characterIndex = Math.min(
            this.characterIndex + length,
            this.sourceText.characterLength()
        );
    }

    public SourceRange characterSourceRange() {
        var end = isEnd()
            ? position()
            : new SourcePosition(this.characterIndex + 1);

        return new SourceRange(sourceText, position(), end);
    }

    public SourcePosition position() {
        return new SourcePosition(this.characterIndex);
    }

    public void position(SourcePosition position) {
        this.characterIndex = position.characterIndex();
    }

    public SourceRange sourceRange(
        SourcePosition start,
        SourcePosition end
    ) {
        return new SourceRange(sourceText, start, end);
    }
}
