package com.mindit.milestone.exception;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
  int getErrorId();

  String getErrorCode();

  String getErrorMessage();

  HttpStatus getHttpStatus();

  void setErrorMessage(String errorMessage);
}
