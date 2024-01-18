package com.mindit.milestone.services.impl;

import static com.mindit.milestone.utils.EmailUtil.isValidEmail;

import com.mindit.milestone.data.entity.Milestone;
import com.mindit.milestone.data.entity.Project;
import com.mindit.milestone.data.repository.MilestoneRepository;
import com.mindit.milestone.data.repository.ProjectRepository;
import com.mindit.milestone.exception.CommonErrorCode;
import com.mindit.milestone.exception.MMException;
import com.mindit.milestone.models.request.ProjectRequest;
import com.mindit.milestone.models.response.ProjectIdResponse;
import com.mindit.milestone.models.response.ProjectInfoResponse;
import com.mindit.milestone.models.response.ProjectResponse;
import com.mindit.milestone.services.ProjectService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;

  @Autowired private AudienceServiceImpl audienceService;

  private final MilestoneRepository milestoneRepository;

  private static final String errMsg =
      "Some error occurred while performing operations in database!";

  @Override
  public ProjectIdResponse addProject(ProjectRequest projectRequest) {
    try {
      if (isValidEmail(projectRequest.getOwner())) {
        ProjectIdResponse projectIdResponse = new ProjectIdResponse();
        Project project = new Project();
        project.setName(projectRequest.getName());
        project.setDescription(projectRequest.getDescription());
        project.setStartDate(projectRequest.getStartDate());
        project.setEndDate(projectRequest.getEndDate());
        project.setStatus(projectRequest.getStatus());
        project.setOwner(projectRequest.getOwner());
        projectRepository.save(project);

        sendMail(projectRequest.getOwner(), null, projectRequest.getName() + " added");

        log.info("Data saved successfully");
        projectIdResponse.setId(String.valueOf(project.getId()));
        return projectIdResponse;
      }
      log.error("Invalid email format");
      throw new MMException(CommonErrorCode.EMAIL_NOT_VALID);
    } catch (Exception e) {
      log.error(errMsg);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

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
  public ProjectIdResponse updateProjectInfoById(ProjectRequest projectRequest, String id)
      throws MessagingException, IOException {
    try {
      Optional<Project> optionalProject = projectRepository.findById(id);
      if (optionalProject.isPresent()) {
        if (isValidEmail(projectRequest.getOwner())) {
          Project existingProject = optionalProject.get();
          existingProject.setName(projectRequest.getName());
          existingProject.setDescription(projectRequest.getDescription());
          existingProject.setStartDate(projectRequest.getStartDate());
          existingProject.setEndDate(projectRequest.getEndDate());
          existingProject.setStatus(projectRequest.getStatus());
          existingProject.setOwner(projectRequest.getOwner());
          existingProject = projectRepository.save(existingProject);
          sendMail(projectRequest.getOwner(), null, projectRequest.getName() + " added");
          log.info("Data updated successfully");
          ProjectIdResponse projectIdResponse = new ProjectIdResponse();
          projectIdResponse.setId(String.valueOf(existingProject.getId()));
          return projectIdResponse;
        }
        throw new MMException(CommonErrorCode.EMAIL_NOT_VALID);
      }
      log.error("Project not found with ID: " + id);
      throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
    } catch (Exception e) {
      log.error(errMsg, e);
      throw e;
    }
  }

  @Override
  public List<String> getStatus() {
    try {
      List<Project> projects = projectRepository.findAll();
      List<String> statusList = projects.stream().map(Project::getStatus).toList();
      return statusList;
    } catch (Exception e) {
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public List<ProjectResponse> getAllProjectDetails(String email) {
    try {
      List<ProjectResponse> projectResponses = null;
      if (email.isEmpty()) {
        List<Project> projects = projectRepository.findAll();
        projectResponses = projectResponseSet(projects);
      } else {
        List<Project> projects = projectRepository.findByOwner(email);
        projectResponses = projectResponseSet(projects);
      }
      return projectResponses;
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private List<ProjectResponse> projectResponseSet(List<Project> projects) {
    List<ProjectResponse> projectResponses = new ArrayList<>();
    for (Project project : projects) {
      Optional<Milestone> milestone = milestoneRepository.findByProjectId(project.getId());
      ProjectResponse projectResponse = new ProjectResponse();
      projectResponse.setId(project.getId());
      projectResponse.setOwner(project.getOwner());
      projectResponse.setName(project.getName());
      projectResponse.setDescription(project.getDescription());
      projectResponse.setStartDate(project.getStartDate());
      projectResponse.setEndDate(project.getEndDate());
      projectResponse.setStatus(project.getStatus());
      if (milestone.isPresent()) {
        projectResponse.setMilestoneName(milestone.get().getName());
        projectResponse.setMilestoneEndDate(milestone.get().getDueDate());
      }
      projectResponses.add(projectResponse);
    }
    return projectResponses;
  }

  @Override
  public List<Object> getProjectInfo(String name) {
    try {
      List<Project> projects = projectRepository.findByName(name);
      List<Object> projectList = new ArrayList<>();

      if (projects.isEmpty()) {
        log.error("Data not found for project: {}", name);
        throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
      }

      for (Project project : projects) {
        Date todayDate = new Date();
        Date latestDate = null;

        List<Milestone> milestones = milestoneRepository.checkId(project.getId());
        List<Milestone> milestonesdetails = milestoneRepository.check(project.getId());

        List<Date> dataValue = new ArrayList<>();

        for (Milestone milestone : milestonesdetails) {
          if (milestone.getDueDate().compareTo(todayDate) > 0) {
            dataValue.add(milestone.getDueDate());
          }
        }

        if (!dataValue.isEmpty()) {
          Date minDate = Collections.min(dataValue);
          latestDate = minDate;
        }

        ProjectInfoResponse projectInfoResponse = new ProjectInfoResponse();
        projectInfoResponse.setId(project.getId());
        projectInfoResponse.setProjectName(project.getName());

        if (!milestones.isEmpty()) {
          projectInfoResponse.setNextMilestone(milestones.get(0).getName());
          projectInfoResponse.setMilestoneEndDate((java.sql.Date) latestDate);
        } else {
          projectInfoResponse.setMilestoneEndDate(null);
        }

        projectInfoResponse.setProjectEndDate(project.getEndDate());
        projectInfoResponse.setStatus(project.getStatus());

        projectList.add(projectInfoResponse);
      }

      return projectList;

    } catch (Exception e) {
      log.error("Error while fetching project info for project: {}", name, e);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public ProjectResponse getProjectById(String id) {
    try {
      Optional<Project> project = projectRepository.findById(id);
      ProjectResponse projectResponse = new ProjectResponse();
      if (project.isPresent()) {
        projectResponse.setDescription(project.get().getDescription());
        projectResponse.setName(project.get().getName());
        projectResponse.setStatus(project.get().getStatus());
        projectResponse.setStartDate(project.get().getStartDate());
        projectResponse.setEndDate(project.get().getEndDate());
        projectResponse.setOwner(project.get().getOwner());
        projectResponse.setId(project.get().getId());
        return projectResponse;
      } else {
        log.error("Couldn't Find project");
        throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
      }
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
