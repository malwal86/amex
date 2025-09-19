package com.amex;

import com.amex.user.controller.CreateUserHandler;
import com.amex.user.controller.DeleteUserHandler;
import com.amex.user.controller.GetUserHandler;
import com.amex.user.controller.UpdateUserHandler;
import com.amex.user.error.UserErrorHandler;
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
public final class HttpVerticle_Factory implements Factory<HttpVerticle> {
  private final Provider<UserErrorHandler> errorHandlerProvider;

  private final Provider<CreateUserHandler> createUserHandlerProvider;

  private final Provider<GetUserHandler> getUserHandlerProvider;

  private final Provider<UpdateUserHandler> updateUserHandlerProvider;

  private final Provider<DeleteUserHandler> deleteUserHandlerProvider;

  public HttpVerticle_Factory(Provider<UserErrorHandler> errorHandlerProvider,
      Provider<CreateUserHandler> createUserHandlerProvider,
      Provider<GetUserHandler> getUserHandlerProvider,
      Provider<UpdateUserHandler> updateUserHandlerProvider,
      Provider<DeleteUserHandler> deleteUserHandlerProvider) {
    this.errorHandlerProvider = errorHandlerProvider;
    this.createUserHandlerProvider = createUserHandlerProvider;
    this.getUserHandlerProvider = getUserHandlerProvider;
    this.updateUserHandlerProvider = updateUserHandlerProvider;
    this.deleteUserHandlerProvider = deleteUserHandlerProvider;
  }

  @Override
  public HttpVerticle get() {
    return newInstance(errorHandlerProvider.get(), createUserHandlerProvider.get(), getUserHandlerProvider.get(), updateUserHandlerProvider.get(), deleteUserHandlerProvider.get());
  }

  public static HttpVerticle_Factory create(Provider<UserErrorHandler> errorHandlerProvider,
      Provider<CreateUserHandler> createUserHandlerProvider,
      Provider<GetUserHandler> getUserHandlerProvider,
      Provider<UpdateUserHandler> updateUserHandlerProvider,
      Provider<DeleteUserHandler> deleteUserHandlerProvider) {
    return new HttpVerticle_Factory(errorHandlerProvider, createUserHandlerProvider, getUserHandlerProvider, updateUserHandlerProvider, deleteUserHandlerProvider);
  }

  public static HttpVerticle newInstance(UserErrorHandler errorHandler,
      CreateUserHandler createUserHandler, GetUserHandler getUserHandler,
      UpdateUserHandler updateUserHandler, DeleteUserHandler deleteUserHandler) {
    return new HttpVerticle(errorHandler, createUserHandler, getUserHandler, updateUserHandler, deleteUserHandler);
  }
}
