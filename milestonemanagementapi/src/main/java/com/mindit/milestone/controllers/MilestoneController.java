package com.mindit.milestone.controllers;

import com.mindit.milestone.models.request.MilestoneRequest;
import com.mindit.milestone.models.request.MilestoneUpdate;
import com.mindit.milestone.models.response.MilestoneIdResponse;
import com.mindit.milestone.models.response.MilestoneResponse;
import com.mindit.milestone.models.response.MilestoneUpdateResponse;
import com.mindit.milestone.services.MilestoneService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/project")
@AllArgsConstructor
public class MilestoneController {

  private final MilestoneService milestoneService;

  @PostMapping("/{id}/milestones")
  @CrossOrigin
  public ResponseEntity<MilestoneIdResponse> addMilestone(
      @RequestBody List<MilestoneRequest> milestoneRequest, @PathVariable String id) {
    log.info("Post request for add Project info [{}]", milestoneRequest.get(0).getName());
    MilestoneIdResponse milestoneIdResponse = milestoneService.addMilestone(milestoneRequest, id);
    return new ResponseEntity<>(milestoneIdResponse, HttpStatus.CREATED);
  }

  @GetMapping("/milestones")
  @CrossOrigin
  public ResponseEntity<List<Object>> getMilestoneList() {
    log.info("Get request for getting Milestone List");
    List<Object> milestoneList = milestoneService.getMilestoneList();
    return new ResponseEntity<>(milestoneList, HttpStatus.OK);
  }

  @GetMapping("/milestone")
  @CrossOrigin
  public ResponseEntity<List<MilestoneResponse>> getMilestoneInfo() {
    log.info("Get request for getting information of all the milestones!");
    List<MilestoneResponse> milestoneInfo = milestoneService.getMilestoneInfo();
    return new ResponseEntity<>(milestoneInfo, HttpStatus.OK);
  }

  @PutMapping("/{id}/milestone")
  @CrossOrigin
  public ResponseEntity<MilestoneUpdateResponse> updateMilestone(
      @RequestBody List<MilestoneUpdate> milestoneUpdate, @PathVariable String id) {
    log.info("Put request for updating Milestone info");
    MilestoneUpdateResponse milestoneUpdateResponse =
        milestoneService.updateMilestone(milestoneUpdate, id);
    return new ResponseEntity<>(milestoneUpdateResponse, HttpStatus.ACCEPTED);
  }

  @GetMapping("/{id}")
  @CrossOrigin
  public ResponseEntity<List<MilestoneResponse>> getMilestonesByProjectId(@PathVariable String id) {
    log.info("Get all milestones By Project id for [{}]", id);
    List<MilestoneResponse> milestoneList = milestoneService.getMilestonesByProjectId(id);
    return new ResponseEntity<>(milestoneList, HttpStatus.OK);
  }
}
