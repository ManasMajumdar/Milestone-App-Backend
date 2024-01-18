package com.mindit.milestone.controllers;

// import com.mindit.milestone.filters.JwtHelper;
import com.mindit.milestone.models.request.LoginRequest;
import com.mindit.milestone.models.response.TokenResponse;
import com.mindit.milestone.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin
public class AuthController {

  //  private final AuthService authService;

  //  @Autowired private UserDetailsService userDetailsService;

  //  @Autowired private AuthenticationManager manager;

  //  @Autowired private JwtHelper helper;

  @Autowired private UserService userService;

  @PostMapping()
  public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest loginRequest) {
    log.info("Login request for user [{}]", loginRequest.getEmail());
    /*    TokenResponse loginResponse = authService.login(loginRequest);

    this.doAuthenticate(loginRequest.getEmail(), loginRequest.getPassword());

    UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
    String token = this.helper.generateToken(userDetails);*/

    TokenResponse loginResponse =
        userService.loginResponse(loginRequest.getEmail(), loginRequest.getPassword());

    //    TokenResponse response = TokenResponse.builder().token("Hello").build();
    return new ResponseEntity<>(loginResponse, HttpStatus.OK);
  }

  /* private void doAuthenticate(String email, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);
    try {
      manager.authenticate(authenticationToken);
    } catch (BadCredentialsException e) {
      throw new BadCredentialsException("Invalid userName or Password");
    }
  }

  @ExceptionHandler(BadCredentialsException.class)
  public String exceptionHandler() {
    return "Credential Invalid";
  }*/
}
