package com.hey.server;

import com.hey.BaseVerticleTestSuite;
import com.hey.model.*;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VertxUnitRunner.class)
public class ApiServerTestSuite extends BaseVerticleTestSuite {

    @Test
    public void test_getChatList(TestContext context) {
        final Async async = context.async();
        HttpClientRequest request = generateRequest("/api/protected/chatlist");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
           async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.end();
    }

    @Test
    public void test_getAddressBook(TestContext context) {
        final Async async = context.async();
        HttpClientRequest request = generateRequest("/api/protected/addressbook");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.end();
    }

    @Test
    public void test_checkUsernameExisted_withExistedUsername_shouldSuccess(TestContext context) {
        final Async async = context.async();
        UsernameExistedRequest obj = new UsernameExistedRequest();
        obj.setUsername("nthnhung");
        HttpClientRequest request = generatePostRequest("/api/protected/usernameexisted");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_checkUsernameExisted_withNotExistedUsername_shouldFailed(TestContext context) {
        final Async async = context.async();
        UsernameExistedRequest obj = new UsernameExistedRequest();
        obj.setUsername("utest");
        HttpClientRequest request = generatePostRequest("/api/protected/usernameexisted");
        request.handler(resp -> {
            context.assertEquals(400, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_getSessionIdByUserId_withExistedUserId_shouldSuccess(TestContext context) {
        final Async async = context.async();
        GetSessionIdRequest obj = new GetSessionIdRequest();
        obj.setUserId(getAnotherUserIdToTest());
        HttpClientRequest request = generatePostRequest("/api/protected/sessionidbyuserid");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_getSessionIdByUserId_withNotExistedUserId_shouldFailed(TestContext context) {
        final Async async = context.async();
        GetSessionIdRequest obj = new GetSessionIdRequest();
        obj.setUserId("not-correct");
        HttpClientRequest request = generatePostRequest("/api/protected/sessionidbyuserid");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            resp.handler(data -> {
                JsonObject obj2 = Json.decodeValue(data, JsonObject.class);
                GetSessionIdResponse dataResponse = Json.decodeValue(obj2.getString("data"), GetSessionIdResponse.class);
                context.assertTrue(StringUtils.isBlank(dataResponse.getSessionId()));
            });
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_addFriend_withNotFriend_shouldSuccess(TestContext context) {
        final Async async = context.async();
        AddFriendRequest obj = new AddFriendRequest();
        obj.setUsername("utest");
        HttpClientRequest request = generatePostRequest("/api/protected/addfriend");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            getApiService().getDataRepository().deleteFriend(getUserIdToTest(), getYetAnotherUserIdToTest()).setHandler(ar -> {
                if (ar.succeeded()) {
                    async.complete();
                }
            });
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();

    }

    @Test
    public void test_addFriend_withFriend_shouldFailed(TestContext context) {
        final Async async = context.async();
        AddFriendRequest obj = new AddFriendRequest();
        obj.setUsername("nthnhung");
        HttpClientRequest request = generatePostRequest("/api/protected/addfriend");
        request.handler(resp -> {
            context.assertEquals(400, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_changeStatus(TestContext context) {
        final Async async = context.async();
        ChangeStatusRequest obj = new ChangeStatusRequest();
        obj.setStatus("Test Status");
        HttpClientRequest request = generatePostRequest("/api/protected/status");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(obj));
        request.end();
    }

    @Test
    public void test_login_withCorrectCredential_shouldSuccess(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserName("vcthanh24");
        user.setPassword("123");
        HttpClientRequest request = generatePostRequest("/signin");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(user));
        request.end();
    }

    @Test
    public void test_login_withWrongCredential_shouldFailed(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserName("vcthanh24");
        user.setPassword("1235");
        HttpClientRequest request = generatePostRequest("/signin");
        request.handler(resp -> {
            context.assertEquals(401, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(user));
        request.end();
    }

    @Test
    public void test_registerUser(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserName(RandomStringUtils.random(10));
        user.setFullName(RandomStringUtils.random(10));
        user.setPassword("123");
        HttpClientRequest request = generatePostRequest("/api/protected/user");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.write(Json.encodePrettily(user));
        request.end();
    }

    @Test
    public void test_getUserProfile(TestContext context) {
        final Async async = context.async();
        HttpClientRequest request = generatePostRequest("/api/protected/user");
        request.handler(resp -> {
            context.assertEquals(200, resp.statusCode());
            async.complete();
        });
        request.exceptionHandler(handler -> {
            context.fail(handler.getCause());
            async.complete();
        });
        request.end();
    }

    private HttpClientRequest generateRequest(String url) {
        HttpClientRequest request = getClient().get(getPort(), getHost(), url);
        request.putHeader("Authorization", getJwtAuthHeader());
        return request;
    }

    private HttpClientRequest generatePostRequest(String url) {
        HttpClientRequest request = getClient().post(getPort(), getHost(), url);
        request.putHeader("Authorization", getJwtAuthHeader());
        request.setChunked(true);
        return request;
    }

}
