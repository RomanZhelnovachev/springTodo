package com.emobile.springtodo.controller;

import com.emobile.springtodo.config.PostgresTestContainer;
import com.emobile.springtodo.dto.CreateTaskRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DisplayName("Интеграционные тесты TaskController")
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String baseUrl;

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", PostgresTestContainer.container::getJdbcUrl);
        registry.add("spring.datasource.username", PostgresTestContainer.container::getUsername);
        registry.add("spring.datasource.password", PostgresTestContainer.container::getPassword);
    }

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/v1/tasks";
    }

    @Test
    @DisplayName("Создание задачи")
    void testCreateTask() throws Exception {
        CreateTaskRequest request = new CreateTaskRequest("Тестовая задача", "Иван", "Описание задачи");
        String requestJson = objectMapper.writeValueAsString(request);
        HttpHeaders headers = jsonHeaders();
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/create", new HttpEntity<>(requestJson, headers),
                String.class);
        String responseBody = response.getBody();
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(responseBody).isNotNull();
        JSONAssert.assertEquals("""
                {
                  "name": "Тестовая задача",
                  "author": "Иван",
                  "description": "Описание задачи",
                  "status": "TODO"
                }
                """, responseBody, JSONCompareMode.LENIENT);
        JsonNode node = objectMapper.readTree(responseBody);
        assertThat(node.has("id")).isTrue();
        assertThat(node.get("id").asText()).matches("[0-9a-f-]{36}");
    }

    @Test
    @DisplayName("Поиск задачи по ID")
    void testGetTaskById() throws Exception {
        String taskId = createTaskViaHttp("Задача для GET", "Автор");
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "/" + taskId, String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        JSONAssert.assertEquals("""
                {
                  "id": "%s",
                  "name": "Задача для GET",
                  "author": "Автор",
                  "status": "TODO"
                }
                """.formatted(taskId), response.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    @DisplayName("Постраничный список всех задач")
    void testGetAllTasks() throws JSONException {
        ResponseEntity<String> response = restTemplate.getForEntity(baseUrl + "?limit=10&offset=0", String.class);
        assertThat(response.getStatusCode().value()).isEqualTo(200);
        JSONAssert.assertEquals("""
                {
                  "content": [],
                  "totalElements": 0,
                  "totalPages": 0,
                  "size": 10,
                  "number": 0,
                  "first": true,
                  "last": true
                }
                """, response.getBody(), JSONCompareMode.LENIENT);
    }

    private String createTaskViaHttp(String name, String author) throws JsonProcessingException {
        CreateTaskRequest request = new CreateTaskRequest(name, author, "Описание");
        String requestJson = objectMapper.writeValueAsString(request);
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/create", new HttpEntity<>(requestJson,jsonHeaders()), String.class);
        JsonNode node = objectMapper.readTree(response.getBody());
        return node.get("id").asText();
    }

    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}