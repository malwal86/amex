package com.amex.user;

import com.amex.user.dao.UserDao;
import com.amex.user.error.UserAlreadyExistsException;
import com.amex.user.error.UserNotFoundException;
import com.amex.user.model.User;
import java.util.Optional;
import java.util.UUID;
import javax.inject.Inject;

public class UserServiceImpl implements UserService {
  private UserDao userDao;

  @Inject
  public UserServiceImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public User create(String email, String name) {
    User newUser = new User(UUID.randomUUID().toString(), name, email);
    Optional<User> user = userDao.create(newUser);

    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UserAlreadyExistsException(newUser.getId());
    }
  }

  @Override
  public User findById(String id) throws UserNotFoundException {
    Optional<User> user = userDao.findById(id);
    
    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UserNotFoundException(id);
    }
  }

  @Override
  public User updateEmail(String id, String email) throws UserNotFoundException {
    Optional<User> user = userDao.updateEmail(new User(id, email));

    if (user.isPresent()) {
      return user.get();
    } else {
      throw new UserNotFoundException(id);
    }
  }

  @Override
  public boolean deleteById(String id) {
    return userDao.deleteById(id) != null;
  }
}