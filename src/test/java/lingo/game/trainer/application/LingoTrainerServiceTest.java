package lingo.game.trainer.application;

import lingo.game.trainer.application.exception.AttemptException;
import lingo.game.trainer.application.exception.GameNotFoundException;
import lingo.game.trainer.domain.Game;
import lingo.game.trainer.domain.enums.GameState;
import lingo.game.trainer.presentation.dto.GameDto;
import lingo.game.words.application.WordService;
import lingo.game.trainer.data.LingoTrainerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LingoTrainerServiceTest {

    private LingoTrainerService lingoTrainerService;

    private WordService wordService;

    private LingoTrainerRepository repository;

    private Game game;

    @BeforeEach
    void initialize() {
        this.wordService = mock(WordService.class);
        when(wordService.provideRandomWord(5))
                .thenReturn("groep");

        when(wordService.provideRandomWord(6))
                .thenReturn("groeps");

        this.game = new Game();
        game.startRound("groep");

        this.repository = mock(LingoTrainerRepository.class);

        when(repository.findById(1L)).thenReturn(Optional.of(this.game));

        this.lingoTrainerService = new LingoTrainerService(this.wordService, this.repository);
    }

    @Test
    @DisplayName("Testing startGame that returns a existing game with round Playing")
    void testStartGameReturnPlaying() {
        GameDto gameDto = this.lingoTrainerService.getGame(1L);
        assertEquals(GameState.PLAYING, gameDto.getGameState());
        assertEquals(1, gameDto.getRoundCounter());
    }

    @Test
    @DisplayName("Testing startNewGame that returns a new game")
    void testStartGameReturnPlayingNoId() {
        GameDto gameDto = this.lingoTrainerService.startNewGame();
        assertEquals(GameState.PLAYING, gameDto.getGameState());
        assertEquals(1, gameDto.getRoundCounter());
    }

    @Test
    @DisplayName("Testing attempt that returns a game with new round if word was guessed")
    void testStartGameReturnPlayingWonRound() {

        this.lingoTrainerService.getGame(1L);

        GameDto gameDto = this.lingoTrainerService.attemptWord(1L, "groep");
        assertEquals(GameState.PLAYING, gameDto.getGameState());
        assertEquals(2, gameDto.getRoundCounter());
    }

    @Test
    @DisplayName("Testing attemptWord that returns an exception because game is lost")
    void testAttemptThrowsExceptionLostGame() throws AttemptException {
        for (int i = 0; i < 5; i++) {
            this.lingoTrainerService.attemptWord(1L, "groes");
        }
        assertThrows(AttemptException.class, () -> this.lingoTrainerService.attemptWord(1L, "groes"));
    }

    @Test
    @DisplayName("Testing getGameById that returns an exception because id not found")
    void testGetGameByIdNotFound() throws GameNotFoundException {
        assertThrows(GameNotFoundException.class, () -> this.lingoTrainerService.getGameById(2L));
    }

    @Test
    @DisplayName("Testing getGameById that returns game by id")
    void testGetGameByIdFound() throws GameNotFoundException {
        assertNotNull(this.lingoTrainerService.getGameById(1L));
    }

    @Test
    @DisplayName("Testing when game is lost that is returns the wordToGuess")
    void testReturnsWordToGuess() throws GameNotFoundException {
        for (int i = 0; i < 5; i++) {
            this.lingoTrainerService.attemptWord(1L, "groes");
        }
        assertEquals("groep", this.lingoTrainerService.getGame(1L).getWordToGuess());
        assertEquals(GameState.LOST, this.lingoTrainerService.getGame(1L).getGameState());
    }
}