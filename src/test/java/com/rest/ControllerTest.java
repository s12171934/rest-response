package com.rest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
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

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

  @InjectMocks
  private TestController testController;

  @Mock
  private TestService testService;

  @Test
  void getRestResponseTest() {

    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI("/api/test");
    request.setServerPort(8080);
    request.setScheme("http");
    request.setServerName("localhost");

    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
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
