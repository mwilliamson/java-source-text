package org.zwobble.sourcetext;

public class SourceCharacterIterator {
    private final SourceText sourceText;
    private int characterIndex;

    SourceCharacterIterator(SourceText sourceText) {
        this.sourceText = sourceText;
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

    public CharSequence peekSequence(int length) {
        return this.sourceText.charSequence(
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
            : this.sourceText.characterPosition(this.characterIndex + 1);

        return position().to(end);
    }

    public SourcePosition position() {
        return this.sourceText.characterPosition(this.characterIndex);
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
