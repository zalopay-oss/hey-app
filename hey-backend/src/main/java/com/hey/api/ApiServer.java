package com.hey.api;

import com.hey.handler.ProtectedApiHandler;
import com.hey.handler.PublicApiHandler;
import com.hey.handler.WebHandler;
import com.hey.util.PropertiesUtils;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public final class ApiServer {
    private HttpServer httpServer;
    private ProtectedApiHandler protectedApiHandler;
    private PublicApiHandler publicApiHandler;
    private WebHandler webHandler;

    private static final Logger LOGGER = LogManager.getLogger(ApiServer.class);

    private ApiServer() {
    }

    public static ApiServer newInstance() {
        ApiServer apiServer = new ApiServer();
        return apiServer;
    }

    public Future<Void> createHttpServer(Vertx vertx) {
        if (httpServer != null) Future.succeededFuture();
        LOGGER.info("Starting API Server ...");

        Router router = Router.router(vertx);

        router.route("/").handler(routingContext -> {

            HttpServerResponse httpServerResponse = routingContext.response();
            httpServerResponse
                    .putHeader("content-type", "text/html")
                    .end("<h1>Helloworld</h1>");

        });

        router.route().handler(BodyHandler.create());

        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("x-requested-with");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("origin");
        allowedHeaders.add("accept");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("Authorization");

        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        allowedMethods.add(HttpMethod.DELETE);
        allowedMethods.add(HttpMethod.PATCH);
        allowedMethods.add(HttpMethod.PUT);

        router.route("/*").handler(CorsHandler.create("http://localhost:3000").allowedHeaders
                (allowedHeaders).allowedMethods(allowedMethods).allowCredentials(true))
                .handler(BodyHandler.create());

        router.get("/inittestdata").handler(webHandler::initTestData);

        router.post("/signin").handler(webHandler::signIn);
        router.post("/signout").handler(webHandler::signOut);

        router.route("/api/protected/*").handler(protectedApiHandler::handle);

        router.post("/api/public/*").handler(publicApiHandler::handle);


        Future future = Future.future();
        httpServer = vertx
                .createHttpServer()
                .requestHandler(router::accept)
                .exceptionHandler(exHandler -> {
                    LOGGER.error(exHandler.getCause());
                })
                .listen(
                        PropertiesUtils.getInstance().getIntValue("api.port"),
                        ar -> {
                            if (ar.succeeded()) {
                                LOGGER.info("API Server start successfully !");
                                future.complete();
                            } else {
                                LOGGER.error("API Server start fail. Reason: {}", ar.cause().getMessage());
                                future.fail(ar.cause());
                            }
                        }
                );

        return future;
    }

    public void setProtectedApiHandler(ProtectedApiHandler protectedApiHandler) {
        this.protectedApiHandler = protectedApiHandler;
    }

    public void setPublicApiHandler(PublicApiHandler publicApiHandler) {
        this.publicApiHandler = publicApiHandler;
    }

    public void setWebHandler(WebHandler webHandler) {
        this.webHandler = webHandler;
    }

    public ProtectedApiHandler getProtectedApiHandler() {
        return protectedApiHandler;
    }

    public PublicApiHandler getPublicApiHandler() {
        return publicApiHandler;
    }

    public WebHandler getWebHandler() {
        return webHandler;
    }
}
