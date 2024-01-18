package com.mindit.milestone.controllers;

import com.mindit.milestone.models.request.ProjectRequest;
import com.mindit.milestone.models.response.ProjectIdResponse;
import com.mindit.milestone.models.response.ProjectResponse;
import com.mindit.milestone.services.ProjectService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(value = "/v1/projects", produces = MediaType.APPLICATION_JSON_VALUE)
@AllArgsConstructor
public class ProjectController {

  private final ProjectService projectService;

  @PostMapping()
  @CrossOrigin
  public ResponseEntity<ProjectIdResponse> addProject(@RequestBody ProjectRequest projectRequest) {
    log.info("Post request for add Project info [{}]", projectRequest.getName());
    ProjectIdResponse projectIdResponse = projectService.addProject(projectRequest);
    return new ResponseEntity<>(projectIdResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  @CrossOrigin
  public ResponseEntity<ProjectIdResponse> updateProjectInfo(
      @RequestBody ProjectRequest projectRequest, @PathVariable String id)
      throws MessagingException, IOException {
    log.info("Put request for update Project info in Controller");
    ProjectIdResponse projectIdResponse = projectService.updateProjectInfoById(projectRequest, id);
    return new ResponseEntity<>(projectIdResponse, HttpStatus.ACCEPTED);
  }

  @GetMapping("/status")
  @CrossOrigin
  public List<String> getStatus() {
    log.error("Received a request to get all status");
    return projectService.getStatus();
  }

  @GetMapping()
  @CrossOrigin
  public ResponseEntity<List<ProjectResponse>> getAllProjectDetails(
      @RequestParam(value = "owner") String owner) {
    log.info("Request in controller for get all project details");
    List<ProjectResponse> usersResponse = projectService.getAllProjectDetails(owner);
    return new ResponseEntity<>(usersResponse, HttpStatus.OK);
  }

  @GetMapping(value = "/project", produces = MediaType.APPLICATION_JSON_VALUE)
  @CrossOrigin
  public ResponseEntity<List<Object>> getProjectInfo(
      @RequestParam(value = "name", required = true) String name) {
    log.info("Request in controller for get all project details");
    List<Object> projectList = projectService.getProjectInfo(name);
    return new ResponseEntity<>(projectList, HttpStatus.OK);
  }

  @GetMapping(value = "/{id}")
  @CrossOrigin
  public ResponseEntity<ProjectResponse> getProjectById(@PathVariable String id) {
    log.info("Request project details");
    ProjectResponse projectResponse = projectService.getProjectById(id);
    return new ResponseEntity<>(projectResponse, HttpStatus.OK);
  }
}
