package de.unistuttgart.chickenshockbackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import de.unistuttgart.chickenshockbackend.data.*;
import de.unistuttgart.chickenshockbackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.chickenshockbackend.repositories.ConfigurationRepository;
import de.unistuttgart.chickenshockbackend.repositories.GameResultRepository;
import java.io.IOException;
import java.util.*;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class GameResultControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ConfigurationMapper configurationMapper;

  @Autowired
  private ConfigurationRepository configurationRepository;

  @Autowired
  private GameResultRepository gameResultRepository;

  @Autowired
  private WireMockServer mockResultsService;

  private ObjectMapper objectMapper;
  private Configuration initialConfig;
  private ConfigurationDTO initialConfigDTO;

  private Question initialQuestion1;
  private Question initialQuestion2;

  private final String API_URL = "/results";

  @MockBean
  JWTValidatorService jwtValidatorService;
  Cookie cookie = new Cookie("access_token", "testToken");

  @BeforeEach
  public void createBasicData() throws IOException {
    ResultMocks.setupMockBooksResponse(mockResultsService);
    configurationRepository.deleteAll();
    initialQuestion1 = new Question();
    initialQuestion1.setText("Are you cool?");
    initialQuestion1.setRightAnswer("Yes");
    initialQuestion1.setWrongAnswers(Set.of("No", "Maybe"));

    initialQuestion2 = new Question();
    initialQuestion2.setText("Is this game cool?");
    initialQuestion2.setRightAnswer("Yes");
    initialQuestion2.setWrongAnswers(Set.of("No", "Maybe"));

    final Configuration configuration = new Configuration();
    configuration.setQuestions(Set.of(initialQuestion1, initialQuestion2));

    initialConfig = configurationRepository.save(configuration);
    initialConfigDTO = configurationMapper.configurationToConfigurationDTO(initialConfig);
    initialConfig
      .getQuestions()
      .stream()
      .filter(question -> question.getText().equals(initialQuestion1.getText()))
      .forEach(question -> initialQuestion1 = question);
    initialConfig
      .getQuestions()
      .stream()
      .filter(question -> question.getText().equals(initialQuestion2.getText()))
      .forEach(question -> initialQuestion2 = question);

    objectMapper = new ObjectMapper();
    DecodedJWT jwtTest = JWT.decode(JWT.create().withSubject("testUser").sign(Algorithm.none()));
    when(jwtValidatorService.validate("testToken")).thenReturn(jwtTest);
  }

  @AfterEach
  void deleteBasicData() {
    gameResultRepository.deleteAll();
    configurationRepository.deleteAll();
  }

  @Test
  void saveGameResult() throws Exception {
    List<RoundResultDTO> correctList = new ArrayList<>();
    List<RoundResultDTO> wrongList = new ArrayList<>();
    correctList.add(new RoundResultDTO(initialQuestion1.getId(), initialQuestion1.getRightAnswer()));
    wrongList.add(
      new RoundResultDTO(initialQuestion2.getId(), initialQuestion2.getWrongAnswers().stream().findFirst().get())
    );
    GameResultDTO gameResultDTO = new GameResultDTO(
      2,
      30,
      30,
      1,
      1,
      2,
      20,
      0,
      correctList,
      wrongList,
      UUID.randomUUID()
    );

    final String bodyValue = objectMapper.writeValueAsString(gameResultDTO);
    final MvcResult result = mvc
      .perform(post(API_URL).cookie(cookie).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn();

    final GameResultDTO createdGameResultDTO = objectMapper.readValue(
      result.getResponse().getContentAsString(),
      GameResultDTO.class
    );

    assertEquals(gameResultDTO, createdGameResultDTO);
  }
}
