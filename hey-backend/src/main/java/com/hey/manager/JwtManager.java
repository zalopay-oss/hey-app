package com.hey.manager;

import com.hey.Main;
import com.hey.util.PropertiesUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.AsyncMap;
import io.vertx.core.shareddata.SharedData;
import io.vertx.ext.auth.KeyStoreOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.auth.jwt.impl.JWTUser;
import io.vertx.ext.jwt.JWTOptions;

import java.util.Set;

public class JwtManager {
    private SharedData sharedData;
    private JWTAuth authProvider;
    private JWTOptions jwtOptions;
    private static final String JWT_ASYNC_MAP = "jwt-async-map";
    private static final String JWT_BLACKLIST_KEY = "blacklist-key";

    public JwtManager(Vertx vertx) {

        this.sharedData = vertx.sharedData();

        authProvider = JWTAuth.create(vertx, new JWTAuthOptions()
                .setKeyStore(new KeyStoreOptions()
                        .setType(PropertiesUtils.getInstance().getValue("jwt.keystore.type"))
                        .setPassword(PropertiesUtils.getInstance().getValue("jwt.keystore.password"))
                        .setPath(Main.RESOURCE_PATH + PropertiesUtils.getInstance().getValue("jwt.keystore"))));

        jwtOptions = new JWTOptions()
                .setIssuer(PropertiesUtils.getInstance().getValue("jwt.iss"))
                .addAudience(PropertiesUtils.getInstance().getValue("jwt.aud"))
                .setExpiresInSeconds(PropertiesUtils.getInstance().getIntValue("jwt.expire"));
    }

    public void authenticate(JsonObject authInfo, Handler<AsyncResult<User>> resultHandler) {
        String jwt = authInfo.getString("jwt");
        checkForExistingAsynMap(jwt).setHandler(event -> {
            if (event.result()) {
                resultHandler.handle(Future.failedFuture("Token has been blacklist"));
            } else {
                authProvider.authenticate(authInfo, resultHandler);
            }
        });

    }

    public void blacklistToken(String token, String userId, long ttl) {
        putToAsynMap(token, userId, ttl);
    }

    public String generateToken(String userId) {
        JsonObject userObj = new JsonObject()
                .put("userId", userId);
        return authProvider.generateToken(userObj, jwtOptions);
    }

    private void putToAsynMap(String token, String userId, long ttl) {
        sharedData.getAsyncMap(JWT_ASYNC_MAP, event -> {
            AsyncMap<Object, Object> aMap = event.result();
            aMap.put(token, userId, ttl, AsyncResult::mapEmpty);
        });
    }

    private Future<Boolean> checkForExistingAsynMap(String token) {
        Future<Boolean> future = Future.future();
        sharedData.getAsyncMap(JWT_ASYNC_MAP, event -> {
            AsyncMap<Object, Object> aMap = event.result();
            aMap.get(token, event2 -> {
                if (event2.result() != null) {
                    future.complete(true);
                } else {
                    future.complete(false);
                }
            });
        });
        return future;
    }

    public void setSharedData(SharedData sharedData) {
        this.sharedData = sharedData;
    }

    public JWTAuth getAuthProvider() {
        return authProvider;
    }

    public void setAuthProvider(JWTAuth authProvider) {
        this.authProvider = authProvider;
    }
}
