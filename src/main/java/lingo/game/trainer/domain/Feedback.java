package lingo.game.trainer.domain;

import lingo.game.trainer.domain.enums.Mark;

import javax.persistence.*;
import java.util.*;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String wordToGuess;

    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Mark.class)
    private List<Mark> marks;

    public Feedback(String wordToGuess) {
        this.wordToGuess = wordToGuess;
        this.marks = new ArrayList<>();
    }

    public Feedback() {

    }

    public boolean isWordGuessed() {
        for (Mark mark : marks) {
            if (!mark.equals(Mark.CORRECT)) {
                return false;
            }
        }
        return true;
    }

    public boolean isWordInvalid() {
        for (Mark mark : marks) {
            if (mark.equals(Mark.INVALID)) {
                return true;
            }
        }
        return false;
    }

    public List<Mark> getMarks() {
        return marks;
    }

    public List<Mark> createFeedback(String attempt) {

        List<Mark> newMarks = new ArrayList<>();
        if (attempt.length() != wordToGuess.length()) {
            for (int x = 0; x < wordToGuess.length(); x++) {
                newMarks.add(Mark.INVALID);
            }
            this.marks = newMarks;
            return newMarks;
        }

        Map<Character, Integer> dictionary = new HashMap<>();

        for (int x = 0; x < wordToGuess.length(); x++) {
            if (dictionary.containsKey(wordToGuess.charAt(x))) {
                dictionary.put(wordToGuess.charAt(x), dictionary.get(wordToGuess.charAt(x)) + 1);
            } else {
                dictionary.put(wordToGuess.charAt(x), 1);
            }
        }

        for (int j = 0; j < wordToGuess.length(); j++) {
            if (attempt.charAt(j) == wordToGuess.charAt(j)) {
                newMarks.add(Mark.CORRECT);
                dictionary.merge(attempt.charAt(j), -1, Integer::sum);

            } else if (wordToGuess.contains(String.valueOf(attempt.charAt(j)))) {
                if (dictionary.get(attempt.charAt(j)) != 0) {
                    newMarks.add(Mark.PRESENT);
                    dictionary.merge(attempt.charAt(j), -1, Integer::sum);
                } else {
                    newMarks.add(Mark.ABSENT);
                }

            } else {
                newMarks.add(Mark.ABSENT);
            }
        }

        this.marks = newMarks;
        return marks;
    }
}