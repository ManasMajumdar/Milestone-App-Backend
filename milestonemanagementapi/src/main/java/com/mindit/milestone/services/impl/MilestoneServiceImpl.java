package com.mindit.milestone.services.impl;

import com.mindit.milestone.data.entity.Milestone;
import com.mindit.milestone.data.entity.Project;
import com.mindit.milestone.data.repository.MilestoneRepository;
import com.mindit.milestone.data.repository.ProjectRepository;
import com.mindit.milestone.exception.CommonErrorCode;
import com.mindit.milestone.exception.MMException;
import com.mindit.milestone.models.request.MilestoneRequest;
import com.mindit.milestone.models.request.MilestoneUpdate;
import com.mindit.milestone.models.response.MilestoneIdResponse;
import com.mindit.milestone.models.response.MilestoneResponse;
import com.mindit.milestone.models.response.MilestoneUpdateResponse;
import com.mindit.milestone.services.MilestoneService;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {

  private final MilestoneRepository milestoneRepository;

  private final ProjectRepository projectRepository;

  private static final String errMsg =
      "Some error occurred while performing operations in database!";

  @Override
  public MilestoneIdResponse addMilestone(List<MilestoneRequest> milestoneRequest, String id) {
    try {
      Project project = projectRepository.checkId(id);
      MilestoneIdResponse milestoneIdResponse = new MilestoneIdResponse();
      if (project == null) {
        log.error("Project id not present");
        throw new MMException(CommonErrorCode.DATA_NOT_SAVED);
      } else {
        for (MilestoneRequest milestoneRequest1 : milestoneRequest) {
          Milestone milestone = new Milestone();
          milestone.setProjectId(Integer.parseInt(id));
          milestone.setName(milestoneRequest1.getName());
          milestone.setStatus(milestoneRequest1.getStatus());
          milestone.setDueDate(milestoneRequest1.getDueDate());
          milestone.setDueDateReason(" ");
          milestoneRepository.save(milestone);
          log.info("Data saved successfully");
          milestoneIdResponse.setId(milestone.getId());
        }
      }
      return milestoneIdResponse;
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public List<Object> getMilestoneList() {
    try {
      List<Object> milestones = milestoneRepository.checkName();
      LinkedHashSet<Object> uniqueList = new LinkedHashSet<>(milestones);
      log.info("Getting milestone lists");
      List<Object> milestoneList = new ArrayList<>(uniqueList);
      return milestoneList;
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
    }
  }

  @Override
  public List<MilestoneResponse> getMilestoneInfo() {
    try {
      List<Milestone> milestones = milestoneRepository.findAll();
      List<MilestoneResponse> milestoneResponses = new ArrayList<>();
      for (Milestone milestone : milestones) {
        MilestoneResponse milestoneResponse = new MilestoneResponse();
        milestoneResponse.setId(milestone.getId());
        milestoneResponse.setName(milestone.getName());
        milestoneResponse.setStatus(milestone.getStatus());
        milestoneResponse.setDueDate(milestone.getDueDate());
        milestoneResponse.setDueDateReason(milestone.getDueDateReason());
        milestoneResponses.add(milestoneResponse);
        log.info("Getting information of all the milestones");
      }
      return milestoneResponses;
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
    }
  }

  @Override
  public MilestoneUpdateResponse updateMilestone(List<MilestoneUpdate> milestoneUpdate, String id) {
    try {
      List<Milestone> milestones = milestoneRepository.check(id);
      if (milestones.isEmpty()) {
        log.error("Milestone not present");
        throw new MMException(CommonErrorCode.DATA_NOT_SAVED);
      } else {
        MilestoneUpdateResponse milestoneUpdateResponse = new MilestoneUpdateResponse();
        for (int i = 0; i < milestones.size(); i++) {
          List<Milestone> milestone1 = milestoneRepository.findId(milestones.get(i).getId());
          for (Milestone milestone : milestone1) {
            milestone.setId(milestoneUpdate.get(i).getId());
            milestone.setProjectId(milestone.getProjectId());
            milestone.setName(milestone.getName());
            milestone.setStatus(milestone.getStatus());
            milestone.setDueDate(milestoneUpdate.get(i).getDueDate());
            milestone.setDueDateReason(milestoneUpdate.get(i).getDueDateReason());
            milestoneRepository.save(milestone);
            milestoneUpdateResponse.setMessage("Milestone has been updated!");
          }
        }
        return milestoneUpdateResponse;
      }
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public List<MilestoneResponse> getMilestonesByProjectId(String id) {
    try {
      Optional<Project> project = projectRepository.findById(id);
      if (project.isEmpty()) {
        log.error("Project not present!");
        throw new MMException(CommonErrorCode.DATA_NOT_SAVED);
      } else {
        List<Milestone> milestones = milestoneRepository.check(id);
        if (milestones.isEmpty()) {
          log.error("Milestone not present!");
          throw new MMException(CommonErrorCode.DATA_NOT_SAVED);
        } else {
          List<MilestoneResponse> milestoneList = new ArrayList<>();
          int count = 0;
          for (Milestone milestone : milestones) {
            MilestoneResponse milestoneResponse = new MilestoneResponse();
            count = count + 1;
            milestoneResponse.setId(milestone.getId());
            milestoneResponse.setName(milestone.getName());
            milestoneResponse.setStatus(milestone.getStatus());
            milestoneResponse.setDueDate(milestone.getDueDate());
            milestoneResponse.setDueDateReason(milestone.getDueDateReason());
            milestoneList.add(milestoneResponse);
          }
          log.info("Getting milestone details as per [{}]", id);
          log.info("There are {} milestones", count);
          return milestoneList;
        }
      }
    } catch (Exception e) {
      log.error(errMsg, e);
      throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
    }
  }
}
