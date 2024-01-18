package com.mindit.milestone.services;

import com.mindit.milestone.models.request.MilestoneRequest;
import com.mindit.milestone.models.request.MilestoneUpdate;
import com.mindit.milestone.models.response.MilestoneIdResponse;
import com.mindit.milestone.models.response.MilestoneResponse;
import com.mindit.milestone.models.response.MilestoneUpdateResponse;
import java.util.List;

public interface MilestoneService {
  MilestoneIdResponse addMilestone(List<MilestoneRequest> milestoneRequest, String id);

  List<Object> getMilestoneList();

  List<MilestoneResponse> getMilestoneInfo();

  MilestoneUpdateResponse updateMilestone(List<MilestoneUpdate> milestoneUpdate, String id);

  List<MilestoneResponse> getMilestonesByProjectId(String id);
}
