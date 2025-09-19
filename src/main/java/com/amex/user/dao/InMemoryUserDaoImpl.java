package com.amex.user.dao;

import com.amex.user.model.User;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.inject.Inject;

public class InMemoryUserDaoImpl implements UserDao {
  private final ConcurrentMap<String, User> users = new ConcurrentHashMap<>();
  private final Set<String> emails = ConcurrentHashMap.newKeySet();

  @Inject
  public InMemoryUserDaoImpl() {}

  @Override
  public Optional<User> create(User user) {
    if (!emails.contains(user.getEmail())) {
      users.put(user.getId(), user);
      emails.add(user.getEmail());
      return Optional.of(user);
    }
    return Optional.empty();
  }

  @Override
  public Optional<User> findById(String id) {
    return Optional.ofNullable(users.get(id));
  }

  @Override
  public Optional<User> updateEmail(User newUser) {
    if (users.containsKey(newUser.getId())) {
      User currentUser = users.get(newUser.getId());
      currentUser.setEmail(newUser.getEmail());

      return Optional.of(currentUser);
    } else {
      return Optional.empty();
    }
  }

  @Override
  public User deleteById(String id) {
    if (users.containsKey(id)) {
      User toDelete = users.get(id);
      emails.remove(toDelete.getEmail());
      return users.remove(id);
    }
    return null;
  }
}
