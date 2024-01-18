package com.mindit.milestone.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AudienceRequest {
  private String email;
  private String type;
  private String inbox;
}
