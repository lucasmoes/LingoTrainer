package lingo.game.trainer.presentation;

import lingo.game.trainer.application.LingoTrainerService;
import lingo.game.trainer.presentation.dto.AttemptDto;
import lingo.game.trainer.presentation.dto.GameDto;
import lingo.game.trainer.presentation.dto.ReturnGameDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lingotrainer")
public class LingoTrainerController {

    @Autowired
    private LingoTrainerService lingoTrainerService;

    public LingoTrainerController(LingoTrainerService lingoTrainerService) {
        this.lingoTrainerService = lingoTrainerService;
    }

    @PostMapping("/newgame")
    public GameDto start() {
        return this.lingoTrainerService.startNewGame();
    }

    @GetMapping("/getgame")
    public GameDto start(@Validated @RequestBody ReturnGameDto returnGameDto) {
        return this.lingoTrainerService.getGame(returnGameDto.getId());
    }

    @PostMapping("/attemptword")
    public GameDto attempt(@Validated @RequestBody AttemptDto attemptDto) {
        return this.lingoTrainerService.attemptWord(attemptDto.getId(), attemptDto.getAttempt());
    }
}