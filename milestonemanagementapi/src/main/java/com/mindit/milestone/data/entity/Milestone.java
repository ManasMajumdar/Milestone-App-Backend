package com.mindit.milestone.data.entity;

import jakarta.persistence.*;
import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "milestone")
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Milestone extends BaseEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Column(name = "name")
  private String name;

  @Column(name = "status")
  private String status;

  @Column(name = "due_date")
  private Date dueDate;

  @Column(name = "due_date_reason")
  private String dueDateReason;

  @Column(name = "project_id")
  private int projectId;
}
