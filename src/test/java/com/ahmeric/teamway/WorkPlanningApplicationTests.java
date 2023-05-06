package com.ahmeric.teamway;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WorkPlanningApplicationTests {

  @Test
  void contextLoads() {
    // Ensure that the Spring context loads without any exceptions
    Assertions.assertDoesNotThrow(() -> WorkPlanningApplication.main(new String[]{}));
  }

}
