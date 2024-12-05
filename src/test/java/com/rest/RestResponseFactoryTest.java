package com.rest;

import com.rest.code.TestResponseCode;
import com.rest.config.TestConfig;
import com.rest.response.Pagination;
import com.rest.response.RestResponse;
import com.rest.response.RestResponseFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
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
  private HttpHeaders testHeaders;
  private Pagination pagination;

  @BeforeEach
  void setUp() {
    testHeaders = new HttpHeaders();
    testHeaders.add("Custom-Header", "test-value");

    pagination = new Pagination(1,10,100);

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
    String testData = "test-data";

    // When
    ResponseEntity<RestResponse<String>> result = RestResponseFactory.createFullResponseEntity(
        HttpStatus.OK,
        testHeaders,
        testData
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("test-value", result.getHeaders().getFirst("Custom-Header"));
    assertEquals(testData, result.getBody().result());
  }

  @Test
  void createFullResponseEntityWithListAndPaginationTest() {
    // Given
    List<String> testData = List.of("test1", "test2");

    // When
    ResponseEntity<RestResponse<List<String>>> result = RestResponseFactory.createFullResponseEntity(
        HttpStatus.OK,
        testHeaders,
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
  void createFullResponseEntityWithArrayAndPaginationTest() {
    // Given
    String[] testData = {"test1", "test2", "test3"};

    // When
    ResponseEntity<RestResponse<String[]>> result = RestResponseFactory.createFullResponseEntity(
        HttpStatus.OK,
        testHeaders,
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
    String testData = "test-data";

    // When
    ResponseEntity<RestResponse<String>> result = RestResponseFactory.createResultResponseEntity(
        HttpStatus.OK,
        testData
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(testData, result.getBody().result());
  }

  @Test
  void createHeadersResponseEntityTest() {
    // When
    ResponseEntity<RestResponse<Void>> result = RestResponseFactory.createHeaderResponseEntity(
        HttpStatus.OK,
        testHeaders
    );

    // Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals("test-value", result.getHeaders().getFirst("Custom-Header"));
  }

  @Test
  void createFullResponseEntityWithCustomResponseCodeTest() {
    // Given
    String testData = "test-data";

    //When
    ResponseEntity<RestResponse<String>> result = RestResponseFactory.createFullResponseEntity(
        TestResponseCode.TEST_RESPONSE_CODE,
        testHeaders,
        testData
    );

    //Then
    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(20000, result.getBody().status().code());
    assertEquals("TEST_RESPONSE_CODE", result.getBody().status().message());
    assertEquals("test-value", result.getHeaders().getFirst("Custom-Header"));
    assertEquals(testData, result.getBody().result());
  }

  @Test
  void setFullResponseTest() throws IOException {
    // Given
    String testData = "test-data";

    // When
    RestResponseFactory.setFullResponse(
        response,
        HttpStatus.OK,
        testHeaders,
        testData
    );

    // Then
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertEquals("test-value", response.getHeader("Custom-Header"));
    assertTrue(response.getContentAsString().contains("test-data"));
  }

  @Test
  void setFullResponseWithPaginationTest() throws IOException {
    // Given
    List<String> testData = List.of("test1", "test2");

    // When
    RestResponseFactory.setFullResponse(
        response,
        HttpStatus.OK,
        testHeaders,
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

  @Test
  void setFullResponseWithCustomResponseCodeTest() throws IOException {
    // Given
    String testData = "test-data";

    //When
    RestResponseFactory.setFullResponse(
        response,
        TestResponseCode.TEST_RESPONSE_CODE,
        testHeaders,
        testData
    );

    //Then
    assertEquals(HttpStatus.OK.value(), response.getStatus());
    assertTrue(response.getContentAsString().contains("test-data"));
    assertTrue(response.getContentAsString().contains("\"code\":20000"));
    assertTrue(response.getContentAsString().contains("\"message\":\"TEST_RESPONSE_CODE\""));
  }
}