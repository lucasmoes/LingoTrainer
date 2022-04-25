package lingo.game.trainer.domain;

import lingo.game.trainer.data.ListStringConverter;
import lingo.game.trainer.domain.enums.Mark;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Hint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Convert(converter = ListStringConverter.class)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<String> hint;

    public Hint() {

    }

    public List<String> createFirstHint(String wordToGuess) {
        List<String> hints = new ArrayList<>();
        for (int i = 0; i < wordToGuess.length(); i++) {
            if (i == 0) {
                hints.add(String.valueOf(wordToGuess.charAt(i)));
            } else {
                hints.add(".");
            }
        }
        hint = hints;
        return hints;
    }

    public List<String> newHint(List<Mark> marks, String wordToGuess) {

        int i = 0;
        List<String> hints = new ArrayList<>();

        for (Mark mark : marks) {

            if (mark.equals(Mark.CORRECT)) {
                hints.add(String.valueOf(wordToGuess.charAt(i)));

            } else {
                hints.add(".");
            }
            i += 1;
        }

        for (int j = 0; j < hints.size(); j++) {
            if (hints.get(j).equals(".") && !(hint.get(j).equals("."))) {
                hints.set(j, String.valueOf(hint.get(j)));
            }
        }
        hint = hints;
        return hints;
    }

    public List<String> getHint() {
        return hint;
    }
}