package com.mindit.milestone.services;

import com.mindit.milestone.models.request.ProjectRequest;
import com.mindit.milestone.models.response.ProjectIdResponse;
import com.mindit.milestone.models.response.ProjectResponse;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface ProjectService {
  ProjectIdResponse addProject(ProjectRequest projectRequest);

  ProjectIdResponse updateProjectInfoById(ProjectRequest projectRequest, String id)
      throws MessagingException, IOException;

  List<String> getStatus();

  List<ProjectResponse> getAllProjectDetails(String owner);

  List<Object> getProjectInfo(String name);

  ProjectResponse getProjectById(String id);
}
