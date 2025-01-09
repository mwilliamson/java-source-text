package org.zwobble.sourcetext;

import java.util.Objects;

public class SourceRange {
    private final SourceText sourceText;
    private final SourcePosition start;
    private final SourcePosition end;

    public SourceRange(
        SourceText sourceText,
        SourcePosition start,
        SourcePosition end
    ) {
        this.sourceText = sourceText;
        this.start = start;
        this.end = end;
    }

    public CharSequence charSequence() {
        return sourceText.charSequence(
            start.characterIndex(),
            end.characterIndex()
        );
    }

    public SourceText sourceText() {
        return this.sourceText;
    }

    public SourcePosition start() {
        return this.start;
    }

    public SourcePosition end() {
        return this.end;
    }

    public SourceRange to(SourceRange end) {
        return new SourceRange(
            this.sourceText,
            this.start,
            end.end
        );
    }

    public String describe() {
        var lineStartCharacterIndex = 0;
        var lineIndex = 0;
        var columnIndex = 0;

        for (
            var characterIndex = 0;
            characterIndex < this.sourceText.characterLength();
            characterIndex++
        ) {
            if (characterIndex == this.start.characterIndex()) {
                var lineEndCharacterIndex = lineStartCharacterIndex;
                while (
                    lineEndCharacterIndex < this.sourceText.characterLength() &&
                        this.sourceText.getCharacter(lineEndCharacterIndex) != '\n'
                ) {
                    lineEndCharacterIndex++;
                }
                return context(
                    this.sourceText.charSequence(
                        lineStartCharacterIndex,
                        lineEndCharacterIndex
                    ),
                    lineIndex,
                    columnIndex,
                    lineEndCharacterIndex <= this.end.characterIndex()
                        ? 1
                        : Math.max(this.end.characterIndex() - this.start.characterIndex(), 1)
                );
            }

            if (this.sourceText.getCharacter(characterIndex) == '\n') {
                lineIndex += 1;
                columnIndex = 0;
                lineStartCharacterIndex = characterIndex + 1;
            } else {
                columnIndex += 1;
            }
        }

        return context(
            this.sourceText.charSequence(
                lineStartCharacterIndex,
                this.sourceText.characterLength()
            ),
            lineIndex,
            columnIndex,
            1
        );
    }

    private String context(CharSequence line, int lineIndex, int columnIndex, int length) {
        var lineNumber = lineIndex + 1;
        var columnNumber = columnIndex + 1;
        return this.sourceText.name() + ":" + lineNumber + ":" + columnNumber + "\n" + line + "\n" + " ".repeat(columnIndex) + "^".repeat(length);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (!(other instanceof SourceRange otherSourceRange)) {
            return false;
        }

        return this.sourceText.equals(otherSourceRange.sourceText) &&
            this.start.equals(otherSourceRange.start) &&
            this.end.equals(otherSourceRange.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.sourceText, this.start, this.end);
    }

    @Override
    public String toString() {
        var lineRange = this.toLineRange();

        return String.format(
            "%s:%s:%s:%s:%s",
            this.sourceText.name(),
            lineRange.startLineIndex + 1,
            lineRange.startColumnIndex + 1,
            lineRange.endLineIndex + 1,
            lineRange.endColumnIndex + 1
        );
    }

    private SourceLineRange toLineRange() {
        var lineIndex = 0;
        var columnIndex = 0;
        var startLineIndex = -1;
        var startColumnIndex = -1;
        var endLineIndex = -1;
        var endColumnIndex = -1;

        for (
            var characterIndex = 0;
            characterIndex <= this.sourceText.characterLength();
            characterIndex++
        ) {
            if (characterIndex == this.start.characterIndex()) {
                startLineIndex = lineIndex;
                startColumnIndex = columnIndex;
            }

            if (characterIndex == this.end.characterIndex() || characterIndex == this.sourceText.characterLength()) {
                endLineIndex = lineIndex;
                endColumnIndex = columnIndex;
                break;
            }

            if (this.sourceText.getCharacter(characterIndex) == '\n') {
                lineIndex += 1;
                columnIndex = 0;
            } else {
                columnIndex += 1;
            }
        }

        return new SourceLineRange(
            startLineIndex,
            startColumnIndex,
            endLineIndex,
            endColumnIndex
        );
    }

    private record SourceLineRange(
        int startLineIndex,
        int startColumnIndex,
        int endLineIndex,
        int endColumnIndex
    ) {
    }
}
