package com.amex.config;

import com.amex.user.UserService;
import com.amex.user.UserServiceImpl;
import com.amex.user.dao.InMemoryUserDaoImpl;
import com.amex.user.dao.UserDao;
import dagger.Binds;
import dagger.Module;
import javax.inject.Singleton;

@Module
public interface ComponentModule {

  @Binds
  @Singleton
  UserService userService(UserServiceImpl impl);

  @Binds
  @Singleton
  UserDao userDao(InMemoryUserDaoImpl impl);
}
