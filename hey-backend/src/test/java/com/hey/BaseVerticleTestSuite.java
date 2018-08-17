package com.hey;

import com.hey.model.User;
import com.hey.model.UserAuth;
import com.hey.model.UserFull;
import com.hey.repository.DataRepository;
import com.hey.service.APIService;
import com.hey.service.WebService;
import com.hey.verticle.HeyVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class BaseVerticleTestSuite {
    private static final Logger LOGGER = LogManager.getLogger(BaseVerticleTestSuite.class);
    private static final String PROP_FILE_NAME = "system.properties";
    public static final String RESOURCE_PATH = "src/main/resources/";

    private static HeyVerticle heyVerticle;

    private static APIService apiService;

    private static WebService webService;

    private static String userIdToTest;

    private static String anotherUserIdToTest;

    private static String yetAnotherUserIdToTest;

    private static UserFull userFullToTest;

    private static UserFull anotherUserFullToTest;

    private static String sessionIdToTest;

    private static String jwtToTest;

    private static String jwtAuthHeader;

    private static String host = "localhost";

    private static int port = 8080;

    private static DataRepository dataRepository;

    private static HttpClient client;

    private static String env;

    public HeyVerticle getHeyVerticle() {
        return heyVerticle;
    }

    public static APIService getApiService() {
        return apiService;
    }

    public static WebService getWebService() {
        return webService;
    }

    public static DataRepository getDataRepository() {
        return dataRepository;
    }

    public static String getUserIdToTest() {
        return userIdToTest;
    }

    public static String getAnotherUserIdToTest() {
        return anotherUserIdToTest;
    }

    public static String getYetAnotherUserIdToTest() {
        return yetAnotherUserIdToTest;
    }

    public static UserFull getUserFullToTest() {
        return userFullToTest;
    }

    public static UserFull getAnotherUserFullToTest() {
        return anotherUserFullToTest;
    }

    public static HttpClient getClient() {
        return client;
    }

    public static String getSessionIdToTest() {
        return sessionIdToTest;
    }

    public static String getJwtToTest() {
        return jwtToTest;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getJwtAuthHeader() {
        return jwtAuthHeader;
    }

    @BeforeClass
    public static void setUp(TestContext context) {
        if (heyVerticle == null) {
            env = System.getenv("env");
            if (StringUtils.isBlank(env)) {
                LOGGER.error("Missing env");
                System.exit(1);
            }
            initSystemProperty(context);
            initVertx(context);
        }
    }

    private static void initSystemProperty(TestContext context) {
        Properties p = new Properties();
        try {
            p.load(FileUtils.openInputStream(new File(RESOURCE_PATH + env + "." + PROP_FILE_NAME)));
        } catch (IOException e) {
            LOGGER.error("Cannot load System Property");
            System.exit(1);
        }
        for (String name : p.stringPropertyNames()) {
            String value = p.getProperty(name);
            System.setProperty(name, value);
        }
    }

    private static void initData() {

    }

    private static void initVertx(TestContext context) {
        final Async async = context.async();
        Vertx vertx = Vertx.vertx();
        heyVerticle = new HeyVerticle();
        vertx.deployVerticle(
                heyVerticle,
                ar -> {
                    apiService = heyVerticle.getApiServer().getProtectedApiHandler().getApiService();
                    webService = heyVerticle.getApiServer().getWebHandler().getWebService();
                    dataRepository = heyVerticle.getApiServer().getProtectedApiHandler().getDataRepository();
                    client = vertx.createHttpClient();
                    Future<UserAuth> getUserAuthFuture = getApiService().getDataRepository().getUserAuth("vcthanh24");
                    Future<UserAuth> getUserAuthFuture2 = getApiService().getDataRepository().getUserAuth("nthnhung");
                    Future<UserAuth> getUserAuthFuture3 = getApiService().getDataRepository().getUserAuth("utest");
                    CompositeFuture compositeFuture = CompositeFuture.all(getUserAuthFuture, getUserAuthFuture2, getUserAuthFuture3);
                    compositeFuture.setHandler(res -> {
                        userIdToTest = ((UserAuth) compositeFuture.resultAt(0)).getUserId();
                        anotherUserIdToTest = ((UserAuth) compositeFuture.resultAt(1)).getUserId();
                        yetAnotherUserIdToTest = ((UserAuth) compositeFuture.resultAt(2)).getUserId();
                        sessionIdToTest = "test-1234-id";
                        User user = new User();
                        user.setUserName("vcthanh24");
                        user.setPassword("123");
                        getWebService().signIn(Json.encodePrettily(user)).setHandler(res2 -> {
                            jwtToTest = res2.result().getString("jwt");
                            jwtAuthHeader = "Bearer " + jwtToTest;
                            Future<UserFull> getUserFullFuture = getApiService().getDataRepository().getUserFull(userIdToTest);
                            Future<UserFull> getUserFullFuture2 = getApiService().getDataRepository().getUserFull(anotherUserIdToTest);
                            CompositeFuture compositeFuture2 = CompositeFuture.all(getUserFullFuture, getUserFullFuture2);
                            compositeFuture2.setHandler(res3 -> {
                                userFullToTest = ((UserFull) compositeFuture2.resultAt(0));
                                anotherUserFullToTest = ((UserFull) compositeFuture2.resultAt(1));
                                async.complete();
                            });
                        });

                    });

                });
    }
}
