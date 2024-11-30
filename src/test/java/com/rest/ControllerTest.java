package com.rest;

import com.rest.config.AppProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest(
    classes = TestConfig.class,
    properties = "spring.config.name=application-test"
)
public class ControllerTest {

  @InjectMocks
  private TestController testController;

  @Mock
  private TestService testService;

  @BeforeEach
  void setUp() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/test");
    request.setServerPort(8080);
    request.setScheme("http");
    request.setServerName("localhost");

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }

  @AfterEach
  void tearDown() {
    RequestContextHolder.resetRequestAttributes();
  }

  @Test
  void getRestResponseTest() {

    when(testService.getTest()).thenReturn(List.of(new String[]{"test1", "test2"}));

    ResponseEntity<RestResponse<List<String>>> result = testController.getData();

    System.out.println(result.getBody());
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