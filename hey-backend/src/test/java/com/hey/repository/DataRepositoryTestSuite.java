package com.hey.repository;

import com.hey.BaseVerticleTestSuite;
import com.hey.model.*;
import com.hey.util.GenerationUtils;
import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(VertxUnitRunner.class)
public class DataRepositoryTestSuite extends BaseVerticleTestSuite {

    @Test
    public void test_getKeysByPattern_withChatListKeyPattern_shouldBeListChatListKey(TestContext context) {
        final Async async = context.async();

        String keyPattern = "chat:list:" + "*" + getUserIdToTest() + "*";

        Future<List<String>> future = getDataRepository().getKeysByPattern(keyPattern);
        future.compose(keysActual -> {
            context.assertTrue(keysActual.size() > 0 && keysActual.get(0).contains(getUserIdToTest()));
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getKeysByPattern_withChatMessageKeyPattern_shouldBeListChatMessageKey(TestContext context) {
        final Async async = context.async();

        String keyPattern = "chat:message:" + getSessionIdToTest() + ":*";

        Future<List<String>> future = getDataRepository().getKeysByPattern(keyPattern);
        future.compose(keysActual -> {
            context.assertTrue(keysActual.size() > 0 && keysActual.get(0).contains(getSessionIdToTest()));
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getKeysByPattern_withFriendListKeyPattern_shouldBeListFriendListKey(TestContext context) {
        final Async async = context.async();

        String keyPattern = "friend:list:*" + getUserIdToTest() + "*";

        Future<List<String>> future = getDataRepository().getKeysByPattern(keyPattern);
        future.compose(keysActual -> {
            context.assertTrue(keysActual.size() > 0 && keysActual.get(0).contains(getUserIdToTest()));
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertUserAuth_withValidUserAuth_shouldSuccess(TestContext context) {
        final Async async = context.async();

        UserAuth userAuth = new UserAuth();
        userAuth.setUserName("testInsertUserAuth");
        userAuth.setUserId(GenerationUtils.generateId());
        userAuth.setHashedPassword(BCrypt.hashpw("123", BCrypt.gensalt()));

        Future<UserAuth> future = getDataRepository().insertUserAuth(userAuth);
        future.compose(userAuthActual -> {
            context.assertEquals("testInsertUserAuth", userAuthActual.getUserName());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserAuth_withExistedUser_shouldBeExistedUserAuth(TestContext context) {
        final Async async = context.async();

        Future<UserAuth> future = getDataRepository().getUserAuth(getUserFullToTest().getUserName());
        future.compose(userAuthActual -> {
            context.assertEquals("vcthanh24", userAuthActual.getUserName());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserAuth_withNotExistUser_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<UserAuth> future = getDataRepository().getUserAuth("testNotExistUserAuth");
        future.compose(userAuthActual -> {
            context.assertNull(userAuthActual);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertUserFull_withValidUserFull_shouldSuccess(TestContext context) {
        final Async async = context.async();

        UserFull userFull = new UserFull();
        userFull.setUserId(GenerationUtils.generateId());
        userFull.setUserName("testInsertUserFull");
        userFull.setFullName("Test insert user full");

        Future<UserFull> future = getDataRepository().insertUserFull(userFull);
        future.compose(userFullActual -> {
            context.assertEquals("testInsertUserFull", userFullActual.getUserName());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserFull_withExistedUser_shouldBeExistedUserFull(TestContext context) {
        final Async async = context.async();

        Future<UserFull> future = getDataRepository().getUserFull(getUserFullToTest().getUserId());
        future.compose(userFullActual -> {
            context.assertEquals("vcthanh24", userFullActual.getUserName());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserFull_withNotExistUser_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<UserFull> future = getDataRepository().getUserFull("testNotExistUserFull");
        future.compose(userFullActual -> {
            context.assertNull(userFullActual);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertUserStatus_withValidUserStatus_shouldSuccess(TestContext context) {
        final Async async = context.async();

        UserStatus userStatus = new UserStatus();
        userStatus.setUserId(GenerationUtils.generateId());
        userStatus.setStatus("Test status");

        Future<UserStatus> future = getDataRepository().insertUserStatus(userStatus);
        future.compose(userStatusActual -> {
            context.assertEquals("Test status", userStatusActual.getStatus());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserStatus_withExistedUser_shouldBeExistedUserStatus(TestContext context) {
        final Async async = context.async();

        Future<UserStatus> future = getDataRepository().getUserStatus(getUserIdToTest());
        future.compose(userStatusActual -> {
            context.assertEquals(getUserIdToTest(), userStatusActual.getUserId());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUserStatus_withNotExistUser_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<UserStatus> future = getDataRepository().getUserStatus("notExistUserStatus");
        future.compose(userStatusActual -> {
            context.assertNull(userStatusActual);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertFriendList_withValidFriendList_shouldSuccess(TestContext context) {
        final Async async = context.async();

        FriendList friendList = new FriendList();
        friendList.setCurrentUserHashes(new UserHash("testInsertFriendListCurrentUserId", "testInsertFriendListCurrentUserName"));
        friendList.setFriendUserHashes(new UserHash("testInsertFriendListFriendUserId", "testInsertFriendListFriendUserName"));

        Future<FriendList> future = getDataRepository().insertFriendList(friendList);
        future.compose(friendListActual -> {
            context.assertEquals("testInsertFriendListCurrentUserId", friendListActual.getCurrentUserHashes().getUserId());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getFriendList_withExistedFriendList_shouldBeFriendList(TestContext context) {
        final Async async = context.async();

        Future<FriendList> future =
                getDataRepository().getFriendList("friend:list:" + getUserIdToTest()+ ":" + getAnotherUserIdToTest(), getUserIdToTest());
        future.compose(friendList -> {
            context.assertEquals(getUserIdToTest(), friendList.getCurrentUserHashes().getUserId());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getFriendList_withNotExistFriendList_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<FriendList> future =
                getDataRepository().getFriendList("friend:list:" + getUserIdToTest()+ ":" + GenerationUtils.generateId(), getUserIdToTest());
        future.compose(friendList -> {
            context.assertNull(friendList);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertChatList_withValidChatList_shouldSuccess(TestContext context) {
        final Async async = context.async();

        ChatList chatList = new ChatList();
        chatList.setSessionId(GenerationUtils.generateId());
        chatList.setUpdatedDate(new Date());
        chatList.setLastMessage("Test last message");
        List<UserHash> userHashes = new ArrayList<>();
        userHashes.add(new UserHash("testInsertChatListUserId1", "testInsertChatListUserName1"));
        userHashes.add(new UserHash("testInsertChatListUserId2", "testInsertChatListUserName2"));
        chatList.setUserHashes(userHashes);

        Future<ChatList> future = getDataRepository().insertChatList(chatList);
        future.compose(chatListActual -> {
            context.assertEquals("testInsertChatListUserId1", chatListActual.getUserHashes().get(0).getUserId());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatList_withExistedChatList_shouldBeChatList(TestContext context) {
        final Async async = context.async();

        Future<ChatList> future =
                getDataRepository().getChatList("chat:list:" + getSessionIdToTest() + ":" + getUserIdToTest() + ":" + getAnotherUserIdToTest());
        future.compose(chatList -> {
            context.assertEquals(getSessionIdToTest(), chatList.getSessionId());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatList_withNotExistChatList_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<ChatList> future =
                getDataRepository().getChatList("chat:list:" + getSessionIdToTest() + ":" + getUserIdToTest() + ":" + GenerationUtils.generateId());
        future.compose(chatList -> {
            context.assertNull(chatList);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertChatMessage_withValidChatList_shouldSuccess(TestContext context) {
        final Async async = context.async();

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setUserHash(new UserHash("testInsertChatMessageId", "testInsertChatMessageFullName"));
        chatMessage.setSessionId(GenerationUtils.generateId());
        chatMessage.setMessage("test insert chat message");
        chatMessage.setCreatedDate(new Date());

        Future<ChatMessage> future = getDataRepository().insertChatMessage(chatMessage);
        future.compose(chatMessageActual -> {
            context.assertEquals("test insert chat message", chatMessageActual.getMessage());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getChatMessage_withExistedChatMessage_shouldBeChatMessage(TestContext context) {
        final Async async = context.async();

        Future<List<String>> getKeysByPatternFuture = getDataRepository().getKeysByPattern("chat:message:" + getSessionIdToTest() + "*");
        getKeysByPatternFuture.compose(keys -> {

            Future<ChatMessage> future = getDataRepository().getChatMessage(keys.get(0));
            future.compose(chatMessage -> {
                context.assertEquals(getSessionIdToTest(), chatMessage.getSessionId());
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
    public void test_getChatMessage_withNotExistChatMessage_shouldBeNull(TestContext context) {
        final Async async = context.async();

        Future<ChatMessage> future = getDataRepository().getChatMessage("chat:message:" + getSessionIdToTest() + ":" + GenerationUtils.generateId());
        future.compose(chatMessage -> {
            context.assertNull(chatMessage);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));

    }

    @Test
    public void test_increaseUnseenCount_withValidUserIdAndSessionId_shouldSuccess(TestContext context) {
        final Async async = context.async();

        Future<Long> future = getDataRepository().increaseUnseenCount(GenerationUtils.generateId(), GenerationUtils.generateId());
        future.compose(unSeenCount -> {
            context.assertTrue(unSeenCount > 0);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_getUnseenCount_withUserHaveUnseenMessage_shouldBePositiveNumber(TestContext context) {
        final Async async = context.async();

        Future<Long> future = getDataRepository().getUnseenCount(getAnotherUserIdToTest(), getSessionIdToTest());
        future.compose(unSeenCount -> {
            context.assertTrue(unSeenCount > 0);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));

    }

    @Test
    public void test_getUnseenCount_withUserNotHaveUnseenMessage_shouldBeZero(TestContext context) {
        final Async async = context.async();

        Future<Long> future = getDataRepository().getUnseenCount(GenerationUtils.generateId(), getSessionIdToTest());
        future.compose(unSeenCount -> {
            context.assertTrue(unSeenCount == 0);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));

    }

    @Test
    public void test_deleteUnseenCount_withValidUserIdAndSessionId_shouldSuccess(TestContext context) {
        final Async async = context.async();

        Future<Long> future = getDataRepository().deleteUnseenCount(GenerationUtils.generateId(), GenerationUtils.generateId());
        checkResultSuccess(context, async, future);
    }

    @Test
    public void test_deleteFriend_withValidUserIdAndFriendId_shouldSuccess(TestContext context) {
        final Async async = context.async();

        Future<Long> future = getDataRepository().deleteFriend(GenerationUtils.generateId(), GenerationUtils.generateId());
        checkResultSuccess(context, async, future);
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
}


