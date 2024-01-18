package com.mindit.milestone.services.impl;

import com.mindit.milestone.data.entity.Milestone;
import com.mindit.milestone.data.entity.Project;
import com.mindit.milestone.data.repository.MilestoneRepository;
import com.mindit.milestone.data.repository.ProjectRepository;
import com.mindit.milestone.services.ReminderService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ReminderServiceImpl implements ReminderService {
  private final ProjectRepository projectRepository;
  private final MilestoneRepository milestoneRepository;
  private final AudienceServiceImpl audienceService;

  public void sendMail(String to, String cc, String inboxBody)
      throws MessagingException, IOException {
    String message = "New project named " + inboxBody + " successfully";

    if (cc != null && !cc.isEmpty()) {
      audienceService.sendMail(to, cc, message);
    } else {
      audienceService.sendMail(to, null, message);
    }
  }

  @Override
  public void GetProjectDate() throws MessagingException, IOException {
    List<Project> allProjects = projectRepository.findAll();
    LocalDate today = LocalDate.now();
    for (Project project : allProjects) {
      LocalDate endDate = project.getEndDate().toLocalDate();
      long daysUntilEnd = endDate.toEpochDay() - today.toEpochDay();
      if (daysUntilEnd == 15
          || daysUntilEnd == 7
          || daysUntilEnd == 3
          || daysUntilEnd == 2
          || daysUntilEnd == 1) {
        sendMail(
            project.getOwner(),
            null,
            "Project Name:- "
                + project.getName()
                + "\n"
                + "End Date:- "
                + project.getEndDate().toString());
      }
    }
  }

  @Override
  public void GetMilestoneDate() throws MessagingException, IOException {
    List<Project> allProjects = projectRepository.findAll();
    LocalDate today = LocalDate.now();
    for (Project project : allProjects) {
      List<Milestone> milestones = milestoneRepository.check(project.getId());
      for (Milestone milestone : milestones) {
        LocalDate endDate = milestone.getDueDate().toLocalDate();
        long daysUntilEnd = endDate.toEpochDay() - today.toEpochDay();
        if (daysUntilEnd == 5
            || daysUntilEnd == 4
            || daysUntilEnd == 3
            || daysUntilEnd == 2
            || daysUntilEnd == 1) {
          sendMail(
              project.getOwner(),
              null,
              "Project Name:- "
                  + project.getName()
                  + "\n"
                  + "Milestone Name:- "
                  + milestone.getName()
                  + "\n"
                  + "Due Date:- "
                  + milestone.getDueDate().toString());
        }
      }
    }
  }
}
