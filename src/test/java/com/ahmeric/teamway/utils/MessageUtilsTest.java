package com.ahmeric.teamway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

@ExtendWith(MockitoExtension.class)
class MessageUtilsTest {

  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private MessageUtils messageUtils;

  @BeforeEach
  void setUp() {
    // Create an instance of MessageUtils with the mocked MessageSource
    messageUtils = new MessageUtils(messageSource);
  }

  @Test
  void testGetMessage_GivenValidMessageKey_ShouldReturnCorrectMessage() {
    String messageKey = "valid.message.key";
    String expectedMessage = "This is a valid message.";

    when(messageSource.getMessage(messageKey, null, Locale.getDefault())).thenReturn(
        expectedMessage);

    String actualMessage = MessageUtils.getMessage(messageKey);

    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  void testGetMessage_GivenInvalidMessageKey_ShouldReturnNull() {
    String invalidMessageKey = "invalid.message.key";

    when(messageSource.getMessage(invalidMessageKey, null, Locale.getDefault())).thenReturn(null);

    String actualMessage = MessageUtils.getMessage(invalidMessageKey);

    assertThat(actualMessage).isNull();
  }
}
