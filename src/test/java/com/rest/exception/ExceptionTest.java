package com.rest.exception;

import com.rest.config.TestConfig;
import com.rest.resource.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
    classes = { TestConfig.class, WebMvcAutoConfiguration.class },
    properties = "spring.config.name=application-test"
)
@AutoConfigureMockMvc
@Import({TestController.class, BaseControllerAdvice.class})
public class ExceptionTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testException() throws Exception {
    mockMvc
        .perform(get("/api/test/custom-exception"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testRuntimeException() throws Exception {
    mockMvc
        .perform(get("/api/test/custom-runtime"))
        .andExpect(status().isInternalServerError());
  }
}
