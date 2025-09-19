package com.amex.user.error;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.BadRequestException;
import javax.inject.Inject;

public class UserErrorHandler implements Handler<RoutingContext> {
  @Inject
  public UserErrorHandler() {}

  // TODO log error messages and return error messages from Exceptions

  @Override
  public void handle(RoutingContext ctx) {
    Throwable err = ctx.failure();

    if (err instanceof UserNotFoundException) {
      ctx.response()
        .setStatusCode(HttpResponseStatus.NOT_FOUND.code())
        .end(errorAsJson(HttpResponseStatus.NOT_FOUND.toString()));
    } else if (err instanceof BadRequestException) {
      ctx.response()
        .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
        .end(errorAsJson(HttpResponseStatus.BAD_REQUEST.toString()));
    } else if (err instanceof UserAlreadyExistsException) {
      ctx.response()
        .setStatusCode(HttpResponseStatus.CONFLICT.code())
        .end(errorAsJson(HttpResponseStatus.CONFLICT.toString()));
    } else {
      ctx.response()
        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
        .end(errorAsJson(HttpResponseStatus.INTERNAL_SERVER_ERROR.toString()));
    }
  }

  private String errorAsJson(String err) {
    return "{ \"errorMessage\":\"" + err + "\" }";
  }
}
