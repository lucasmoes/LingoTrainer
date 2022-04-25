package lingo.game.trainer.presentation.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class ReturnGameDto {

    @Positive
    @NotNull
    public Long id;

    public Long getId() {
        return id;
    }

}