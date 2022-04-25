package lingo.game.trainer.application.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AttemptException extends RuntimeException {
    public AttemptException(String message) {
        super(message);
    }
}

