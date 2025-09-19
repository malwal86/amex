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
public final class UpdateUserHandler_Factory implements Factory<UpdateUserHandler> {
  private final Provider<UserService> userServiceProvider;

  public UpdateUserHandler_Factory(Provider<UserService> userServiceProvider) {
    this.userServiceProvider = userServiceProvider;
  }

  @Override
  public UpdateUserHandler get() {
    return newInstance(userServiceProvider.get());
  }

  public static UpdateUserHandler_Factory create(Provider<UserService> userServiceProvider) {
    return new UpdateUserHandler_Factory(userServiceProvider);
  }

  public static UpdateUserHandler newInstance(UserService userService) {
    return new UpdateUserHandler(userService);
  }
}
