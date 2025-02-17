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

    @Override
    public String toString() {
        var lineIndex = 0;
        var columnIndex = 0;

        for (
            var characterIndex = 0;
            characterIndex <= this.sourceText.characterLength();
            characterIndex++
        ) {
            if (characterIndex == this.characterIndex || characterIndex == this.sourceText.characterLength()) {
                break;
            }

            if (this.sourceText.getCharacter(characterIndex) == '\n') {
                lineIndex += 1;
                columnIndex = 0;
            } else {
                columnIndex += 1;
            }
        }

        return String.format(
            "%s:%s:%s",
            this.sourceText.name(),
            lineIndex + 1,
            columnIndex + 1
        );
    }
}
