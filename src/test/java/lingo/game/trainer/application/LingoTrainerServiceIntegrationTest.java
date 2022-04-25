package lingo.game.trainer.application;

import lingo.game.trainer.application.exception.AttemptException;
import lingo.game.trainer.application.exception.GameNotFoundException;
import lingo.game.trainer.domain.Game;
import lingo.game.trainer.domain.enums.GameState;
import lingo.game.trainer.presentation.dto.GameDto;
import lingo.game.words.data.WordRepository;
import lingo.game.words.domain.Word;
import lingo.game.trainer.data.LingoTrainerRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LingoTrainerServiceIntegrationTest {

    private static final String RANDOM_WORD_5 = "groep";
    private static final String RANDOM_WORD_6 = "school";
    private static final String RANDOM_WORD_7 = "student";

    @Autowired
    private LingoTrainerService lingoTrainerService;

    @Autowired
    private LingoTrainerRepository lingoTrainerRepository;

    @Autowired
    private WordRepository wordRepository;

    private Game game;

    @BeforeEach
    void initialize() {

        this.wordRepository.save(new Word(RANDOM_WORD_5));
        this.wordRepository.save(new Word(RANDOM_WORD_6));
        this.wordRepository.save(new Word(RANDOM_WORD_7));

        this.game = new Game();
        this.game.startRound("groep");
        this.lingoTrainerRepository.save(this.game);
    }

    @AfterEach
    void clearTestData() {
        this.lingoTrainerRepository.deleteAll();
        this.wordRepository.deleteAll();
    }

    @Test
    @DisplayName("Testing startGame")
    void testStartGame() {
        assertNotNull(this.lingoTrainerService.getGame(this.game.getId()));
    }

    @Test
    @DisplayName("Testing startGame that returns a existing game")
    void testStartGameById() {
        GameDto gameDto = this.lingoTrainerService.getGame(this.game.getId());
        assertEquals(GameState.PLAYING, gameDto.getGameState());
        assertEquals(0, gameDto.getScore());
        assertEquals(5, gameDto.getCurrentRound().getHint().size());
        assertEquals(0, gameDto.getCurrentRound().getGuesses());
    }

    @Test
    @DisplayName("Testing startNewGame that returns a new game")
    void testStartGameReturnNewGame() {
        GameDto gameDto = this.lingoTrainerService.startNewGame();
        assertEquals(GameState.PLAYING, gameDto.getGameState());
        assertEquals(0, gameDto.getScore());
        assertEquals(5, gameDto.getCurrentRound().getHint().size());
        assertEquals(0, gameDto.getCurrentRound().getGuesses());
    }

    @Test
    @DisplayName("Testing attemptWord if word is correct and return new round")
    void testGuessWord() {
        Long id = this.game.getId();
        GameDto gameDto = this.lingoTrainerService.attemptWord(id, "groep");
        assertEquals(50, gameDto.getScore());
        assertEquals(2, gameDto.getRoundCounter());
        assertEquals(6, gameDto.getCurrentRound().getHint().size());
    }

    @Test
    @DisplayName("Testing if id is not found return exception")
    void testGameNotFountExecption() {
        Long id = this.game.getId() + 123;
        assertThrows(GameNotFoundException.class, () -> this.lingoTrainerService.getGame(id));
    }

    @Test
    @DisplayName("Testing if id is not fount return exception")
    void testAttemtpExecption() {
        Long id = this.game.getId();
        this.lingoTrainerService.attemptWord(id, "groes");
        this.lingoTrainerService.attemptWord(id, "groes");
        this.lingoTrainerService.attemptWord(id, "groes");
        this.lingoTrainerService.attemptWord(id, "groes");
        this.lingoTrainerService.attemptWord(id, "groes");
        assertThrows(AttemptException.class, () -> this.lingoTrainerService.attemptWord(id, "groes"));
    }
}