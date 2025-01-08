package org.zwobble.sourcetext;

/**
 * A position in a source text. Rather than referring to a specific character,
 * a position is between characters, or at the start or end of the source text.
 */
public class SourcePosition {
    private final SourceText sourceText;
    private final int characterIndex;

    public SourcePosition(SourceText sourceText, int characterIndex) {
        this.sourceText = sourceText;
        this.characterIndex = characterIndex;
    }

    public int characterIndex() {
        return characterIndex;
    }

    public SourceRange to(SourcePosition end) {
        return new SourceRange(this.sourceText, this, end);
    }
}
