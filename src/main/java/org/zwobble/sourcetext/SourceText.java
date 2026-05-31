package org.zwobble.sourcetext;

import java.nio.CharBuffer;
import java.util.Optional;

public class SourceText {
    public static SourceText fromString(String name, String string) {
        return new SourceText(name, CharBuffer.wrap(string), Optional.empty());
    }

    public static SourceText derived(SourceText original, String string, PositionMapper positionMapper) {
        return new SourceText(
            original.name,
            CharBuffer.wrap(string),
            Optional.of(new ParentSourceText(original, positionMapper))
        );
    }

    @FunctionalInterface
    public interface PositionMapper {
        int derivedCharacterIndexToOriginalCharacterIndex(int derivedCharacterIndex);
    }

    record ParentSourceText(SourceText sourceText, PositionMapper positionMapper) {
        SourcePosition originalPosition(SourcePosition derivedPosition) {
            var originalCharacterIndex = positionMapper
                .derivedCharacterIndexToOriginalCharacterIndex(derivedPosition.characterIndex());
            return sourceText.characterPosition(originalCharacterIndex);
        }
    }

    private final String name;
    private final CharBuffer charBuffer;
    private final Optional<ParentSourceText> parentSourceText;

    private SourceText(String name, CharBuffer charBuffer, Optional<ParentSourceText> parentSourceText) {
        this.name = name;
        this.charBuffer = charBuffer;
        this.parentSourceText = parentSourceText;
    }

    public String name() {
        return this.name;
    }

    public CharSequence charSequence() {
        return this.charBuffer;
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

    public SourcePosition characterPosition(int characterIndex) {
        return new SourcePosition(this, characterIndex);
    }

    Optional<ParentSourceText> parentSourceText() {
        return parentSourceText;
    }
}
