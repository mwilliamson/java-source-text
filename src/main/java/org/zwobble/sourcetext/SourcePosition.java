package org.zwobble.sourcetext;

import java.util.Objects;

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

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof SourcePosition otherSourcePosition)) {
            return false;
        }

        return this.sourceText.equals(otherSourcePosition.sourceText) &&
            this.characterIndex == otherSourcePosition.characterIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sourceText, this.characterIndex);
    }
}
