package com.hjdrsa.dynamodb.poc.event;

import com.fasterxml.jackson.annotation.JsonView;
import com.hjdrsa.dynamodb.poc.config.View;
import com.hjdrsa.dynamodb.poc.error.RequestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author hjd
 */
@RestController
@RequestMapping(path = "events")
@Slf4j
public class EventController {

  @Autowired
  private EventRepository eventRepository;

  @JsonView(View.List.class)
  @GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
  @Operation(
          summary = "Find all users events",
          description = "Find events created by a user",
          responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all events for user"),}
  )
  public ResponseEntity<List<Event>> findAll(@AuthenticationPrincipal UsernamePasswordAuthenticationToken principal) {
    UserDetails userDetails = (UserDetails) principal.getPrincipal();
    List<Event> events = eventRepository.findByUserId(userDetails.getUsername());

    return ResponseEntity.ok(events);
  }

  @JsonView(View.One.class)
  @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
          summary = "Find users events by id",
          description = "Find a event by user id and event id",
          responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved event by id"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No representation has been found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                              @ExampleObject(value = "{\n"
                                      + "  \"status\": \"NOT_FOUND\",\n"
                                      + "  \"timestamp\": \"19-08-2020 03:11:06\",\n"
                                      + "  \"message\": \"Message with id -> {} could not be found \"\n"
                                      + "}")
                            }
                    )
            )
          }
  )
  public ResponseEntity<Event> findById(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id) {
    return ResponseEntity.ok(eventRepository.findByUserIdAndId(userDetails.getUsername(), id).orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, String.format("Message with id -> %s could not be found ", id))));
  }

  @JsonView(View.Add.class)
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
          summary = "Add a new event",
          description = "Add a new event",
          responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully added a new event")
          }
  )
  public ResponseEntity<Event> newEvent(@AuthenticationPrincipal UserDetails userDetails, @RequestBody Event newEvent) {
    newEvent.setUserId(userDetails.getUsername());
    newEvent.setId(null);
    return ResponseEntity.ok(eventRepository.save(newEvent));
  }

  @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(
          summary = "Update an event",
          description = "Update an event with new data",
          responses = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated a event"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No representation has been found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {
                              @ExampleObject(value = "{\n"
                                      + "  \"status\": \"NOT_FOUND\",\n"
                                      + "  \"timestamp\": \"19-08-2020 03:11:06\",\n"
                                      + "  \"message\": \"Message with id -> {} could not be found \"\n"
                                      + "}")
                            }
                    )
            )
          }
  )
  public ResponseEntity<Event> updateEvent(@AuthenticationPrincipal UserDetails userDetails, @PathVariable String id, @RequestBody Event updateEvent) {
    return ResponseEntity.ok(eventRepository.findByUserIdAndId(userDetails.getUsername(), id).map(event -> {
      event.setDate(updateEvent.getDate());
      event.setName(updateEvent.getName());
      System.out.println(event);
      return eventRepository.save(event);
    }).orElseThrow(() -> new RequestException(HttpStatus.NOT_FOUND, String.format("Message with id -> %s could not be found ", id))));
  }
}
