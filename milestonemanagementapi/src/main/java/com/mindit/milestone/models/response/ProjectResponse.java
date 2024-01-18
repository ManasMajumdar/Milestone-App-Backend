package com.mindit.milestone.models.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectResponse {
  private String id;
  private String owner;
  private String name;
  private String description;
  private Date startDate;
  private Date endDate;
  private String status;
  private String milestoneName;
  private Date milestoneEndDate;
}
