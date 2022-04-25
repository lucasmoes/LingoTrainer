package lingo.game.trainer.application;

import lingo.game.trainer.application.exception.AttemptException;
import lingo.game.trainer.application.exception.GameNotFoundException;
import lingo.game.trainer.domain.Game;
import lingo.game.trainer.domain.enums.GameState;
import lingo.game.trainer.presentation.dto.GameDto;
import lingo.game.words.application.WordService;
import lingo.game.trainer.data.LingoTrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class LingoTrainerService {

    private final WordService wordService;
    private final LingoTrainerRepository lingoTrainerRepository;

    @Autowired
    public LingoTrainerService(WordService wordService, LingoTrainerRepository gameRepository) {
        this.wordService = wordService;
        this.lingoTrainerRepository = gameRepository;
    }

    public Game getGameById(Long id) throws GameNotFoundException {
        return lingoTrainerRepository.findById(id).orElseThrow(
                () -> new GameNotFoundException("Game with id: " + id + ", does not exist"));
    }

    public GameDto getGame(Long id) throws GameNotFoundException {
        Game game = getGameById(id);
        return new GameDto(game);
    }

    public GameDto startNewGame() throws GameNotFoundException {
        Game game = new Game();
        game.startRound(wordService.provideRandomWord(5));
        this.lingoTrainerRepository.save(game);
        return new GameDto(game);
    }

    public GameDto attemptWord(Long id, String attempt) throws AttemptException {
        Game game = getGameById(id);
        if (game.getGameState() == GameState.LOST) {
            throw new AttemptException("Last round you lost, Start a new game");
        }

        game.attemptWord(attempt);

        if (game.getGameState() == GameState.PLAYING_WON_ROUND) {
            game.startRound(wordService.provideRandomWord(game.provideNextWordToGuessLength()));
        }

        this.lingoTrainerRepository.save(game);

        return new GameDto(game);
    }
}