package com.ahmeric.teamway.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.ahmeric.teamway.model.response.ErrorMessage;
import com.ahmeric.teamway.utils.MessageUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GlobalExceptionHandlerTest {

  private GlobalExceptionHandler globalExceptionHandler;

  @BeforeEach
  public void setUp() {
    globalExceptionHandler = new GlobalExceptionHandler();
    // Mock the MessageUtils.getMessage method
    try (MockedStatic<MessageUtils> utilitiesMock = Mockito.mockStatic(MessageUtils.class)) {
      for (ErrorRegistry errorRegistry : ErrorRegistry.values()) {
        utilitiesMock.when(() -> MessageUtils.getMessage(errorRegistry.getError().getDescription()))
            .thenReturn("Mocked " + errorRegistry.getError().getDescription());
      }
    }
  }

  @Test
  void testHandleException_GivenGenericException_ShouldReturnInternalServerError() {
    Exception exception = new Exception("Test exception");

    ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleException(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

  }

  @Test
  void testHandleException_GivenWorkPlanningException_ShouldReturnCustomErrorResponse() {
    WorkPlanningException exception = new WorkPlanningException(ErrorRegistry.WORKER_NOT_FOUND);

    ResponseEntity<Object> response = globalExceptionHandler.handleException(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

  }

  @Test
  void testHandleMethodArgumentNotValidException_GivenValidationError_ShouldReturnBadRequest() {
    MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
    FieldError fieldError = new FieldError("objectName", "fieldName", "Test validation error");
    BindingResult bindingResult = mock(BindingResult.class);
    when(exception.getBindingResult()).thenReturn(bindingResult);
    when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

    ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleMethodArgumentNotValidException(
        exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().getMessage()).isEqualTo("Test validation error");
  }

  @Test
  void testHandleJsonErrors_GivenOtherJsonError_ShouldReturnBadRequest() {
    HttpMessageNotReadableException exception = new HttpMessageNotReadableException(
        "Test JSON error");

    ResponseEntity<ErrorMessage> response = globalExceptionHandler.handleJsonErrors(exception);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().getMessage()).isEqualTo("Test JSON error");
  }
}

