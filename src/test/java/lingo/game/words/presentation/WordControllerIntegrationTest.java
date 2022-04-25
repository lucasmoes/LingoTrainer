package lingo.game.words.presentation;

import lingo.game.words.data.WordRepository;
import lingo.game.words.domain.Word;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class WordControllerIntegrationTest {
    // Define test fixtures to be inserted before and deleted after each test
    // See: @BeforeEach and @AfterEach
    private static final String RANDOM_WORD_5 = "groep";
    private static final String RANDOM_WORD_6 = "school";
    private static final String RANDOM_WORD_7 = "student";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WordRepository repository;

    @Test
    @DisplayName("only supports 5, 6 and 7 letter words")
    void notSupportedWordLength() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/words/random")
                .param("length", "8");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("provides 5 letter word")
    void provide5LetterWord() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/words/random")
                .param("length", String.valueOf(5));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word", is(RANDOM_WORD_5)));
    }

    @Test
    @DisplayName("provides 6 letter word")
    void provide6LetterWord() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/words/random")
                .param("length", String.valueOf(6));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word", is(RANDOM_WORD_6)));
    }

    @Test
    @DisplayName("provides 7 letter word")
    void provide7LetterWord() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/words/random")
                .param("length", String.valueOf(7));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word", is(RANDOM_WORD_7)));
    }

    @BeforeEach
    void loadTestData() {
        // Load test fixtures into test database before each test case
        repository.save(new Word(RANDOM_WORD_5));
        repository.save(new Word(RANDOM_WORD_6));
        repository.save(new Word(RANDOM_WORD_7));
    }

    @AfterEach
    void clearTestData() {
        // Remove test fixtures from test database after each test case
        repository.deleteAll();
    }
}
