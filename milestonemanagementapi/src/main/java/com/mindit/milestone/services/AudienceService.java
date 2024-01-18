package com.mindit.milestone.services;

import com.mindit.milestone.models.request.AudienceRequest;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface AudienceService {
  void addAudience(List<AudienceRequest> audienceRequest);

  List<String> getAudience(String type);
}
