package com.moorhuhnservice.moorhuhnservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.moorhuhnservice.moorhuhnservice.data.Question;
import com.moorhuhnservice.moorhuhnservice.repositories.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(classes = MoorhuhnServiceApplication.class)
class MoorhuhnServiceApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    void getQuestionsWithCorrectConfiguration_thenStatus200()
            throws Exception {
        Question testQuestion = new Question("configuration", "ist dies die frage?", "dies ist die correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");
        questionRepository.save(testQuestion);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);

        Cookie cookie = new Cookie("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdXRob3JpemVkIjp0cnVlLCJleHAiOjE2NTk2MDI4NzQsImlkIjoiY2w1N3hmNDFqMDAwNmhuMTFmZHg0MDFzeCIsInVzZXIiOiJhc2RmIn0.JbmVGmeg7LOZV8kkwCQZknf1dt4HudDBgcpSIrD2I1Q");
        MvcResult result = mvc.perform(get("/get-all-questions/configuration")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Question> addedQuestions = Arrays.asList(mapper.readValue(content, Question[].class));

        for (Question question : addedQuestions) {
            questionRepository.deleteById(question.getId());
        }
    }

    @Test
    void updateQuestion_thenStatus200()
            throws Exception {
        Question testQuestion = new Question("configuration", "ist dies die frage?", "dies ist die correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");
        Question testQuestionUpdated = new Question("configuration", "ist dies die frage?", "dies ist die WIRKLICH correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(testQuestionUpdated);

        Question questionToUpdate = questionRepository.save(testQuestion);
        MvcResult result = mvc.perform(put("/put-question-element-by-id/" + questionToUpdate.getId())
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        Question updatedQuestion = mapper.readValue(content, Question.class);
        questionRepository.deleteById(updatedQuestion.getId());

    }

    @Test
    void deleteQuestion_thenStatus200()
            throws Exception {
        Question testQuestion = new Question("configuration", "ist dies die frage?", "dies ist die correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");

        Question question = questionRepository.save(testQuestion);
        mvc.perform(delete("/delete-question-element-by-id/" + question.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void postQuestions_thenStatus200()
            throws Exception {
        Question testQuestion = new Question("configuration", "ist dies die frage?", "dies ist die correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");
        Question testQuestion2 = new Question("configuration", "ist dies die zweite frage?", "dies ist die correcte Antwort", "dies ist die erste falsche Antwort", "dies ist die zweite falsche Antwort", "dies ist die dritte falsche Antwort", "dies ist die vierte falsche Antwort");

        List<Question> questionList = new ArrayList<>();
        questionList.add(testQuestion);
        questionList.add(testQuestion2);

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(questionList);

        MvcResult result = mvc.perform(post("/save-all-questions")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<Question> addedQuestions = Arrays.asList(mapper.readValue(content, Question[].class));

        for (Question question : addedQuestions) {
            questionRepository.deleteById(question.getId());
        }
    }
}
