package com.mindit.milestone.models.response;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoResponse {
  private String id;
  private String projectName;
  private Date projectEndDate;
  private String nextMilestone;
  private Date milestoneEndDate;
  private String status;
}
