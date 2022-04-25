package lingo.game.trainer.domain;

import lingo.game.trainer.application.exception.AttemptException;
import lingo.game.trainer.domain.enums.GameState;

import javax.persistence.*;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int score = 0;

    private int roundCounter = 0;

    @OneToOne(cascade = CascadeType.ALL)
    private Round currentRound;

    @Enumerated(EnumType.STRING)
    private GameState gameState;

    public Game() {

    }

    public Game startRound(String wordToGuess) {
        this.gameState = GameState.PLAYING;
        this.currentRound = new Round(wordToGuess);
        this.currentRound.createFirstHint();
        this.roundCounter++;
        return this;
    }

    public void attemptWord(String attempt) {
        if (gameState != GameState.PLAYING) {
            throw new AttemptException("Start a new game");
        }

        gameState = currentRound.attemptWord(attempt);

        if (gameState == GameState.PLAYING_WON_ROUND) {
            calculateScore();
        }
    }

    public void calculateScore() {
        this.score += 5 * (5 - this.currentRound.getGuesses() + 5);
    }

    public int provideNextWordToGuessLength() {
        int wordLength = this.currentRound.returnWordToGuess().length();

        if (wordLength == 5) {
            wordLength = 6;
        } else if (wordLength == 6) {
            wordLength = 7;
        } else {
            wordLength = 5;
        }
        return wordLength;
    }

    public int getScore() {
        return score;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Round getCurrentRound() {
        return currentRound;
    }

    public Long getId() {
        return id;
    }
}