package com.mindit.milestone.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
  private String id;
  private String empid;
  private String firstName;
  private String lastName;
  private String email;
}
