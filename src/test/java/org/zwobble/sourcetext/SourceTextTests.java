package org.zwobble.sourcetext;

import org.junit.jupiter.api.Test;

import static org.zwobble.precisely.AssertThat.assertThat;
import static org.zwobble.precisely.Matchers.equalTo;

public class SourceTextTests {
    @Test
    public void canCreateSourceRangeFromSourcePositions() {
        var sourceText = SourceText.fromString("<filename>", "abcdef");
        var start = sourceText.characterPosition(2);
        var end = sourceText.characterPosition(5);
        var sourceRange = start.to(end);

        var result = sourceRange.describe();

        assertThat(result, equalTo("""
        <filename>:1:3
        abcdef
          ^^^"""));
    }
}
