package com.mindit.milestone.controllers;

import com.mindit.milestone.models.request.UserRequest;
import com.mindit.milestone.models.response.UserResponse;
import com.mindit.milestone.models.response.UsersResponse;
import com.mindit.milestone.services.UserService;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/v1/user")
@AllArgsConstructor
@CrossOrigin
public class UserController {

  private final UserService userService;

  @GetMapping()
  @CrossOrigin
  public ResponseEntity<List<UserResponse>> getAllUsers(
      @RequestParam(name = "searchText", required = false) String searchText) {
    log.info("Getting data of all users for admin");
    List<UserResponse> usersResponse = userService.getAllUsers(searchText);
    return new ResponseEntity<>(usersResponse, HttpStatus.OK);
  }

  @PostMapping()
  @CrossOrigin
  ResponseEntity<UsersResponse> addUser(@RequestBody UserRequest userRequest) {
    UsersResponse userResponse = userService.addUser(userRequest);
    return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
  }

  @PutMapping("/{userId}/{status}")
  @CrossOrigin
  public ResponseEntity<Void> updateUserStatus(
      @PathVariable("userId") String userId, @PathVariable("status") boolean status) {
    Void object = userService.updateUserStatus(userId, status);
    return new ResponseEntity<>(object, HttpStatus.OK);
  }

  @DeleteMapping("/{userId}")
  @CrossOrigin
  ResponseEntity<Void> deleteUser(@PathVariable String userId) {
    log.info("The invoked method will set the user column to is deleted");
    Void object = userService.deleteUser(userId);
    return new ResponseEntity<>(object, HttpStatus.OK);
  }
}
