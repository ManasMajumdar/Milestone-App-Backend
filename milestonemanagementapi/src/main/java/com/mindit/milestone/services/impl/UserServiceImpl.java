package com.mindit.milestone.services.impl;

import static com.mindit.milestone.utils.EncryptionService.encrypt;
import static com.mindit.milestone.utils.PasswordGenerator.generateRandomPassword;

import com.mindit.milestone.contants.enums.UserRole;
import com.mindit.milestone.data.entity.User;
import com.mindit.milestone.data.repository.UserRepository;
import com.mindit.milestone.exception.CommonErrorCode;
import com.mindit.milestone.exception.MMException;
import com.mindit.milestone.models.request.UserRequest;
import com.mindit.milestone.models.response.TokenResponse;
import com.mindit.milestone.models.response.UserResponse;
import com.mindit.milestone.models.response.UsersResponse;
import com.mindit.milestone.services.UserService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired private AudienceServiceImpl audienceService;

  private static final String str = "Provided UserId ";

  @Override
  public List<UserResponse> getAllUsers(String searchText) {
    try {
      List<User> users;
      if (searchText != null && !searchText.isEmpty()) {
        log.info("Getting data from matching fields for " + searchText + " search .");
      }
      users =
          userRepository.findByFirstNameOrEmailContainingIgnoreCaseAndRoleAndNotDeleted(
              searchText, UserRole.USER);
      List<UserResponse> userResponses = userMapping(users);
      return userResponses;
    } catch (MMException e) {
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private List<UserResponse> userMapping(List<User> users) {
    List<UserResponse> userLists = new ArrayList<>();
    if (!users.isEmpty()) {
      log.info("Your result is being mapped to response");
      for (User userdata : users) {
        UserResponse userList = new UserResponse();
        userList.setId(userdata.getId());
        userList.setEmpid(userdata.getEmpid());
        userList.setFirstName(userdata.getFirstName());
        userList.setLastName(userdata.getLastName());
        userList.setEmail(userdata.getEmail());
        userList.setIsActive(userdata.getIsActive());
        userLists.add(userList);
      }
    }
    return userLists;
  }

  @Override
  public UsersResponse addUser(UserRequest userRequest) {
    UsersResponse userResponse = new UsersResponse();
    try {
      User user = userFromRequest(userRequest);
      userRepository.save(user);

      Optional<User> userOptional = userRepository.findByEmail(userRequest.getEmail());
      if (userOptional.isPresent()) {
        userResponse = userFromResponse(userOptional.get());
      }
      return userResponse;
    } catch (MMException e) {
      throw e;
    } catch (Exception ex) {
      log.error("Error occurred while adding a user: [{}]", ex.getMessage());
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  private User userFromRequest(UserRequest userRequest)
      throws NoSuchAlgorithmException, InvalidKeySpecException, MessagingException, IOException {
    User user = new User();
    UUID uuid = UUID.randomUUID();
    user.setId(uuid.toString());

    if (!userRequest.getFirstName().isEmpty() && !userRequest.getLastName().isEmpty()) {
      user.setFirstName(userRequest.getFirstName());
      user.setLastName(userRequest.getLastName());
    } else {
      throw new MMException(CommonErrorCode.USERNAME_NOT_VALID);
    }

    if (isValidEmail(userRequest.getEmail())) {
      user.setEmpid(userRequest.getEmpid());
      user.setEmail(userRequest.getEmail());
      user.setIsActive(true);
      user.setIsDeleted(false);
      user.setRole(UserRole.USER);

      int passwordLength = 12;
      String randomPassword = generateRandomPassword(passwordLength);
      String encryptedPassword = encrypt(randomPassword);
      String userId = userRequest.getEmail();
      String inboxMessage = "Your userId is " + userId + ", and user password is " + randomPassword;
      audienceService.sendMail(user.getEmail(), null, inboxMessage);
      user.setPassword(encryptedPassword);
    } else {
      throw new MMException(CommonErrorCode.BAD_REQUEST);
    }
    return user;
  }

  private boolean isValidEmail(String email) {
    return email != null && email.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^. -]+@[a-zA-Z0-9. -]+$");
  }

  private UsersResponse userFromResponse(User user) {
    UsersResponse userResponse = new UsersResponse();
    userResponse.setId(user.getId());
    userResponse.setEmpid(user.getEmpid());
    userResponse.setEmail(user.getEmail());
    userResponse.setFirstName(user.getFirstName());
    userResponse.setLastName(user.getLastName());
    return userResponse;
  }

  @Override
  public Void updateUserStatus(String userId, Boolean status) {
    try {
      Optional<User> optionalUser = userRepository.findById(userId);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        user.setIsActive(status);
        userRepository.save(user);
      } else {
        log.error(str + userId + " does not exist");
        throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
      }
    } catch (MMException e) {
      throw e;
    }
    return null;
  }

  @Override
  public Void deleteUser(String id) {
    try {
      Optional<User> optionalUser = userRepository.findById(id);
      if (optionalUser.isPresent()) {
        User user = optionalUser.get();
        user.setIsDeleted(true);
        userRepository.save(user);
        log.info(str + id + " is set to deleted");
      } else {
        log.error(str + id + " does not exist");
        throw new MMException(CommonErrorCode.DATA_NOT_FOUND);
      }
      return null;
    } catch (MMException e) {
      throw e;
    }
  }

  @Override
  public TokenResponse loginResponse(String email, String password) {
    try {

      Optional<User> userData = userRepository.findByEmail(email);
      TokenResponse tokenResponse = new TokenResponse();
      if (userData.isEmpty()) {
        throw new MMException(CommonErrorCode.USERNAME_NOT_VALID);
      }
      String encryptedPassword = encrypt(password);
      if (String.valueOf(userData.get().getRole()).equals("ADMIN")
          && encryptedPassword.equals(userData.get().getPassword())) {
        tokenResponse.setMessage("Login Successfully");
        tokenResponse.setEmail(userData.get().getEmail());
        tokenResponse.setRole("ADMIN");
      } else if (String.valueOf(userData.get().getRole()).equals("USER")
          && encryptedPassword.equals(userData.get().getPassword())) {
        tokenResponse.setMessage("Login Successfully");
        tokenResponse.setEmail(userData.get().getEmail());
        tokenResponse.setRole("USER");
      } else {
        throw new MMException(CommonErrorCode.PASSWORD_NOT_VALID);
      }

      return tokenResponse;
    } catch (MMException e) {
      throw e;
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }
}
