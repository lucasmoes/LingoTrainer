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

class FeedbackTest {

    @Test
    @DisplayName("Word is guessed if all letters are correct")
    void wordIsGuessed() {
        Feedback feedback = new Feedback("woord");
        feedback.createFeedback("woord");
        assertTrue(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("Word is not guessed if all letters are not correct")
    void wordIsNotGuessed() {
        Feedback feedback = new Feedback("woord");
        feedback.createFeedback("woors");
        assertFalse(feedback.isWordGuessed());
    }

    @Test
    @DisplayName("If word is Invalid")
    void guessIsInvalid() {
        Feedback feedback = new Feedback("woord");
        feedback.createFeedback("worden");
        assertTrue(feedback.isWordInvalid());
    }

    @Test
    @DisplayName("If word does not contains a letter that is Invalid")
    void guessIsNotInvalid() {
        Feedback feedback = new Feedback("woord");
        feedback.createFeedback("woord");
        assertFalse(feedback.isWordInvalid());
    }

    @Test
    @DisplayName("Test getMarks in feedback")
    void getMarksInFeedback() {
        Feedback feedback = new Feedback("woord");
        feedback.createFeedback("woord");
        assertEquals(feedback.getMarks(), List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT));
    }

    @ParameterizedTest
    @DisplayName("Testing getFeedback")
    @MethodSource("provideFeedbackExamples")
    void givesFeedback(String wordToGuess, String attempt, List<Mark> output) {
        Feedback feedback = new Feedback(wordToGuess);
        assertEquals(feedback.createFeedback(attempt), output);
    }

    static Stream<Arguments> provideFeedbackExamples() {
        return Stream.of(
                Arguments.of("banaan", "banana", List.of(Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.PRESENT)),
                Arguments.of("ksuir", "kruis", List.of(Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT, Mark.PRESENT)),
                Arguments.of("kaasje", "kastje", List.of(Mark.CORRECT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("aaabbb", "bbbaaa", List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT)),
                Arguments.of("aaabab", "bbbaaa", List.of(Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.CORRECT, Mark.PRESENT)),
                Arguments.of("aaaaaa", "bbbbbb", List.of(Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("gehoor", "onmens", List.of(Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT)),
                Arguments.of("aabbcc", "abcabc", List.of(Mark.CORRECT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT)),
                Arguments.of("alianna", "liniaal", List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT, Mark.PRESENT, Mark.PRESENT, Mark.ABSENT)),
                Arguments.of("heren", "haren", List.of(Mark.CORRECT, Mark.ABSENT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT)),
                Arguments.of("eeaaae", "aaeeae", List.of(Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT, Mark.CORRECT))
        );
    }

    @ParameterizedTest
    @DisplayName("Testing createFeedback that returns a list of Marks.Invalid")
    @MethodSource("provideInvalidFeedbackExamples")
    void givesFirstFeedback(String wordToGuess, String attempt, List<Mark> output) {
        Feedback feedback = new Feedback(wordToGuess);
        assertEquals(feedback.createFeedback(attempt), output);
    }

    static Stream<Arguments> provideInvalidFeedbackExamples() {
        return Stream.of(
//                testing with 5 letter words
                Arguments.of("ksuir","aaaaaa", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)),
//                testing with 6 letter words
                Arguments.of("banaan","aaaaaaa", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID)),
//                testing with 7 letter words
                Arguments.of("kaasjes","aaaaaaaa", List.of(Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID, Mark.INVALID))
        );
    }
}