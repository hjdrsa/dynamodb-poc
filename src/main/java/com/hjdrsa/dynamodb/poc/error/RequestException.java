package com.hjdrsa.dynamodb.poc.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 *
 * @author hjd
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RequestException extends RuntimeException {
  
  private HttpStatus status;
  private String message;
}
