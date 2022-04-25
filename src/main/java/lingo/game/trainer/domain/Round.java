package lingo.game.trainer.domain;

import lingo.game.trainer.domain.enums.GameState;
import lingo.game.trainer.domain.enums.Mark;

import javax.persistence.*;
import java.util.List;

@Entity
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String wordToGuess;

    private int guesses;

    @OneToOne(cascade = CascadeType.ALL)
    private Feedback feedback;

    @OneToOne(cascade = CascadeType.ALL)
    private Hint hint;


    public Round(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.feedback = new Feedback(wordToGuess);
        this.hint = new Hint();
    }

    public Round() {

    }

    public List<String> createFirstHint() {
        return hint.createFirstHint(wordToGuess);
    }

    public List<String> createHint(String attempt) {
        return hint.newHint(feedback.createFeedback(attempt), wordToGuess);
    }

    public GameState attemptWord(String attempt) {

        feedback.createFeedback(attempt);
        hint.newHint(feedback.getMarks(), wordToGuess);

        if (feedback.isWordGuessed()) {
            return GameState.PLAYING_WON_ROUND;
        }
        this.guesses++;

        if (guesses >= 5) {
            return GameState.LOST;
        }

        return GameState.PLAYING;
    }

    public String returnWordToGuess() {
        return wordToGuess;
    }

    public int getGuesses() {
        return guesses;
    }

    public List<Mark> getFeedback() {
        return feedback.getMarks();
    }

    public List<String> getHint() {
        return hint.getHint();
    }
}