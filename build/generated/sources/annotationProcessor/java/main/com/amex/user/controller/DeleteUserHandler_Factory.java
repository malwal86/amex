package com.amex.user.controller;

import com.amex.user.UserService;
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
public final class DeleteUserHandler_Factory implements Factory<DeleteUserHandler> {
  private final Provider<UserService> userServiceProvider;

  public DeleteUserHandler_Factory(Provider<UserService> userServiceProvider) {
    this.userServiceProvider = userServiceProvider;
  }

  @Override
  public DeleteUserHandler get() {
    return newInstance(userServiceProvider.get());
  }

  public static DeleteUserHandler_Factory create(Provider<UserService> userServiceProvider) {
    return new DeleteUserHandler_Factory(userServiceProvider);
  }

  public static DeleteUserHandler newInstance(UserService userService) {
    return new DeleteUserHandler(userService);
  }
}
