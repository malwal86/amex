package com.amex.user;

import com.amex.user.error.UserNotFoundException;
import com.amex.user.model.User;

public interface UserService {
  User create(String email, String name);
  User findById(String id) throws UserNotFoundException;
  User updateEmail(String id, String email) throws UserNotFoundException;
  boolean deleteById(String id);
}
