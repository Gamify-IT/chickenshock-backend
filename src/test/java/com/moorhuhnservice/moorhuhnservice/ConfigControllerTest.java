package com.moorhuhnservice.moorhuhnservice;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moorhuhnservice.moorhuhnservice.data.Configuration;
import com.moorhuhnservice.moorhuhnservice.data.ConfigurationDTO;
import com.moorhuhnservice.moorhuhnservice.data.Question;
import com.moorhuhnservice.moorhuhnservice.data.QuestionDTO;
import com.moorhuhnservice.moorhuhnservice.data.mapper.ConfigurationMapper;
import com.moorhuhnservice.moorhuhnservice.data.mapper.QuestionMapper;
import com.moorhuhnservice.moorhuhnservice.repositories.ConfigurationRepository;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConfigControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ConfigurationMapper configurationMapper;

  @Autowired
  private QuestionMapper questionMapper;

  @Autowired
  private ConfigurationRepository configurationRepository;

  @Autowired
  private QuestionRepository questionRepository;

  private final String API_URL = "/api/v1/minigames/moorhuhn/configurations";
  private ObjectMapper objectMapper;
  private Configuration createdConfiguration;

  @BeforeEach
  public void createBasicData() {
    configurationRepository.deleteAll();
    Question questionOne = new Question("Are you cool?", "Yes", Arrays.asList("No", "Maybe"));
    Question questionTwo = new Question("Is this game cool?", "Yes", Arrays.asList("No", "Maybe"));

    Configuration configuration = new Configuration("initialConfiguration", Set.of(questionOne, questionTwo));
    createdConfiguration = configurationRepository.save(configuration);

    objectMapper = new ObjectMapper();
  }

  @AfterEach
  public void deleteBasicData() {
    configurationRepository.deleteAll();
  }

  @Test
  public void getConfigurations() throws Exception {
    MvcResult result = mvc
      .perform(get(API_URL).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    List<Configuration> configurations = Arrays.asList(objectMapper.readValue(content, Configuration[].class));

    assertSame(1, configurations.size());
  }

  @Test
  public void getQuestionsFromSpecificConfiguration() throws Exception {
    MvcResult result = mvc
      .perform(
        get(API_URL + "/" + createdConfiguration.getName() + "/questions").contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    Set<Question> questions = Set.of(objectMapper.readValue(content, Question[].class));
    Set<QuestionDTO> questionDTOs = questionMapper.questionsToQuestionDTOs(questions);

    assertEquals(questionMapper.questionsToQuestionDTOs(createdConfiguration.getQuestions()), questionDTOs);
  }

  @Test
  public void createConfiguration() throws Exception {
    ConfigurationDTO newCreatedConfiguration = new ConfigurationDTO(
      "newConfiguration",
      Set.of(new QuestionDTO("Is this a new configuration?", "Yes", Arrays.asList("Maybe", "No")))
    );
    String bodyValue = objectMapper.writeValueAsString(newCreatedConfiguration);
    MvcResult result = mvc
      .perform(post(API_URL).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isCreated())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    Configuration newCreatedConfigurationResponse = objectMapper.readValue(content, Configuration.class);
    ConfigurationDTO newCreatedConfigurationDTOResponse = configurationMapper.configurationToConfigurationDTO(
      newCreatedConfigurationResponse
    );

    assertEquals(newCreatedConfiguration.getQuestions(), newCreatedConfigurationDTOResponse.getQuestions());
    assertSame(2, configurationRepository.findAll().size());
  }

  @Test
  public void updateConfiguration() throws Exception {
    ConfigurationDTO updatedConfiguration = configurationMapper.configurationToConfigurationDTO(createdConfiguration);
    Set<QuestionDTO> newQuestionsDTO = Set.of(
      new QuestionDTO("Is this a new configuration?", "Yes", Arrays.asList("Maybe", "No"))
    );
    updatedConfiguration.setQuestions(newQuestionsDTO);
    String bodyValue = objectMapper.writeValueAsString(updatedConfiguration);
    MvcResult result = mvc
      .perform(
        put(API_URL + "/" + createdConfiguration.getName()).content(bodyValue).contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    Configuration updatedConfigurationResponse = objectMapper.readValue(content, Configuration.class);
    ConfigurationDTO updatedConfigurationDTOResponse = configurationMapper.configurationToConfigurationDTO(
      updatedConfigurationResponse
    );

    assertEquals(newQuestionsDTO, updatedConfigurationDTOResponse.getQuestions());
    assertNotEquals(
      questionMapper.questionsToQuestionDTOs(createdConfiguration.getQuestions()),
      updatedConfigurationDTOResponse.getQuestions()
    );
    assertSame(1, configurationRepository.findAll().size());
  }

  @Test
  public void addQuestionToExistingConfiguration() throws Exception {
    QuestionDTO addedQuestionDTO = new QuestionDTO(
      "What is this question about?",
      "Question",
      Arrays.asList("Nothing", "Everything")
    );

    String bodyValue = objectMapper.writeValueAsString(addedQuestionDTO);
    MvcResult result = mvc
      .perform(
        post(API_URL + "/" + createdConfiguration.getName() + "/questions")
          .content(bodyValue)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isCreated())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    Question newAddedQuestionResponse = objectMapper.readValue(content, Question.class);
    QuestionDTO newAddedQuestionDTOResponse = questionMapper.questionToQuestionDTO(newAddedQuestionResponse);

    assertEquals(addedQuestionDTO.getText(), newAddedQuestionResponse.getText());
    assertEquals(addedQuestionDTO, newAddedQuestionDTOResponse);
  }

  @Test
  public void removeQuestionFromExistingConfiguration() throws Exception {
    Question removedQuestion = createdConfiguration.getQuestions().stream().findFirst().get();
    assertTrue(questionRepository.existsById(removedQuestion.getId()));
    MvcResult result = mvc
      .perform(
        delete(API_URL + "/" + createdConfiguration.getName() + "/questions/" + removedQuestion.getId())
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    QuestionDTO removedQuestionDTOResult = objectMapper.readValue(content, QuestionDTO.class);

    assertSame(
      createdConfiguration.getQuestions().size() - 1,
      configurationRepository.findByName(createdConfiguration.getName()).getQuestions().size()
    );
    assertFalse(questionRepository.existsById(removedQuestion.getId()));
  }

  @Test
  public void updateQuestionFromExistingConfiguration() throws Exception {
    Question updatedQuestion = createdConfiguration.getQuestions().stream().findFirst().get();
    QuestionDTO updatedQuestionDTO = questionMapper.questionToQuestionDTO(updatedQuestion);
    String newText = "Is this a new updated question?";
    updatedQuestionDTO.setText(newText);

    String bodyValue = objectMapper.writeValueAsString(updatedQuestionDTO);
    MvcResult result = mvc
      .perform(
        put(API_URL + "/" + createdConfiguration.getName() + "/questions/" + updatedQuestion.getId())
          .content(bodyValue)
          .contentType(MediaType.APPLICATION_JSON)
      )
      .andExpect(status().isOk())
      .andReturn();

    String content = result.getResponse().getContentAsString();
    Question updatedQuestionResult = objectMapper.readValue(content, Question.class);

    assertEquals(newText, updatedQuestionResult.getText());
    assertEquals(newText, questionRepository.findById(updatedQuestion.getId()).get().getText());
  }
}