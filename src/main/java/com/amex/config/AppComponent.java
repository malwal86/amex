package com.amex.config;

import com.amex.HttpVerticle;
import dagger.BindsInstance;
import dagger.Component;
import io.vertx.core.Vertx;
import javax.inject.Singleton;

@Singleton
@Component(modules = { ComponentModule.class })
public interface AppComponent {
  HttpVerticle httpVerticle();

  @Component.Factory
  interface Factory {
    AppComponent create(@BindsInstance Vertx vertx);
  }
}
