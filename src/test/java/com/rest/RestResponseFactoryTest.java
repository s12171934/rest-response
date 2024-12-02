package com.rest;

import com.rest.config.AppProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
    classes = TestConfig.class,
    properties = "spring.config.name=application-test"
)
public class RestResponseFactoryTest {

  private MockHttpServletResponse response;

  @BeforeEach
  void setUp() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/test");
    request.setServerPort(8080);
    request.setScheme("http");
    request.setServerName("localhost");

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    response = new MockHttpServletResponse();
  }

  @AfterEach
  void tearDown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void createFullResponseEntityTest() {
    // Given
    HttpHeaders headers = new HttpHeaders();
    headers.add("Custom-Header", "test-value");
    List<String> testData = List.of("test1", "test2");

    // When
    ResponseEntity<RestResponse<List<String>>> result = RestResponseFactory.createFullResponseEntity(
        HttpStatus.OK,
        headers,
        testData
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("test-value", result.getHeaders().getFirst("Custom-Header"));
    assertEquals(testData, result.getBody().result());
  }

  @Test
  void createFullResponseEntityWithPaginationTest() {
    // Given
    HttpHeaders headers = new HttpHeaders();
    List<String> testData = List.of("test1", "test2");
    Pagination pagination = new Pagination(1,10,100);

    // When
    ResponseEntity<RestResponse<List<String>>> result = RestResponseFactory.createFullResponseEntity(
        HttpStatus.OK,
        headers,
        testData,
        pagination
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(testData, result.getBody().result());
    assertNotNull(result.getBody().metaData().pagination());
    assertEquals(1, result.getBody().metaData().pagination().currentPage());
    assertEquals(10, result.getBody().metaData().pagination().totalPages());
    assertEquals(100, result.getBody().metaData().pagination().totalItems());
  }

  @Test
  void createBasicResponseEntityTest() {
    // When
    ResponseEntity<RestResponse<Void>> result = RestResponseFactory.createBasicResponseEntity(HttpStatus.OK);

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertNull(result.getBody().result());
  }

  @Test
  void createResultResponseEntityTest() {
    // Given
    List<String> testData = List.of("test1", "test2");

    // When
    ResponseEntity<RestResponse<List<String>>> result = RestResponseFactory.createResultResponseEntity(
        HttpStatus.OK,
        testData
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(testData, result.getBody().result());
  }

  @Test
  void setFullResponseTest() throws IOException {
    // Given
    HttpHeaders headers = new HttpHeaders();
    headers.add("Custom-Header", "test-value");
    List<String> testData = List.of("test1", "test2");

    // When
    RestResponseFactory.setFullResponse(
        response,
        HttpStatus.OK,
        headers,
        testData
    );

    // Then
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("test-value", response.getHeader("Custom-Header"));
    assertTrue(response.getContentAsString().contains("test1"));
    assertTrue(response.getContentAsString().contains("test2"));
  }

  @Test
  void setFullResponseWithPaginationTest() throws IOException {
    // Given
    HttpHeaders headers = new HttpHeaders();
    List<String> testData = List.of("test1", "test2");
    Pagination pagination = new Pagination(1,10,100);

    // When
    RestResponseFactory.setFullResponse(
        response,
        HttpStatus.OK,
        headers,
        testData,
        pagination
    );

    // Then
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("test1"));
    assertTrue(response.getContentAsString().contains("test2"));
    assertTrue(response.getContentAsString().contains("\"currentPage\":1"));
    assertTrue(response.getContentAsString().contains("\"totalPages\":10"));
    assertTrue(response.getContentAsString().contains("\"totalItems\":100"));
  }
}

@RestController
class TestController {

  private final TestService testService;

  @Autowired
  public TestController(TestService testService) {

    this.testService = testService;
  }

  @GetMapping("/api/test")
  public ResponseEntity<RestResponse<List<String>>> getData() {

    return RestResponseFactory.createResultResponseEntity(HttpStatus.OK, testService.getTest());
  }
}

@Service
class TestService {

  public List<String> getTest() {
    return List.of(new String[]{"test1", "test2"});
  }
}

@Configuration
@EnableConfigurationProperties(AppProperties.class)
class TestConfig {}