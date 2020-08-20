package com.hjdrsa.dynamodb.poc.error;

import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author hjd
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  
  @ExceptionHandler({RequestException.class})
  public ResponseEntity<Object> handleRequestException(final RequestException ex) {

    RestError restError = new RestError(ex.getStatus(), LocalDateTime.now(), ex.getLocalizedMessage());
    return new ResponseEntity(restError, restError.getStatus());
  }
}
