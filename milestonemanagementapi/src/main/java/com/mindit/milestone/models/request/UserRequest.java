package com.mindit.milestone.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
  private String empid;
  private String firstName;
  private String lastName;
  private String email;
}
