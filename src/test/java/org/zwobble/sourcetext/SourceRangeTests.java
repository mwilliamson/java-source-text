package org.zwobble.sourcetext;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.zwobble.precisely.AssertThat.assertThat;
import static org.zwobble.precisely.Matchers.equalTo;

public class SourceRangeTests {
    private record TestCase(
        String name,
        String contents,
        int characterIndexStart,
        String expectedContext
    ) {
        public int characterIndexEnd() {
            return characterIndexStart;
        }
    }

    @TestFactory
    public List<DynamicTest> sourceContextIsSourceLineWithPointer() {
        return Stream.of(
            new TestCase(
                "one line, first character",
                "abcd",
                0,
                """
                    <filename>:1:1
                    abcd
                    ^"""
            ),
            new TestCase(
                "one line, last character",
                "abcd",
                3,
                """
                    <filename>:1:4
                    abcd
                       ^"""
            ),
            new TestCase(
                "one line, end",
                "abcd",
                4,
                """
                    <filename>:1:5
                    abcd
                        ^"""
            ),
            new TestCase(
                "many lines, end",
                """
                    abc
                    def""",
                7,
                """
                    <filename>:2:4
                    def
                       ^"""
            ),
            new TestCase(
                "first line, first character",
                """
                    abc
                    def""",
                0,
                """
                    <filename>:1:1
                    abc
                    ^"""
            ),
            new TestCase(
                "first line, last character",
                """
                    abc
                    def""",
                2,
                """
                    <filename>:1:3
                    abc
                      ^"""
            ),
            new TestCase(
                "last line, first character",
                """
                    abc
                    def""",
                4,
                """
                    <filename>:2:1
                    def
                    ^"""
            ),
            new TestCase(
                "new line",
                """
                    abc
                    def""",
                3,
                """
                    <filename>:1:4
                    abc
                       ^"""
            )
        ).map(testCase -> DynamicTest.dynamicTest(testCase.name, () -> {
            var sourceText = SourceText.fromString("<filename>", testCase.contents);
            var sourceRange = new SourceRange(
                sourceText,
                sourceText.characterPosition(testCase.characterIndexStart),
                sourceText.characterPosition(testCase.characterIndexEnd())
            );

            var result = sourceRange.describe();

            assertThat(result, equalTo(testCase.expectedContext));
        })).collect(Collectors.toList());
    }

    @Test
    public void whenSourceIsZeroLengthThenPointerPointsToNextCharacter() {
        var sourceText = SourceText.fromString(
            "<filename>",
            """
                abcdef
                ghijkl
                mnopqr"""
        );
        var sourceRange = new SourceRange(
            sourceText,
            sourceText.characterPosition(9),
            sourceText.characterPosition(9)
        );

        var result = sourceRange.describe();

        assertThat(result, equalTo("""
            <filename>:2:3
            ghijkl
              ^"""));
    }

    @Test
    public void whenSourceIsOneCharacterThenPointerPointsToCharacter() {
        var sourceText = SourceText.fromString(
            "<filename>",
            """
                abcdef
                ghijkl
                mnopqr"""
        );
        var sourceRange = new SourceRange(
            sourceText,
            sourceText.characterPosition(9),
            sourceText.characterPosition(9)
        );

        var result = sourceRange.describe();

        assertThat(result, equalTo("""
            <filename>:2:3
            ghijkl
              ^"""));
    }

    @Test
    public void whenSourceIsMultipleCharactersOnSameLineThenPointerPointsToAllCharacters() {
        var sourceText = SourceText.fromString(
            "<filename>",
            """
                abcdef
                ghijkl
                mnopqr"""
        );
        var sourceRange = new SourceRange(
            sourceText,
            sourceText.characterPosition(9),
            sourceText.characterPosition(12)
        );

        var result = sourceRange.describe();

        assertThat(result, equalTo("""
            <filename>:2:3
            ghijkl
              ^^^"""));
    }

    @Test
    public void whenSourceIsMultipleCharactersOnMultipleLinesThenPointerPointsToFirstCharacter() {
        // TODO: show multiple lines
        var sourceText = SourceText.fromString(
            "<filename>",
            """
                abcdef
                ghijkl
                mnopqr"""
        );
        var sourceRange = new SourceRange(
            sourceText,
            sourceText.characterPosition(9),
            sourceText.characterPosition(14)
        );

        assertThat(sourceRange.describe(), equalTo("""
            <filename>:2:3
            ghijkl
              ^"""));
    }

    @Test
    public void rangesWithSameStartAndEndAreEqual() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var start = sourceText.characterPosition(1);
        var end = sourceText.characterPosition(3);
        var sourceRange1 = start.to(end);
        var sourceRange2 = start.to(end);

        var result = sourceRange1.equals(sourceRange2);
        var hashCode1 = sourceRange1.hashCode();
        var hashCode2 = sourceRange2.hashCode();

        assertThat(result, equalTo(true));
        assertThat(hashCode1, equalTo(hashCode2));
    }

    @Test
    public void rangesWithDifferentStartAreNotEqual() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var end = sourceText.characterPosition(3);
        var sourceRange1 = sourceText.characterPosition(1).to(end);
        var sourceRange2 = sourceText.characterPosition(2).to(end);

        var result = sourceRange1.equals(sourceRange2);

        assertThat(result, equalTo(false));
    }

    @Test
    public void rangesWithDifferentEndAreNotEqual() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var start = sourceText.characterPosition(1);
        var sourceRange1 = start.to(sourceText.characterPosition(2));
        var sourceRange2 = start.to(sourceText.characterPosition(3));

        var result = sourceRange1.equals(sourceRange2);

        assertThat(result, equalTo(false));
    }

    @Test
    public void rangeIsNotEqualToNull() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var start = sourceText.characterPosition(1);
        var end = sourceText.characterPosition(3);
        var sourceRange = start.to(end);

        var result = sourceRange.equals(null);

        assertThat(result, equalTo(false));
    }

    @Test
    public void rangeIsNotEqualToOtherType() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var start = sourceText.characterPosition(1);
        var end = sourceText.characterPosition(3);
        var sourceRange = start.to(end);

        var result = sourceRange.equals(1);

        assertThat(result, equalTo(false));
    }

    @Test
    public void rangeToStringIncludesSourceTextNameAndStartAndEnd() {
        var sourceText = SourceText.fromString("<string>", "abc");
        var start = sourceText.characterPosition(1);
        var end = sourceText.characterPosition(3);
        var sourceRange = start.to(end);

        var result = sourceRange.toString();

        assertThat(result, equalTo("<string>:1:2:1:4"));
    }
}
