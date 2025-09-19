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
public final class CreateUserHandler_Factory implements Factory<CreateUserHandler> {
  private final Provider<UserService> userServiceProvider;

  public CreateUserHandler_Factory(Provider<UserService> userServiceProvider) {
    this.userServiceProvider = userServiceProvider;
  }

  @Override
  public CreateUserHandler get() {
    return newInstance(userServiceProvider.get());
  }

  public static CreateUserHandler_Factory create(Provider<UserService> userServiceProvider) {
    return new CreateUserHandler_Factory(userServiceProvider);
  }

  public static CreateUserHandler newInstance(UserService userService) {
    return new CreateUserHandler(userService);
  }
}
