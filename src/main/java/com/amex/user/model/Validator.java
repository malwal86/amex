package com.amex.user.model;

import static io.vertx.json.schema.common.dsl.Keywords.*;
import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Bodies;
import io.vertx.ext.web.validation.builder.Parameters;
import io.vertx.ext.web.validation.builder.ValidationHandlerBuilder;
import io.vertx.json.schema.Draft;
import io.vertx.json.schema.JsonSchemaOptions;
import io.vertx.json.schema.SchemaRepository;
import java.util.regex.Pattern;

public abstract class Validator {
  public final static SchemaRepository schemaRepository =
      SchemaRepository.create(new JsonSchemaOptions()
        .setDraft(Draft.DRAFT7).setBaseUri("https://example.com/schemas/"));
  public final static Pattern EMAIL_PATTERN = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$");
  public final static Pattern UUID_PATTERN = Pattern.compile(
      "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$");

  public static ValidationHandler id() {
    return validationHandlerBuilderForId().build();
  }

  public static ValidationHandler createUser() {
    return ValidationHandlerBuilder
        .create(schemaRepository)
        .body(Bodies.json(objectSchema()
          .requiredProperty("email", stringSchema().with(pattern(EMAIL_PATTERN)))
          .requiredProperty("name", stringSchema().with(minLength(1)).with(maxLength(80)))))
        .build();
  }

  public static ValidationHandler updateUser() {
    return validationHandlerBuilderForId()
      .body(Bodies.json(objectSchema().requiredProperty("email", stringSchema().with(pattern(EMAIL_PATTERN)))))
      .build();
  }

  private static ValidationHandlerBuilder validationHandlerBuilderForId() {
    return ValidationHandlerBuilder
      .create(schemaRepository)
      .pathParameter(Parameters.param("id", stringSchema().with(pattern(UUID_PATTERN))));
  }
}
