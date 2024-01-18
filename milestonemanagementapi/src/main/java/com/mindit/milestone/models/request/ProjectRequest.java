package com.mindit.milestone.models.request;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectRequest {
  private String owner;
  private String name;
  private String description;
  private Date startDate;
  private Date endDate;
  private String status;
}
