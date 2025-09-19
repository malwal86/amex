package com.amex.user.error;

public class UserAlreadyExistsException extends RuntimeException {
  public UserAlreadyExistsException(String id) {
    super("User with id " + id + " already exists");
  }
}
