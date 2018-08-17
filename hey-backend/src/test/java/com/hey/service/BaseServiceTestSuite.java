package com.hey.service;

import com.hey.BaseVerticleTestSuite;
import com.hey.model.*;
import com.hey.util.GenerationUtils;
import com.hey.util.HeyHttpStatusException;
import io.vertx.core.Future;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RunWith(VertxUnitRunner.class)
public class BaseServiceTestSuite extends BaseVerticleTestSuite {

    private static final Logger LOGGER = LogManager.getLogger(BaseServiceTestSuite.class);

    @Test
    public void test_insertUser_withValidUser_shouldSuccess(TestContext context) {
        final Async async = context.async();
        User user = new User();
        user.setUserId(GenerationUtils.generateId());
        user.setUserName("insertTestUser");
        user.setFullName("Insert Test User");
        user.setPassword("123");

        Future<User> future = getApiService().insertUser(user);
        future.compose(userActual -> {
            context.assertEquals("insertTestUser", userActual.getUserName());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_insertChatMessagesAndUpdateChatListAndUpdateUnseenCount_withValidChatMessageAndSessionId_shouldSuccess(TestContext context) {
        final Async async = context.async();
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setSessionId(getSessionIdToTest());
        chatMessage.setUserHash(new UserHash(getUserFullToTest().getUserId(), getUserFullToTest().getFullName()));
        chatMessage.setMessage("test insert chat messages and update chatList and update unseen count");
        chatMessage.setCreatedDate(new Date());

        Future<ChatMessage> future = getApiService().insertChatMessagesAndUpdateChatListAndUpdateUnseenCount(chatMessage);
        future.compose(chatMessageActual -> {
            context.assertEquals("test insert chat messages and update chatList and update unseen count", chatMessageActual.getMessage());
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }

    @Test
    public void test_increaseUnseenCount_withValidSessionIdAndUserIds_shouldSuccess(TestContext context) {
        final Async async = context.async();

        List<String> userIds = new ArrayList<>();
        userIds.add(getUserIdToTest());
        userIds.add(getAnotherUserIdToTest());

        Future<HashMap<String, Long>> future = getApiService().increaseUnseenCount(userIds, getSessionIdToTest());
        future.compose(userIdToUnseenCountMapActual -> {
            context.assertTrue(userIdToUnseenCountMapActual.get(getUserIdToTest()) > 0);
            async.complete();
        }, Future.future().setHandler(handler -> {
            context.fail(handler.cause());
            async.complete();
        }));
    }
}
