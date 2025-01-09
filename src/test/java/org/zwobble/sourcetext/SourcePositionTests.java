package org.zwobble.sourcetext;

import org.junit.jupiter.api.Test;

import static org.zwobble.precisely.AssertThat.assertThat;
import static org.zwobble.precisely.Matchers.equalTo;

public class SourcePositionTests {
    @Test
    public void positionsForSameSourceTextAndCharacterIndexAreEqual() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var position1 = sourceText.characterPosition(1);
        var position2 = sourceText.characterPosition(1);

        var result = position1.equals(position2);
        var hashCode1 = position1.hashCode();
        var hashCode2 = position2.hashCode();

        assertThat(result, equalTo(true));
        assertThat(hashCode1, equalTo(hashCode2));
    }

    @Test
    public void whenPositionsHaveDifferentSourceTextThenTheyAreNotEqual() {
        var sourceText1 = SourceText.fromString("<string>", "abc");
        var position1 = sourceText1.characterPosition(1);
        var sourceText2 = SourceText.fromString("<string>", "abcd");
        var position2 = sourceText2.characterPosition(1);

        var result = position1.equals(position2);

        assertThat(result, equalTo(false));
    }

    @Test
    public void whenPositionsHaveDifferentCharacterIndexThenTheyAreNotEqual() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var position1 = sourceText.characterPosition(1);
        var position2 = sourceText.characterPosition(2);

        var result = position1.equals(position2);

        assertThat(result, equalTo(false));
    }

    @Test
    public void positionIsNotEqualToNull() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var position1 = sourceText.characterPosition(1);

        var result = position1.equals(null);

        assertThat(result, equalTo(false));
    }

    @Test
    public void positionIsNotEqualToOtherType() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var position1 = sourceText.characterPosition(1);

        var result = position1.equals(2);

        assertThat(result, equalTo(false));
    }

    @Test
    public void positionToStringIncludesSourceTextNameAndLineAndColumnNumbers() {
        var sourceText = SourceText.fromString("<string>", "ab\ncdefgh\nij");
        var position = sourceText.characterPosition(5);

        var result = position.toString();

        assertThat(result, equalTo("<string>:2:3"));
    }
}
