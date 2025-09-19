package com.amex.user.controller;

import com.amex.user.UserService;
import com.amex.user.error.UserAlreadyExistsException;
import com.amex.user.model.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import javax.inject.Inject;

public class CreateUserHandler implements Handler<RoutingContext> {

  private UserService userService;

  @Inject
  public CreateUserHandler(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(RoutingContext ctx) {
    JsonObject body = ctx.body().asJsonObject();
    String email = body.getString("email");
    String name = body.getString("name");

    try {
      User user = userService.create(email, name);
      ctx.response().setStatusCode(HttpResponseStatus.CREATED.code()).end(Json.encode(user));
    } catch (UserAlreadyExistsException ex) {
      ctx.fail(HttpResponseStatus.CONFLICT.code(), ex);
    } catch (Exception ex) {
      ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ex);
    }
  }
}
