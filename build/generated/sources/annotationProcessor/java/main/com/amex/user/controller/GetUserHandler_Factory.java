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
public final class GetUserHandler_Factory implements Factory<GetUserHandler> {
  private final Provider<UserService> userServiceProvider;

  public GetUserHandler_Factory(Provider<UserService> userServiceProvider) {
    this.userServiceProvider = userServiceProvider;
  }

  @Override
  public GetUserHandler get() {
    return newInstance(userServiceProvider.get());
  }

  public static GetUserHandler_Factory create(Provider<UserService> userServiceProvider) {
    return new GetUserHandler_Factory(userServiceProvider);
  }

  public static GetUserHandler newInstance(UserService userService) {
    return new GetUserHandler(userService);
  }
}
