package com.mindit.milestone.data.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "access_token")
@Data
@EqualsAndHashCode(callSuper = false)
public class AccessToken extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "token")
  private String token;

  @Column(name = "is_active")
  private Boolean isActive;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Override
  public String toString() {
    return "AccessToken{"
        + "id="
        + id
        + ", token='"
        + token
        + '\''
        + ", isActive="
        + isActive
        + ", userId="
        + (user != null ? user.getId() : "null")
        + // Print relevant user information
        '}';
  }
}
