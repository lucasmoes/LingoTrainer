package lingo.game.words.data;

import lingo.game.words.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * We depend on an interface,
 * Spring generates an implementation based on our configured adapters
 * (see: application.properties and pom.xml)
 */
public interface WordRepository extends JpaRepository<Word, String> {
    @Query(nativeQuery=true, value="SELECT * FROM words w WHERE w.length = ?1 ORDER BY random() LIMIT 1")
    Optional<Word> findRandomWordByLength(Integer length);
}
