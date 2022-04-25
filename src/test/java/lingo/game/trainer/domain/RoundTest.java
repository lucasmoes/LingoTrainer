package lingo.game.trainer.domain;

import lingo.game.trainer.domain.enums.GameState;
import lingo.game.trainer.domain.enums.Mark;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class RoundTest {

    @ParameterizedTest
    @DisplayName("Testing createFirstHint in Round")
    @MethodSource("provideFirstHintExamples")
    void testGetFirstHintRound(String wordToGuess, List<String> output) {
        Round round = new Round(wordToGuess);
        assertEquals(round.createFirstHint(), output);
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
    @DisplayName("Testing getHint in Round")
    @MethodSource("provideHintExamples")
    void testGetHintRound(String wordToGuess, String attempt, List<String> output) {
        Round round = new Round(wordToGuess);
        round.createFirstHint();
        assertEquals(round.createHint(attempt), output);
    }

    static Stream<Arguments> provideHintExamples() {
        return Stream.of(
                Arguments.of("banaan", "banana", List.of("b", "a", "n", "a", ".", ".")),
                Arguments.of("ksuir", "kruis", List.of("k", ".", "u", "i", ".")),
                Arguments.of("kaasje", "kastje", List.of("k", "a", ".", ".", "j", "e")),
                Arguments.of("aaabbb", "bbbaaa", List.of("a", ".", ".", ".", ".", ".")),
                Arguments.of("aaabab", "bbbaaa", List.of("a", ".", ".", ".", "a", ".")),
                Arguments.of("aaaaaa", "bbbbbb", List.of("a", ".", ".", ".", ".", ".")),
                Arguments.of("gehoor", "onmens", List.of("g", ".", ".", ".", ".", ".")),
                Arguments.of("aabbcc", "abcabc", List.of("a", ".", ".", ".", ".", "c")),
                Arguments.of("alianna", "liniaal", List.of("a", ".", ".", ".", ".", ".", ".")),
                Arguments.of("heren", "haren", List.of("h", ".", "r", "e", "n")),
                Arguments.of("eeaaae", "aaeeae", List.of("e", ".", ".", ".", "a", "e"))
        );
    }


    @Test
    @DisplayName("Test attemptWord in Round is PLAYING")
    void getAttemptWordPlaying() {
        Round round = new Round("woord");
        round.createFirstHint();
        assertEquals(round.attemptWord("woors"), GameState.PLAYING);
    }

    @Test
    @DisplayName("Test attemptWord in Round is LOST after 5 tries")
    void getAttemptWordLost() {
        Round round = new Round("woord");
        round.createFirstHint();
        round.attemptWord("woors");
        round.attemptWord("woors");
        round.attemptWord("woors");
        round.attemptWord("woors");
        assertEquals(round.attemptWord("woors"), GameState.LOST);
    }

    @Test
    @DisplayName("Test attemptWord in Round is PLAYING_WON_ROUND")
    void getAttemptWordPlayingWonRound() {
        Round round = new Round("woord");
        round.createFirstHint();
        assertEquals(round.attemptWord("woord"), GameState.PLAYING_WON_ROUND);
    }

    @Test
    @DisplayName("Test returnWordToGuess in Round")
    void getWordToGuess() {
        Round round = new Round("woord");
        round.createFirstHint();
        assertEquals(round.returnWordToGuess(), "woord");
    }

    @Test
    @DisplayName("Test getGuesses in Round")
    void getGuesses() {
        Round round = new Round("woord");
        round.createFirstHint();
        round.attemptWord("woors");
        round.attemptWord("woord");

        assertEquals(1 ,round.getGuesses());
    }

    @Test
    @DisplayName("Test getFeedback in Round")
    void getFeedback() {
        Round round = new Round("woord");
        round.createFirstHint();
        round.attemptWord("woors");
        assertEquals(round.getFeedback(), List.of(Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.CORRECT,Mark.ABSENT));
    }

    @Test
    @DisplayName("Test getHint in Round")
    void getHint() {
        Round round = new Round("woord");
        round.createFirstHint();
        assertEquals(round.getHint(), List.of("w", ".", ".", ".", "."));
    }
}