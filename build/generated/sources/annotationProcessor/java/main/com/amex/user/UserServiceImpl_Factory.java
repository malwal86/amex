package com.amex.user;

import com.amex.user.dao.UserDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class UserServiceImpl_Factory implements Factory<UserServiceImpl> {
  private final Provider<UserDao> userDaoProvider;

  public UserServiceImpl_Factory(Provider<UserDao> userDaoProvider) {
    this.userDaoProvider = userDaoProvider;
  }

  @Override
  public UserServiceImpl get() {
    return newInstance(userDaoProvider.get());
  }

  public static UserServiceImpl_Factory create(Provider<UserDao> userDaoProvider) {
    return new UserServiceImpl_Factory(userDaoProvider);
  }

  public static UserServiceImpl newInstance(UserDao userDao) {
    return new UserServiceImpl(userDao);
  }
}
