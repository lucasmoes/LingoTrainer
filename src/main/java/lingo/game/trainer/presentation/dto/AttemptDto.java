package lingo.game.trainer.presentation.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class AttemptDto {

    @Positive
    @NotNull
    public Long id;

    @NotBlank
    public String attempt;

    public Long getId() {
        return id;
    }

    public String getAttempt() {
        return attempt;
    }

}