package com.amex;

import static org.junit.jupiter.api.Assertions.*;

import com.amex.config.AppComponent;
import com.amex.user.model.User;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
class HttpVerticleITest {
  private final String HOST = "localhost";
  private final int PORT_NUMBER = 8888;

  private static String deploymentId;
  private List<User> users = new ArrayList<>();

  @BeforeAll
  public static void start(Vertx vertx, VertxTestContext testContext) {
    AppComponent component = com.amex.config.DaggerAppComponent.factory().create(vertx);
    HttpVerticle sut = component.httpVerticle();

    vertx.deployVerticle(sut).onComplete(ar -> {
      if (ar.succeeded()) {

        deploymentId = ar.result(); testContext.completeNow();
      } else { 
        testContext.failNow(ar.cause());
      }
    });
  }

  @AfterAll
  public static void stop(Vertx vertx, VertxTestContext testContext) {
    vertx.undeploy(deploymentId).onComplete(x -> testContext.completeNow());
  }

  @Test
  public void testCreateUserSuccessfully(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";

    // create user
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.CREATED.code(), ar.statusCode());
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }

          // fetch user
          User createdUser = Json.decodeValue(ar.body(), User.class);
          client
              .get(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + createdUser.getId())
              .as(BodyCodec.string())
              .send()
              .onComplete(createCtx.succeeding(ar2 -> createCtx.verify(() -> {
                assertEquals(HttpResponseStatus.OK.code(), ar2.statusCode());
                User fetchedUser = Json.decodeValue(ar2.body(), User.class);
                assertEquals(createdUser, fetchedUser);
                if (createCtx.failed()) {
                  throw createCtx.causeOfFailure();
                }
                createCtx.completeNow();
              })));
        })));
  }

  @Test
  public void testUpdateUserEmailSuccessfully(Vertx vertx, VertxTestContext patchCtx) {
    WebClient client = WebClient.create(vertx);
    String createBody = "{\"name\":\"John Doe2\",\"email\":\"john.doe2@example.com\"}";

    // create user
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(createBody))
        .onComplete(patchCtx.succeeding(ar -> patchCtx.verify(() -> {
          assertEquals(HttpResponseStatus.CREATED.code(), ar.statusCode());
          if (patchCtx.failed()) {
            throw patchCtx.causeOfFailure();
          }

          // update user's email
          User createdUser = Json.decodeValue(ar.body(), User.class);
          String patchBody = "{\"email\":\"john.doe2.updated@example.com\"}";
          client
              .patch(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + createdUser.getId())
              .as(BodyCodec.string())
              .sendBuffer(Buffer.buffer(patchBody))
              .onComplete(patchCtx.succeeding(ar2 -> patchCtx.verify(() -> {
                assertEquals(HttpResponseStatus.OK.code(), ar2.statusCode());
                User patchedUser = Json.decodeValue(ar2.body(), User.class);
                assertEquals("john.doe2.updated@example.com", patchedUser.getEmail());
                if (patchCtx.failed()) {
                  throw patchCtx.causeOfFailure();
                }
                patchCtx.completeNow();
              })));
        })));
  }

  @Test
  public void testDeleteUserSuccessfully(Vertx vertx, VertxTestContext deleteCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"John Doe3\",\"email\":\"john.doe3@example.com\"}";

    // create user
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(deleteCtx.succeeding(ar -> deleteCtx.verify(() -> {
          assertEquals(HttpResponseStatus.CREATED.code(), ar.statusCode());
          if (deleteCtx.failed()) {
            throw deleteCtx.causeOfFailure();
          }

          // delete user
          User createdUser = Json.decodeValue(ar.body(), User.class);
          client
              .delete(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + createdUser.getId())
              .as(BodyCodec.string())
              .send()
              .onComplete(deleteCtx.succeeding(ar2 -> deleteCtx.verify(() -> {
                assertEquals(HttpResponseStatus.NO_CONTENT.code(), ar2.statusCode());
                if (deleteCtx.failed()) {
                  throw deleteCtx.causeOfFailure();
                }

                // try get deleted user results in not found error
                client
                    .get(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + createdUser.getId())
                    .as(BodyCodec.string())
                    .send()
                    .onComplete(deleteCtx.succeeding(ar3 -> deleteCtx.verify(() -> {
                      assertEquals(HttpResponseStatus.NOT_FOUND.code(), ar3.statusCode());
                      String errorMessage = new JsonObject(ar3.body()).getString("errorMessage");
                      assertEquals(HttpResponseStatus.NOT_FOUND.toString(), errorMessage);
                      if (deleteCtx.failed()) {
                        throw deleteCtx.causeOfFailure();
                      }
                      deleteCtx.completeNow();
                    })));
              })));
        })));
  }

  @Test
  public void testCreateUserThatAlreadyExists(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"John Doe4\",\"email\":\"john.doe4@example.com\"}";

    // create user
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.CREATED.code(), ar.statusCode());
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }

          // create user that was previously created
          client
              .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
              .as(BodyCodec.string())
              .sendBuffer(Buffer.buffer(body))
              .onComplete(createCtx.succeeding(ar2 -> createCtx.verify(() -> {
                assertEquals(HttpResponseStatus.CONFLICT.code(), ar2.statusCode());
                String errorMessage = new JsonObject(ar2.body()).getString("errorMessage");
                assertEquals(HttpResponseStatus.CONFLICT.toString(), errorMessage);
                if (createCtx.failed()) {
                  throw createCtx.causeOfFailure();
                }
                createCtx.completeNow();
              })));
        })));
  }

  @Test
  public void testCreateUserWithEmptyName(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"\",\"email\":\"john.doe@example.com\"}";

    // create user results in bad request
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.BAD_REQUEST.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.BAD_REQUEST.toString(), errorMessage);
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }
          createCtx.completeNow();
        })));
  }

  @Test
  public void testCreateUserWithoutName(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"email\":\"john.doe@example.com\"}";

    // create user results in bad request
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.BAD_REQUEST.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.BAD_REQUEST.toString(), errorMessage);
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }
          createCtx.completeNow();
        })));
  }

  @Test
  public void testCreateUserWithInvalidEmailFormat(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"John Doe\",\"email\":\"john.doe.example.com\"}";

    // create user results in bad request
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.BAD_REQUEST.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.BAD_REQUEST.toString(), errorMessage);
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }
          createCtx.completeNow();
        })));
  }

  @Test
  public void testCreateUserWithoutEmail(Vertx vertx, VertxTestContext createCtx) {
    WebClient client = WebClient.create(vertx);
    String body = "{\"name\":\"John Doe\"}";

    // create user results in bad request
    client
        .post(PORT_NUMBER, HOST, HttpVerticle.USERS_URI_V1)
        .putHeader(
            HttpHeaders.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(body))
        .onComplete(createCtx.succeeding(ar -> createCtx.verify(() -> {
          assertEquals(HttpResponseStatus.BAD_REQUEST.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.BAD_REQUEST.toString(), errorMessage);
          if (createCtx.failed()) {
            throw createCtx.causeOfFailure();
          }
          createCtx.completeNow();
        })));
  }

  @Test
  public void testUpdateUnknownUserEmail(Vertx vertx, VertxTestContext patchCtx) {
    WebClient client = WebClient.create(vertx);
    String unknownUserId = UUID.randomUUID().toString();
    
    String patchBody = "{\"email\":\"john.doe.updated@example.com\"}";
    client
        .patch(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + unknownUserId)
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(patchBody))
        .onComplete(patchCtx.succeeding(ar -> patchCtx.verify(() -> {
          assertEquals(HttpResponseStatus.NOT_FOUND.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.NOT_FOUND.toString(), errorMessage);
          if (patchCtx.failed()) {
            throw patchCtx.causeOfFailure();
          }
          patchCtx.completeNow();
        })));
  }

  @Test
  public void testUpdateUserEmailWithInvalidId(Vertx vertx, VertxTestContext patchCtx) {
    WebClient client = WebClient.create(vertx);
    String invalidId = "invalid";

    String patchBody = "{\"email\":\"john.doe.updated@example.com\"}";
    client
        .patch(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + invalidId)
        .as(BodyCodec.string())
        .sendBuffer(Buffer.buffer(patchBody))
        .onComplete(patchCtx.succeeding(ar -> patchCtx.verify(() -> {
          assertEquals(HttpResponseStatus.BAD_REQUEST.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.BAD_REQUEST.toString(), errorMessage);
          if (patchCtx.failed()) {
            throw patchCtx.causeOfFailure();
          }
          patchCtx.completeNow();
        })));
  }

  @Test
  public void testDeleteUnknownUser(Vertx vertx, VertxTestContext deleteCtx) {
    WebClient client = WebClient.create(vertx);
    String unknownUserId = UUID.randomUUID().toString();
    
    client
        .delete(PORT_NUMBER, HOST, HttpVerticle.V1 + HttpVerticle.USERS + "/" + unknownUserId)
        .as(BodyCodec.string())
        .send()
        .onComplete(deleteCtx.succeeding(ar -> deleteCtx.verify(() -> {
          assertEquals(HttpResponseStatus.NOT_FOUND.code(), ar.statusCode());
          String errorMessage = new JsonObject(ar.body()).getString("errorMessage");
          assertEquals(HttpResponseStatus.NOT_FOUND.toString(), errorMessage);
          if (deleteCtx.failed()) {
            throw deleteCtx.causeOfFailure();
          }
          deleteCtx.completeNow();
        })));
  }
}
