package com.mindit.milestone.services;

import com.mindit.milestone.models.request.UserRequest;
import com.mindit.milestone.models.response.TokenResponse;
import com.mindit.milestone.models.response.UserResponse;
import com.mindit.milestone.models.response.UsersResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

  List<UserResponse> getAllUsers(String searchText);

  UsersResponse addUser(UserRequest userRequest);

  Void updateUserStatus(String userid, Boolean status);

  Void deleteUser(String id);

  TokenResponse loginResponse(String email, String password);
}
