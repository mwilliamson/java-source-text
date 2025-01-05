package org.zwobble.sourcetext;

import java.nio.CharBuffer;

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

    public CharBuffer charBuffer() {
        return sourceText.charBuffer(
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

    public String describe(String filename) {
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
                    filename,
                    this.sourceText.charBuffer(
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
            filename,
            this.sourceText.charBuffer(
                lineStartCharacterIndex,
                this.sourceText.characterLength()
            ),
            lineIndex,
            columnIndex,
            1
        );
    }

    private String context(String filename, CharBuffer line, int lineIndex, int columnIndex, int length) {
        var lineNumber = lineIndex + 1;
        var columnNumber = columnIndex + 1;
        return filename + ":" + lineNumber + ":" + columnNumber + "\n" + line + "\n" + " ".repeat(columnIndex) + "^".repeat(length);
    }
}
