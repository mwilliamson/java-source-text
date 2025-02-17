package org.zwobble.sourcetext;

import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zwobble.precisely.AssertThat.assertThat;
import static org.zwobble.precisely.Matchers.equalTo;

public class SourceCharacterIteratorTests {
    @Test
    public void emptyStringIsImmediatelyAtEnd() {
        var iterator = sourceCharacterIterator("");

        var result = iterator.isEnd();

        assertThat(result, equalTo(true));
    }

    @Test
    public void nonEmptyStringIsAtEndAfterSkippingAllCharacters() {
        var iterator = sourceCharacterIterator("abc");

        assertThat(iterator.isEnd(), equalTo(false));

        iterator.skip();
        assertThat(iterator.isEnd(), equalTo(false));

        iterator.skip();
        assertThat(iterator.isEnd(), equalTo(false));

        iterator.skip();
        assertThat(iterator.isEnd(), equalTo(true));

        iterator.skip();
        assertThat(iterator.isEnd(), equalTo(true));
    }
    @Test
    public void emptyStringImmediatelyHasZeroRemainingCharacters() {
        var iterator = sourceCharacterIterator("");

        var result = iterator.remaining();

        assertThat(result, equalTo(0));
    }

    @Test
    public void nonEmptyStringRemainingDecrementsWithEachSkippedCharacter() {
        var iterator = sourceCharacterIterator("abc");

        assertThat(iterator.remaining(), equalTo(3));

        iterator.skip();
        assertThat(iterator.remaining(), equalTo(2));

        iterator.skip();
        assertThat(iterator.remaining(), equalTo(1));

        iterator.skip();
        assertThat(iterator.remaining(), equalTo(0));

        iterator.skip();
        assertThat(iterator.remaining(), equalTo(0));
    }

    @Test
    public void peekReturnsCurrentCharacter() {
        var iterator = sourceCharacterIterator("abc");

        assertThat(iterator.peek(), equalTo((int)'a'));

        iterator.skip();
        assertThat(iterator.peek(), equalTo((int)'b'));

        iterator.skip();
        assertThat(iterator.peek(), equalTo((int)'c'));
    }

    @Test
    public void peekReturnsMinusOneAtEnd() {
        var iterator = sourceCharacterIterator("abc");

        iterator.skip();
        iterator.skip();
        iterator.skip();

        assertThat(iterator.peek(), equalTo(-1));

        iterator.skip();
        assertThat(iterator.peek(), equalTo(-1));
    }

    @Test
    public void peekSequenceCanReturnSubSequenceAtStart() {
        var iterator = sourceCharacterIterator("abcdef");

        var result = iterator.peekSequence(3);

        assertThat(result, equalTo(CharBuffer.wrap("abc")));
    }

    @Test
    public void peekSequenceCanReturnSubSequenceInMiddle() {
        var iterator = sourceCharacterIterator("abcdef");
        iterator.skip();

        var result = iterator.peekSequence(3);

        assertThat(result, equalTo(CharBuffer.wrap("bcd")));
    }

    @Test
    public void peekSequenceCanReturnSubSequenceAtEnd() {
        var iterator = sourceCharacterIterator("abcd");
        iterator.skip();

        var result = iterator.peekSequence(3);

        assertThat(result, equalTo(CharBuffer.wrap("bcd")));
    }

    @Test
    public void whenPeekSequenceLengthIsGreaterThanRemainingThenErrorIsThrown() {
        var iterator = sourceCharacterIterator("abc");
        iterator.skip();

        assertThrows(
            IndexOutOfBoundsException.class,
            () -> iterator.peekSequence(3)
        );
    }

    @Test
    public void canSkipMultipleCharacters() {
        var iterator = sourceCharacterIterator("abcdef");

        iterator.skip(3);
        assertThat(iterator.peek(), equalTo((int)'d'));

        iterator.skip(2);
        assertThat(iterator.peek(), equalTo((int)'f'));
    }

    @Test
    public void whenNotAtEndThenCharacterSourceRangeCoversSingleCharacter() {
        var iterator = sourceCharacterIterator("abc");
        iterator.skip();

        var result = iterator.characterSourceRange();

        assertThat(result.start().characterIndex(), equalTo(1));
        assertThat(result.end().characterIndex(), equalTo(2));
    }

    @Test
    public void whenAtEndThenCharacterSourceRangeCoversZeroCharacters() {
        var iterator = sourceCharacterIterator("abc");
        iterator.skip(3);

        var result = iterator.characterSourceRange();

        assertThat(result.start().characterIndex(), equalTo(3));
        assertThat(result.end().characterIndex(), equalTo(3));
    }

    @Test
    public void sourceRangeCanBeConstructedFromPosition() {
        var iterator = sourceCharacterIterator("abcdef");
        iterator.skip(2);
        var start = iterator.position();

        iterator.skip(3);
        var end = iterator.position();

        var result = iterator.sourceRange(start, end);

        assertThat(result.start().characterIndex(), equalTo(2));
        assertThat(result.end().characterIndex(), equalTo(5));
    }

    @Test
    public void canSetPositionOfIterator() {
        var iterator = sourceCharacterIterator("abcdef");
        iterator.skip(2);

        var position = iterator.position();

        iterator.skip(3);
        assertThat(iterator.peek(), equalTo((int)'f'));

        iterator.position(position);
        assertThat(iterator.peek(), equalTo((int)'c'));
    }

    public static SourceCharacterIterator sourceCharacterIterator(String text) {
        var sourceText = SourceText.fromString("<string>", text);
        return sourceText.characterIterator();
    }
}
