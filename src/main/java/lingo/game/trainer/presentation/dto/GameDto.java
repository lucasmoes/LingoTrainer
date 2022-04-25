package lingo.game.trainer.presentation.dto;

import lingo.game.trainer.domain.Game;
import lingo.game.trainer.domain.Round;
import lingo.game.trainer.domain.enums.GameState;

public class GameDto {

    private Long id;

    private int score;

    private int roundCounter;

    private Round currentRound;

    private GameState gameState;

    public GameDto(Game game) {
        this.id = game.getId();
        this.score = game.getScore();
        this.roundCounter = game.getRoundCounter();
        this.currentRound = game.getCurrentRound();
        this.gameState = game.getGameState();
    }

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public GameState getGameState() {
        return gameState;
    }

    public String getWordToGuess() {
        if (gameState != GameState.PLAYING) {
            return currentRound.returnWordToGuess();
        }
        return "Word is Hidden";
    }
}