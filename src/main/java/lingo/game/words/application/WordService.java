package lingo.game.words.application;

import lingo.game.words.domain.exception.WordLengthNotSupportedException;
import lingo.game.words.data.WordRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class WordService {
    private final WordRepository wordRepository;

    public WordService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public String provideRandomWord(Integer length) {
        return this.wordRepository
                .findRandomWordByLength(length)
                .orElseThrow(() -> new WordLengthNotSupportedException(length))
                .getValue();
    }

    public boolean wordExists(String word) {
        return this.wordRepository.existsById(word);
    }
}
