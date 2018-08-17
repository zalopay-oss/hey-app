package com.hey.handler;

import com.hey.manager.JwtManager;
import com.hey.manager.UserWsChannelManager;
import com.hey.model.*;
import com.hey.repository.DataRepository;
import com.hey.service.APIService;
import com.hey.util.ErrorCode;
import com.hey.util.HeyHttpStatusException;
import com.hey.util.HttpStatus;
import com.hey.util.JsonUtils;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.impl.HttpStatusException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseHandler {

    private static final Logger LOGGER = LogManager.getLogger(BaseHandler.class);

    protected DataRepository dataRepository;

    public void setDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void handleException(Throwable throwable, HttpServerResponse response) {

        if (throwable instanceof HeyHttpStatusException) {
            HeyHttpStatusException e = (HeyHttpStatusException) throwable;
            JsonObject obj = new JsonObject();
            obj.put("code", e.getCode());
            obj.put("message", e.getPayload());
            response.setStatusCode(e.getStatusCode())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonUtils.toErrorJSON(obj));
            return;
        }

        if (throwable instanceof Exception) {
            Exception e = (Exception) throwable;
            LOGGER.error(e);
            e.printStackTrace();
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.code())
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .end(JsonUtils.toError500JSON());
            return;
        }
    }

    public void ping(HttpServerRequest request, HttpServerResponse response) {
        response
                .setStatusCode(HttpStatus.OK.code())
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(JsonUtils.toSuccessJSON("Pong"));
    }
}
