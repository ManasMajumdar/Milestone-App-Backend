package com.mindit.milestone.models.request;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneRequest {
  private String name;
  private String status;
  private Date dueDate;
  private String dueDateReason;
}
