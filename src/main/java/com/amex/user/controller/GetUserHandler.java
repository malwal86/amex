package com.amex.user.controller;

import com.amex.user.UserService;
import com.amex.user.error.UserNotFoundException;
import com.amex.user.model.User;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import javax.inject.Inject;

public class GetUserHandler implements Handler<RoutingContext> {

  private UserService userService;

  @Inject
  public GetUserHandler(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(RoutingContext ctx) {
    try {
      User user = userService.findById(ctx.pathParam("id"));
      ctx.response().setStatusCode(HttpResponseStatus.OK.code()).end(Json.encode(user));
    } catch(UserNotFoundException ex) {
      ctx.fail(HttpResponseStatus.NOT_FOUND.code(), ex);
    } catch (Exception ex) {
      ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ex);
    }
  }
}
