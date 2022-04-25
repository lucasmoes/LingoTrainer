package lingo.game.trainer.domain;

import lingo.game.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class HintTest {
    @ParameterizedTest
    @DisplayName("Testing createFirstHint")
    @MethodSource("provideFirstHintExamples")
    void givesFirstHint(String wordToGuess, List<String> output) {
        Hint hint = new Hint();
        assertEquals(hint.createFirstHint(wordToGuess), output);
    }

    static Stream<Arguments> provideFirstHintExamples() {
        return Stream.of(
//                testing with 5 letter words
                Arguments.of("woord", List.of("w", ".", ".", ".", ".")),
//                testing with 6 letter words
                Arguments.of("woords", List.of("w", ".", ".", ".", ".", ".")),
//                testing with 7 letter words
                Arguments.of("woorden", List.of("w", ".", ".", ".", ".", ".", "."))
        );
    }

    @ParameterizedTest
    @DisplayName("Testing newHint")
    @MethodSource("provideNewHintExamples")
    void givesNewHint(String wordToGuess, List<Mark> marks, List<String> output) {
        Hint hint = new Hint();
        hint.createFirstHint(wordToGuess);
        assertEquals(hint.newHint(marks, wordToGuess), output);
    }

    static Stream<Arguments> provideNewHintExamples() {
        return Stream.of(
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT), List.of("w", "o", "o", "r", ".")),
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT), List.of("w", "o", "o", ".", ".")),
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.ABSENT), List.of("w", ".", "o", "r", ".")),
                Arguments.of("woord", List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT), List.of("w", ".", "o", ".", "."))
        );
    }

    @Test
    @DisplayName("Testing getHint with previous hint to get new hint")
    void givesHintWithPreviousHint() {
        Hint hint = new Hint();
        hint.createFirstHint("woord");
        hint.newHint(List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT), "woord");
        hint.newHint(List.of(Mark.CORRECT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT), "woord");
        assertEquals(hint.getHint(), List.of("w", "o", "o", ".", "."));
    }
}