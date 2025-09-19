package com.amex.config;

import com.amex.HttpVerticle;
import com.amex.user.UserService;
import com.amex.user.UserServiceImpl;
import com.amex.user.UserServiceImpl_Factory;
import com.amex.user.controller.CreateUserHandler;
import com.amex.user.controller.DeleteUserHandler;
import com.amex.user.controller.GetUserHandler;
import com.amex.user.controller.UpdateUserHandler;
import com.amex.user.dao.InMemoryUserDaoImpl_Factory;
import com.amex.user.dao.UserDao;
import com.amex.user.error.UserErrorHandler;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import io.vertx.core.Vertx;
import javax.annotation.processing.Generated;

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
public final class DaggerAppComponent {
  private DaggerAppComponent() {
  }

  public static AppComponent.Factory factory() {
    return new Factory();
  }

  private static final class Factory implements AppComponent.Factory {
    @Override
    public AppComponent create(Vertx vertx) {
      Preconditions.checkNotNull(vertx);
      return new AppComponentImpl(vertx);
    }
  }

  private static final class AppComponentImpl implements AppComponent {
    private final AppComponentImpl appComponentImpl = this;

    private Provider<UserDao> userDaoProvider;

    private Provider<UserServiceImpl> userServiceImplProvider;

    private Provider<UserService> userServiceProvider;

    private AppComponentImpl(Vertx vertxParam) {

      initialize(vertxParam);

    }

    private CreateUserHandler createUserHandler() {
      return new CreateUserHandler(userServiceProvider.get());
    }

    private GetUserHandler getUserHandler() {
      return new GetUserHandler(userServiceProvider.get());
    }

    private UpdateUserHandler updateUserHandler() {
      return new UpdateUserHandler(userServiceProvider.get());
    }

    private DeleteUserHandler deleteUserHandler() {
      return new DeleteUserHandler(userServiceProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final Vertx vertxParam) {
      this.userDaoProvider = DoubleCheck.provider((Provider) InMemoryUserDaoImpl_Factory.create());
      this.userServiceImplProvider = UserServiceImpl_Factory.create(userDaoProvider);
      this.userServiceProvider = DoubleCheck.provider((Provider) userServiceImplProvider);
    }

    @Override
    public HttpVerticle httpVerticle() {
      return new HttpVerticle(new UserErrorHandler(), createUserHandler(), getUserHandler(), updateUserHandler(), deleteUserHandler());
    }
  }
}
