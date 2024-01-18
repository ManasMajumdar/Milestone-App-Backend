package com.mindit.milestone.controllers;

import com.mindit.milestone.models.request.AudienceRequest;
import com.mindit.milestone.services.AudienceService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/audience")
@AllArgsConstructor
public class AudienceController {

  private final AudienceService audienceService;

  @PostMapping("/add")
  @CrossOrigin
  void addAudience(@RequestBody List<AudienceRequest> audienceRequest) {
    log.info("Add email from admin");
    audienceService.addAudience(audienceRequest);
  }

  @GetMapping("/get/{type}")
  @CrossOrigin
  public List<String> getAudience(@PathVariable String type) {
    log.error("Received a request to get all email addresses");
    return audienceService.getAudience(type);
  }
}
