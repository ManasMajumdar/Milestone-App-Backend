package com.mindit.milestone.data.entity;

import com.mindit.milestone.contants.enums.UserRole;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "user")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class User extends BaseEntity {

  @Id
  @Column(name = "id")
  private String id;

  @Column(name = "empid")
  private String empid;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "email")
  private String email;

  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private UserRole role;

  @Column(name = "is_active")
  private Boolean isActive;

  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<AccessToken> tokens;

  public User(String id, String empid, String firstName, String lastName, String email) {
    this.id = id;
    this.empid = empid;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public User() {}

  @Override
  public String toString() {
    return "User{"
        + "id='"
        + id
        + '\''
        + ", empid="
        + empid
        + ", firstName='"
        + firstName
        + '\''
        + ", lastName='"
        + lastName
        + '\''
        + ", email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", role="
        + role
        + ", isActive="
        + isActive
        + ", isDeleted="
        + isDeleted
        + ", tokens="
        + tokens
        + '}';
  }
}
