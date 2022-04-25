package lingo.game.trainer.domain;

import lingo.game.trainer.application.exception.AttemptException;
import lingo.game.trainer.domain.enums.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void initialize() {
        this.game = new Game();
    }

    @Test
    @DisplayName("Testing if start round returns a game")
    void testStartNewRound() {
        assertNotNull(game.startRound("woord"));
    }

    @Test
    @DisplayName("Testing attemptWord throws an exception in Game")
    void testStartRound() {
        game.startRound("woord");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        assertThrows(AttemptException.class, () -> game.attemptWord("woors"));
    }

    @Test
    @DisplayName("Testing if word is correct calculates the score")
    void testGameScore() {
        game.startRound("woord");
        game.attemptWord("woord");
        assertEquals(50, game.getScore());
    }

    @Test
    @DisplayName("Testing if word is correct calculates the score after a bad attempt")
    void testGameScoreAfterTwoTries() {
        game.startRound("woord");
        game.attemptWord("woors");
        game.attemptWord("woord");
        assertEquals(45, game.getScore());
    }

    @Test
    @DisplayName("Testing if word is correct calculates the score after a bad attempt and next round")
    void testGameScoreAfterARound() {
        game.startRound("woord");
        game.attemptWord("woors");
        game.attemptWord("woord");
        game.startRound("woorden");
        game.attemptWord("woorden");
        assertEquals(95, game.getScore());
    }

    @Test
    @DisplayName("Testing if rounds are counted correctly")
    void testGameRoundCounter() {
        game.startRound("woord");
        game.attemptWord("woord");
        game.startRound("woord");
        game.attemptWord("woord");
        assertEquals(2, game.getRoundCounter());
    }

    @Test
    @DisplayName("Testing if GameState is is playing_won_round after good guess")
    void testGameGameStatePlayingWonRound() {
        game.startRound("woord");
        game.attemptWord("woord");
        assertEquals(GameState.PLAYING_WON_ROUND, game.getGameState());
    }

    @Test
    @DisplayName("Testing if GameState is Playing after a attempt")
    void testGameGameStatePlaying() {
        game.startRound("woord");
        game.attemptWord("woors");
        assertEquals(GameState.PLAYING, game.getGameState());
    }

    @Test
    @DisplayName("Testing if GameState is correct after 5 failed attempts")
    void testGameGameStateLost() {
        game.startRound("woord");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        game.attemptWord("woors");
        assertEquals(GameState.LOST, game.getGameState());
    }

    @ParameterizedTest
    @DisplayName("Testing if it gives the next word lenght")
    @MethodSource("provideDifferentWordLengthsExamples")
    void testRoundWordLenght(String wordToGuess, int output) {
        game.startRound(wordToGuess);
        game.attemptWord(wordToGuess);
        assertEquals(output, game.provideNextWordToGuessLength());
    }

    static Stream<Arguments> provideDifferentWordLengthsExamples() {
        return Stream.of(
//                testing with 5 letter words
                Arguments.of("ksuir",6),
//                testing with 6 letter words
                Arguments.of("banaan",7),
//                testing with 7 letter words
                Arguments.of("kaasjes", 5)
        );
    }

    @Test
    @DisplayName("Testing game is able to return round")
    void testGetCurrentRound() {
        game.startRound("woord");
        assertNotNull(game.getCurrentRound());
    }
}