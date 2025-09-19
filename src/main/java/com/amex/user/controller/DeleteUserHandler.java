package com.amex.user.controller;

import com.amex.user.UserService;
import com.amex.user.error.UserNotFoundException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import javax.inject.Inject;

public class DeleteUserHandler implements Handler<RoutingContext> {

  private UserService userService;

  @Inject
  public DeleteUserHandler(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(RoutingContext ctx) {
    try {
      String id = ctx.pathParam("id");
      if (userService.deleteById(id)) {
        ctx.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
      } else {
        throw new UserNotFoundException(id);
      }
    } catch (Exception ex) {
      ctx.fail(HttpResponseStatus.INTERNAL_SERVER_ERROR.code(), ex);
    }
  }
}
