package com.hey.repository;

import com.hey.model.ChatList;
import com.hey.model.ChatMessage;
import com.hey.model.FriendList;
import com.hey.model.UserAuth;
import com.hey.model.UserFull;
import com.hey.model.UserStatus;
import io.vertx.core.Future;

import java.util.List;

public interface DataRepository {

    public Future<List<String>> getKeysByPattern(String keyPattern);

    public Future<UserAuth> insertUserAuth(UserAuth userAuth);

    public Future<UserAuth> getUserAuth(String userName);

    public Future<UserFull> insertUserFull(UserFull userFull);

    public Future<UserFull> getUserFull(String userId);

    public Future<UserStatus> insertUserStatus(UserStatus userStatus);

    public Future<UserStatus> getUserStatus(String userId);

    public Future<FriendList> insertFriendList(FriendList friendList);

    public Future<FriendList> getFriendList(String friendListKey, String currentUserId);

    public Future<ChatList> insertChatList(ChatList chatList);

    public Future<ChatList> getChatList(String chatListKey);

    public Future<ChatMessage> insertChatMessage(ChatMessage chatMessage);

    public Future<ChatMessage> getChatMessage(String chatMessageKey);

    public Future<Long> increaseUnseenCount(String userId, String sessionId);

    public Future<Long> getUnseenCount(String userId, String sessionId);

    public Future<Long> deleteUnseenCount(String userId, String sessionId);

    Future<Long> deleteFriend(String userId, String friendId);
}