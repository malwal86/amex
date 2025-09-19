package com.amex;

import com.amex.user.controller.CreateUserHandler;
import com.amex.user.controller.DeleteUserHandler;
import com.amex.user.controller.GetUserHandler;
import com.amex.user.controller.UpdateUserHandler;
import com.amex.user.error.UserErrorHandler;
import com.amex.user.model.Validator;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import javax.inject.Inject;

public class HttpVerticle extends AbstractVerticle {
  public static final String PORT = "PORT";
  public static final String DEFAULT_PORT_NUMBER = "8888";
  public static final String V1 = "/v1";
  public static final String USERS = "/users";
  public static final String ID = "/:id";
  public static final String USERS_URI_V1 = V1 + USERS;
  public static final String USERS_URI_WITH_ID_V1 = V1 + USERS + ID;

  private UserErrorHandler errorHandler;
  private CreateUserHandler createUserHandler;
  private GetUserHandler getUserHandler;
  private UpdateUserHandler updateUserHandler;
  private DeleteUserHandler deleteUserHandler;

  @Inject
  public HttpVerticle(
      UserErrorHandler errorHandler,
      CreateUserHandler createUserHandler,
      GetUserHandler getUserHandler,
      UpdateUserHandler updateUserHandler,
      DeleteUserHandler deleteUserHandler) {
    this.errorHandler = errorHandler;
    this.createUserHandler = createUserHandler;
    this.getUserHandler = getUserHandler;
    this.updateUserHandler = updateUserHandler;
    this.deleteUserHandler = deleteUserHandler;
  }

  @Override
  public void start() throws Exception {
    int port = Integer.parseInt(System.getenv().getOrDefault(PORT, DEFAULT_PORT_NUMBER));
    Router router = Router.router(vertx);

    router.route().handler(BodyHandler.create());
    router.post(USERS_URI_V1).handler(Validator.createUser()).blockingHandler(createUserHandler);
    router.get(USERS_URI_WITH_ID_V1).handler(Validator.id()).blockingHandler(getUserHandler);
    router.patch(USERS_URI_WITH_ID_V1).handler(Validator.updateUser()).blockingHandler(updateUserHandler);
    router.delete(USERS_URI_WITH_ID_V1).handler(Validator.id()).blockingHandler(deleteUserHandler);

    router.errorHandler(HttpResponseStatus.BAD_REQUEST.code(), errorHandler);
    router.errorHandler(HttpResponseStatus.CONFLICT.code(), errorHandler);
    router.errorHandler(HttpResponseStatus.NOT_FOUND.code(), errorHandler);
    router.errorHandler(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), errorHandler);

    vertx.createHttpServer()
        .requestHandler(router)
        .listen(port)
        .onSuccess(server -> System.out.println("HTTP server started on port " + server.actualPort()))
        .onFailure(Throwable::printStackTrace);
  }
}
