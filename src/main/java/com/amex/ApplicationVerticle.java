package com.amex;

import com.amex.config.AppComponent;
import com.amex.config.DaggerAppComponent;
import io.vertx.core.AbstractVerticle;

public final class ApplicationVerticle extends AbstractVerticle {

  @Override
  public void start() {
    AppComponent component = DaggerAppComponent.factory().create(vertx);
    vertx.deployVerticle(component.httpVerticle());
  }
}

