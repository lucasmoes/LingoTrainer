package lingo.game.trainer.data;

import lingo.game.trainer.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LingoTrainerRepository extends JpaRepository<Game, Long> {

}
