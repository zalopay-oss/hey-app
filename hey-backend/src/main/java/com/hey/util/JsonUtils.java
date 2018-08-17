package com.hey.util;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.List;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static String toSuccessJSON(Object message) {
        JsonObject objectResult = new JsonObject(Json.encodePrettily(message));
        JsonObject object = new JsonObject();
        object.put("data", objectResult);
        return Json.encodePrettily(object);
    }

    public static String toSuccessJSON(String message) {
        JsonObject object = new JsonObject();
        object.put("data", message);
        return Json.encodePrettily(object);
    }

    public static String toSuccessJSON(List list) {
        JsonObject object = new JsonObject();
        object.put("data", list);
        return Json.encodePrettily(object);
    }

    public static String toErrorJSON(Object message) {
        JsonObject objectResult = new JsonObject(Json.encodePrettily(message));
        JsonObject object = new JsonObject();
        object.put("error", objectResult);
        return Json.encodePrettily(object);
    }

    public static String toErrorJSON(String message) {
        JsonObject object = new JsonObject();
        object.put("error", message);
        return Json.encodePrettily(object);
    }

    public static String toError500JSON() {
        JsonObject object = new JsonObject();
        object.put("error", "Oops, The handler was unable to complete your request. We will be back soon :(");
        return Json.encodePrettily(object);
    }
}
