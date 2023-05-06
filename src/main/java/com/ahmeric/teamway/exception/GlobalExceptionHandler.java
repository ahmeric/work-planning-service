package com.ahmeric.teamway.exception;


import com.ahmeric.teamway.model.response.ErrorMessage;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


  // Pattern to extract enum values from exception messages
  private static final Pattern ENUM_MSG = Pattern.compile(
      "values accepted for Enum class: \\[([^\\]]+)\\]");

  // Handle generic exceptions
  @ExceptionHandler(value = {Exception.class})
  public ResponseEntity<ErrorMessage> handleException(Exception exception) {

    String errorMessageText = exception.getLocalizedMessage() == null ? exception.toString()
        : exception.getLocalizedMessage();
    ErrorMessage errorMessage = new ErrorMessage(new Date(), errorMessageText);
    return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  // Handle custom WorkPlanningException
  @ExceptionHandler(value = {WorkPlanningException.class})
  public ResponseEntity<Object> handleException(WorkPlanningException exception) {

    String errorMessageText = exception.getErrorRegistry().getError().getDescription();
    ErrorMessage errorMessage = new ErrorMessage(new Date(), errorMessageText);
    return new ResponseEntity<>(errorMessage, exception.getErrorRegistry().getStatus());
  }

  // Handle validation errors
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public final ResponseEntity<ErrorMessage> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {

    FieldError fieldError = exception.getBindingResult().getFieldErrors().get(0);
    ErrorMessage errorMessage = new ErrorMessage(new Date(), fieldError.getDefaultMessage());
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

  // Handle JSON parsing errors
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorMessage> handleJsonErrors(HttpMessageNotReadableException exception) {
    if (exception.getCause() != null && exception.getCause() instanceof InvalidFormatException) {
      Matcher match = ENUM_MSG.matcher(exception.getCause().getMessage());
      if (match.find()) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(),
            "The enum value should be: " + match.group(1));
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
      }
    }
    ErrorMessage errorMessage = new ErrorMessage(new Date(), exception.getMessage());
    return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
  }

}
