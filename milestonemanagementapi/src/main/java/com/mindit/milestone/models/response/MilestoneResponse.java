package com.mindit.milestone.models.response;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MilestoneResponse {
  private String id;
  private String name;
  private String status;
  private Date dueDate;
  private String dueDateReason;
}
