package com.amex.user.dao;

import com.amex.user.model.User;
import java.util.Optional;

public interface UserDao {
  Optional<User> create(User user);
  Optional<User>findById(String id);
  Optional<User> updateEmail(User user);
  User deleteById(String id);
}
