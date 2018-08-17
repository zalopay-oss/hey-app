package com.hey.service;

import com.hey.BaseVerticleTestSuite;
import com.hey.model.*;
import com.hey.util.HeyHttpStatusException;
import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(VertxUnitRunner.class)
public class ApiServiceTestSuite extends BaseVerticleTestSuite {

    private static final Logger LOGGER = LogManager.getLogger(ApiServiceTestSuite.class);

    @Test
    public void test_checkUsernameExisted_withExistedFriendUser_shouldBeExisted(TestContext context) {
        final Async async = context.async();
        UsernameExistedRequest usernameExistedRequest = new UsernameExistedRequest();
        usernameExistedRequest.setUsername("nthnhung");

        UsernameExistedResponse usernameExistedResponseExpected = new UsernameExistedResponse();
        usernameExistedResponseExpected.setExisted(true);
        usernameExistedResponseExpected.setUsername("nthnhung");

        Future<UsernameExistedResponse> future = getApiService().checkUsernameExisted(usernameExistedRequest, getUserIdToTest());
        future.compose(usernameExistedResponseActual -> {
            context.assertEquals(usernameExistedResponseExpected.getUsername(), usernameExistedResponseActual.getUsername());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_checkUsernameExisted_withNotExistedFriendUser_shouldError(TestContext context) {
        final Async async = context.async();
        UsernameExistedRequest usernameExistedRequest = new UsernameExistedRequest();
        usernameExistedRequest.setUsername("nthnhung2");


        Future<UsernameExistedResponse> future = getApiService().checkUsernameExisted(usernameExistedRequest, getUserIdToTest());
        checkResultValidateFailed(context, async, future);
    }

    @Test
    public void test_addFriend_withNotExistedFriend_shouldsuccess(TestContext context) {
        final Async async = context.async();
        AddFriendRequest request = new AddFriendRequest();
        request.setUsername("utest");
        Future<AddFriendResponse> future = getApiService().addFriend(request, getUserIdToTest());
        future.compose(response -> {
            String name = response.getItem().getName();
            getApiService().getDataRepository().deleteFriend(getUserIdToTest(), getYetAnotherUserIdToTest()).setHandler(ar -> {
                if (ar.succeeded()) {
                    context.assertTrue("Unit Test".equals(name));
                    async.complete();
                }
            });
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            handler.cause().printStackTrace();
            async.complete();
        }));
    }

    @Test
    public void test_addFriend_withExistedFriend_shouldFailed(TestContext context) {
        final Async async = context.async();
        AddFriendRequest addFriendRequest = new AddFriendRequest();
        addFriendRequest.setUsername("nthnhung");

        Future<AddFriendResponse> future = getApiService().addFriend(addFriendRequest, getUserIdToTest());
        checkResultValidateFailed(context, async, future);
    }

    @Test
    public void test_changeStatus_withText_shouldSuccess(TestContext context) {
        final Async async = context.async();
        ChangeStatusRequest request = new ChangeStatusRequest();
        request.setStatus("Test Status");

        Future future = getApiService().changeStatus(request, getUserIdToTest());
        checkResultSuccess(context, async, future);
    }

    @Test
    public void test_getUserAuths_withListUsername_shouldReturnListAuth(TestContext context) {
        final Async async = context.async();
        List<String> requests = new ArrayList<>();
        requests.add("vcthanh24");
        requests.add("nthnhung");
        requests.add("lvhung");

        Future<List<UserAuth>> future = getApiService().getUserAuths(requests);
        future.compose(response -> {
            context.assertTrue(response.get(0).getUserId() != null);
            context.assertTrue(response.get(1).getUserId() != null);
            context.assertTrue(response.get(2).getUserId() != null);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserFulls_withText_shouldReturnListFull(TestContext context) {
        final Async async = context.async();
        List<String> requests = new ArrayList<>();
        requests.add("vcthanh24");
        requests.add("nthnhung");
        requests.add("lvhung");

        Future<List<UserAuth>> future = getApiService().getUserAuths(requests);
        future.compose(response -> {
            List<String> request2s = new ArrayList<>();
            request2s.add(response.get(0).getUserId());
            request2s.add(response.get(1).getUserId());
            request2s.add(response.get(2).getUserId());
            Future<List<UserFull>> future2 = getApiService().getUserFulls(request2s);
            future2.compose(response2 -> {
                context.assertTrue(response2.get(0).getFullName() != null);
                context.assertTrue(response2.get(1).getFullName() != null);
                context.assertTrue(response2.get(2).getFullName() != null);
                async.complete();
            }, Future.future().setHandler(handler -> {
                context.fail(handler.cause());
                async.complete();
            }));
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getFriendLists_withUserId_shouldReturnListFriend(TestContext context) {
        final Async async = context.async();
        Future future = getApiService().getFriendLists(getUserIdToTest());
        checkResultSuccess(context, async, future);
    }

    @Test
    public void test_getFriendLists_withEmptyUserId_shouldFailed(TestContext context) {
        final Async async = context.async();
        Future future = getApiService().getFriendLists("");
        checkResultValidateFailed(context, async, future);
    }

    @Test
    public void test_isFriend_withFriendUserId_shouldTrue(TestContext context) {
        final Async async = context.async();
        Future<Boolean> future = getApiService().isFriend(getAnotherUserIdToTest(), getUserIdToTest());
        future.compose(response -> {
            context.assertTrue(response);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));

    }

    @Test
    public void test_isFriend_withNotFriendUserId_shouldFalse(TestContext context) {
        final Async async = context.async();
        Future<Boolean> future = getApiService().isFriend(getYetAnotherUserIdToTest(), getUserIdToTest());
        future.compose(response -> {
            context.assertFalse(response);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatListBySessionId_withSessionId_shouldReturnChatList(TestContext context) {
        final Async async = context.async();

        Future<ChatList> future = getApiService().getChatListBySessionId(getSessionIdToTest());
        future.compose(response -> {
            context.assertTrue(response.getUserHashes().size() == 2);
            context.assertEquals(response.getSessionId(), getSessionIdToTest());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatLists_withUserId_shouldReturnListChatList(TestContext context) {
        final Async async = context.async();

        Future<List<ChatList>> future = getApiService().getChatLists(getUserIdToTest());
        future.compose(response -> {
            context.assertTrue(!response.isEmpty());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatMessages_withSessionId_shouldReturnMessages(TestContext context) {
        final Async async = context.async();

        Future<List<ChatMessage>> future = getApiService().getChatMessages(getSessionIdToTest());
        future.compose(response -> {
            context.assertTrue(!response.isEmpty());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserProfile_withUserId_shouldReturnUserProfile(TestContext context) {
        final Async async = context.async();

        Future<UserProfileResponse> future = getApiService().getUserProfile(getUserIdToTest());
        future.compose(response -> {
            context.assertEquals(response.getUserFullName(), "Vo Cong Thanh");
            context.assertEquals(response.getUserName(), "vcthanh24");
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_registerUser_withCorrectInfo_shouldSuccess(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserName(RandomStringUtils.random(10));
        user.setFullName(RandomStringUtils.random(10));
        user.setPassword("123");
        Future<User> future = getApiService().registerUser(Json.encodePrettily(user));
        future.compose(response -> {
            context.assertTrue(StringUtils.isNotBlank(response.getUserId()));
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_registerUser_withWrongInfo_shouldFailed(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserName(RandomStringUtils.random(10));
        user.setFullName("");
        user.setPassword("");
        Future<User> future = getApiService().registerUser(Json.encodePrettily(user));
        checkResultValidateFailed(context, async, future);
    }

    @Test
    public void test_methodName_withCertainState_shouldDoSomething(TestContext context) {
        context.assertTrue(true);
    }

    private void checkResultSuccess(TestContext context, Async async, Future future) {
        future.compose(response -> {
            context.assertTrue(true);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    private void checkResultValidateFailed(TestContext context, Async async, Future future) {
        try {
            future.compose(response -> {
                context.fail("Should not true");
                async.complete();
            }, Future.future().setHandler(handler -> {
                context.assertTrue(handler.cause() instanceof HeyHttpStatusException);
                async.complete();
            }));

        } catch (Exception e) {
            context.assertTrue(e instanceof HeyHttpStatusException);
            async.complete();
        }
    }
}
