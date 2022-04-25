package lingo.game.trainer.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lingo.game.trainer.data.LingoTrainerRepository;
import lingo.game.trainer.domain.Game;
import lingo.game.trainer.presentation.dto.AttemptDto;
import lingo.game.trainer.presentation.dto.ReturnGameDto;
import lingo.game.words.application.WordService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LingoTrainerControllerIntegrationTest {

    @Autowired
    private LingoTrainerRepository repository;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WordService wordService;

    Game game = new Game();

    @BeforeEach
    void initialize() {
        when(wordService.provideRandomWord(5))
                .thenReturn("groep");

        this.game.startRound("groep");
        this.repository.save(this.game);
    }

    @Test
    @DisplayName("Test if game is correctly started")
    void testStartNewGame() throws Exception {

        RequestBuilder request = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        mockMvc.perform(request).andExpect(status().isOk())
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundCounter").value(1))
                .andExpect(jsonPath("$.currentRound.guesses").value(0))
                // no feedback to test
                .andExpect(jsonPath("$.currentRound.hint").value(Matchers.containsInRelativeOrder("g", ".", ".", ".", ".")))
                .andExpect(jsonPath("$.gameState").value("PLAYING"))
                .andExpect(jsonPath("$.wordToGuess").value("Word is Hidden"));
    }

    @Test
    @DisplayName("Test if game can be played")
    void testAttemptword() throws Exception {

        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        MockHttpServletResponse response = mockMvc.perform(requestGame).andReturn().getResponse();

        int gameId = JsonPath.read(response.getContentAsString(), "$.id");

        AttemptDto attemptDto = new AttemptDto();
        attemptDto.id = (long) gameId;
        attemptDto.attempt = "groes";
        String requestBody = new ObjectMapper().writeValueAsString(attemptDto);


        RequestBuilder requestAttempt = MockMvcRequestBuilders
                .post("/lingotrainer/attemptword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestAttempt).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((long) gameId))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundCounter").value(1))
                .andExpect(jsonPath("$.currentRound.guesses").value(1))
                .andExpect(jsonPath("$.currentRound.feedback").value(Matchers.containsInRelativeOrder("CORRECT", "CORRECT", "CORRECT", "CORRECT", "ABSENT")))
                .andExpect(jsonPath("$.currentRound.hint").value(Matchers.containsInRelativeOrder("g", "r", "o", "e", ".")))
                .andExpect(jsonPath("$.gameState").value("PLAYING"))
                .andExpect(jsonPath("$.wordToGuess").value("Word is Hidden"));
    }

    @Test
    @DisplayName("Test if excisting game can be retrieved")
    void testStartGame() throws Exception {

        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        MockHttpServletResponse response = mockMvc.perform(requestGame).andReturn().getResponse();

        int gameId = JsonPath.read(response.getContentAsString(), "$.id");

        ReturnGameDto returnGameDto = new ReturnGameDto();
        returnGameDto.id = (long) gameId;
        String requestBody = new ObjectMapper().writeValueAsString(returnGameDto);


        RequestBuilder requestAttempt = MockMvcRequestBuilders
                .get("/lingotrainer/getgame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestAttempt).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((long) gameId))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundCounter").value(1))
                .andExpect(jsonPath("$.currentRound.guesses").value(0))
                // no feedback to test
                .andExpect(jsonPath("$.currentRound.hint").value(Matchers.containsInRelativeOrder("g", ".", ".", ".", ".")))
                .andExpect(jsonPath("$.gameState").value("PLAYING"))
                .andExpect(jsonPath("$.wordToGuess").value("Word is Hidden"));
    }

    @Test
    @DisplayName("Test if game can be Lost")
    void testAttemptWordLost() throws Exception {

        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        MockHttpServletResponse response = mockMvc.perform(requestGame).andReturn().getResponse();

        int gameId = JsonPath.read(response.getContentAsString(), "$.id");

        AttemptDto attemptDto = new AttemptDto();
        attemptDto.id = (long) gameId;
        attemptDto.attempt = "groes";
        String requestBody = new ObjectMapper().writeValueAsString(attemptDto);
        RequestBuilder requestAttempt = MockMvcRequestBuilders
                .post("/lingotrainer/attemptword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(requestAttempt);
        }

        ReturnGameDto returnGameDto = new ReturnGameDto();
        returnGameDto.id = (long) gameId;
        String requestBody2 = new ObjectMapper().writeValueAsString(returnGameDto);

        RequestBuilder requestExcisingGame = MockMvcRequestBuilders
                .get("/lingotrainer/getgame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2);

        mockMvc.perform(requestExcisingGame).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value((long) gameId))
                .andExpect(jsonPath("$.score").value(0))
                .andExpect(jsonPath("$.roundCounter").value(1))
                .andExpect(jsonPath("$.currentRound.guesses").value(5))
                .andExpect(jsonPath("$.currentRound.feedback").value(Matchers.containsInRelativeOrder("CORRECT", "CORRECT", "CORRECT", "CORRECT", "ABSENT")))
                .andExpect(jsonPath("$.currentRound.hint").value(Matchers.containsInRelativeOrder("g", "r", "o", "e", ".")))
                .andExpect(jsonPath("$.gameState").value("LOST"))
                .andExpect(jsonPath("$.wordToGuess").value("groep"));
    }

    @Test
    @DisplayName("Test attemptWord that throws an exception when trying to attempt a lost game")
    void testAttemptWordThrowsException() throws Exception {

        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        MockHttpServletResponse response = mockMvc.perform(requestGame).andReturn().getResponse();

        int gameId = JsonPath.read(response.getContentAsString(), "$.id");

        AttemptDto attemptDto = new AttemptDto();
        attemptDto.id = (long) gameId;
        attemptDto.attempt = "groes";
        String requestBody = new ObjectMapper().writeValueAsString(attemptDto);
        RequestBuilder requestAttempt = MockMvcRequestBuilders
                .post("/lingotrainer/attemptword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        for (int i = 0; i < 5; i++) {
            mockMvc.perform(requestAttempt);
        }

        mockMvc.perform(requestAttempt).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test getGame that throws exception when game id not found")
    void testGetGameThrowsException() throws Exception {

        RequestBuilder requestGame = MockMvcRequestBuilders
                .post("/lingotrainer/newgame");

        MockHttpServletResponse response = mockMvc.perform(requestGame).andReturn().getResponse();

        int gameId = JsonPath.read(response.getContentAsString(), "$.id");

        ReturnGameDto returnGameDto = new ReturnGameDto();
        returnGameDto.id = (long) gameId + 123;
        String requestBody = new ObjectMapper().writeValueAsString(returnGameDto);

        RequestBuilder requestExcisingGame = MockMvcRequestBuilders
                .get("/lingotrainer/getgame")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestExcisingGame).andExpect(status().isBadRequest());
    }
}