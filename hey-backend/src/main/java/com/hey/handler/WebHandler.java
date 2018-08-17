package com.hey.handler;

import com.hey.manager.JwtManager;
import com.hey.model.User;
import com.hey.model.UserAuth;
import com.hey.repository.DataRepository;
import com.hey.service.WebService;
import com.hey.util.ErrorCode;
import com.hey.util.HeyHttpStatusException;
import com.hey.util.HttpStatus;
import com.hey.util.JsonUtils;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Date;

public class WebHandler extends BaseHandler{

    private static final Logger LOGGER = LogManager.getLogger(WebHandler.class);

    private WebService webService;

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public WebService getWebService() {
        return webService;
    }

    public void signIn(RoutingContext routingContext) {
        String requestJson = routingContext.getBodyAsString();
        Future<JsonObject> signInFuture = webService.signIn(requestJson);

        signInFuture.compose(jsonObject -> {

                routingContext.response()
                        .setStatusCode(HttpStatus.OK.code())
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(JsonUtils.toSuccessJSON(jsonObject));

        }, Future.future().setHandler(handler -> {
            handleException(handler.cause(), routingContext.response());
        }));

    }

    public void signOut(RoutingContext routingContext) {
        webService.signOut(routingContext);
    }

    public void initTestData(RoutingContext routingContext) {

        Future<JsonObject> futureInitTestData = webService.initTestData();

        futureInitTestData.compose(jsonObject -> {

            routingContext.response()
                    .setStatusCode(HttpStatus.OK.code())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonUtils.toSuccessJSON(jsonObject));

        }, Future.future().setHandler(handler -> {
            handleException(handler.cause(), routingContext.response());
        }));

    }

}
