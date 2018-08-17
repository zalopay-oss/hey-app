package com.hey.handler;

import com.hey.manager.UserWsChannelManager;
import com.hey.model.*;
import com.hey.service.APIService;
import com.hey.util.GenerationUtils;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WsHandler extends BaseHandler{

    private UserWsChannelManager userWsChannelManager;
    private APIService apiService;

    public void setApiService(APIService apiService) {
        this.apiService = apiService;
    }

    public void setUserWsChannelManager(UserWsChannelManager userWsChannelManager) {
        this.userWsChannelManager = userWsChannelManager;
    }

    public void handleChatContainerRequest(ChatContainerRequest request, String channelId, String userId) {
        String sessionId = request.getSessionId();

        Future<List<ChatItem>> futureChatItems = getChatItems(sessionId);

        futureChatItems.compose(chatItems -> {

            Future<Long> deleteUnseenCountFuture = dataRepository.deleteUnseenCount(userId, sessionId);

            deleteUnseenCountFuture.compose(deleteUnseenCountFutureRes -> {

                ChatContainerResponse response = new ChatContainerResponse();
                response.setChatGroup(false);
                response.setSessionId(sessionId);
                response.setChatItems(chatItems);
                response.setType(IWsMessage.TYPE_CHAT_ITEM_RESPONSE);
                userWsChannelManager.selfSendMessage(response, channelId);

            }, Future.future().setHandler(handler -> {
                throw new RuntimeException(handler.cause());
            }));

        }, Future.future().setHandler(handler -> {
            throw new RuntimeException(handler.cause());
        }));

    }

    public void handleChatMessageRequest(ChatMessageRequest request, String channelId, String userId) {

        if("-1".equals(request.getSessionId())){
            if(request.getUsernames().size() == 1){
                insertChatMessageBetweenTwoOnNewChatSessionId(request, channelId, userId);

            }else{
                insertChatMessageGroupOnNewChatSessionId(request, channelId, userId);
            }
        }else{

            insertChatMessageOnExistedChatSessionId(request, channelId, userId);
        }
    }

    private void insertChatMessageOnNewChatSessionId(ChatMessageRequest request, String channelId, List<String> userIds){

        Future<List<UserFull>> getUserFullsFuture = apiService.getUserFulls(userIds);

        getUserFullsFuture.compose(userFulls -> {

            List<UserHash> userHashes = new ArrayList<>();
            for(UserFull userFull : userFulls){
                userHashes.add(new UserHash(userFull.getUserId(), userFull.getFullName()));
            }
            String sessionId = GenerationUtils.generateId();

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserHash(userHashes.get(0));
            chatMessage.setSessionId(sessionId);
            chatMessage.setMessage(request.getMessage());
            chatMessage.setCreatedDate(new Date());

            ChatList chatList = new ChatList();
            chatList.setSessionId(sessionId);
            chatList.setUpdatedDate(new Date());
            chatList.setUserHashes(userHashes);
            chatList.setLastMessage(chatMessage.getMessage());
            chatList.setUpdatedDate(chatMessage.getCreatedDate());

            Future<ChatList> insertChatListFuture = dataRepository.insertChatList(chatList);

            Future<ChatMessage> insertChatMessageFuture = dataRepository.insertChatMessage(chatMessage);

            List<String> userFriendIds = userIds.subList(1, userIds.size());
            Future<HashMap<String, Long>> increaseUnseenCountFuture = apiService.increaseUnseenCount(userFriendIds, chatList.getSessionId());

            CompositeFuture cp = CompositeFuture.all(insertChatMessageFuture, insertChatListFuture, increaseUnseenCountFuture);
            cp.setHandler(ar -> {
                if (ar.succeeded()) {

                    NewChatSessionResponse newChatSessionResponse = new NewChatSessionResponse();
                    newChatSessionResponse.setType(IWsMessage.TYPE_CHAT_NEW_SESSION_RESPONSE);
                    newChatSessionResponse.setSessionId(chatMessage.getSessionId());
                    //userWsChannelManager.selfSendMessage(newChatSessionResponse, channelId);
                    for(UserHash userhash: chatList.getUserHashes()){
                        userWsChannelManager.sendMessage(newChatSessionResponse, userhash.getUserId());
                    }

                } else {
                    throw new RuntimeException(ar.cause());
                }
            });

        }, Future.future().setHandler(handler -> {
            throw new RuntimeException(handler.cause());
        }));

    }

    private void insertChatMessageBetweenTwoOnNewChatSessionId(ChatMessageRequest request, String channelId, String userId){

        List<String> userIds = new ArrayList<>();
        userIds.add(userId);
        userIds.add(request.getUsernames().get(0));

        insertChatMessageOnNewChatSessionId(request, channelId, userIds);

    }

    private void insertChatMessageGroupOnNewChatSessionId(ChatMessageRequest request, String channelId, String userId){


        Future<List<UserAuth>> getUserAuthsFuture = apiService.getUserAuths(request.getUsernames());

        getUserAuthsFuture.compose(userAuths -> {
            List<String> userIds = new ArrayList<>();
            userIds.add(userId);
            for(UserAuth userAuth : userAuths){
                userIds.add(userAuth.getUserId());
            }

            insertChatMessageOnNewChatSessionId(request, channelId, userIds);

        }, Future.future().setHandler(handler -> {
            throw new RuntimeException(handler.cause());
        }));

    }

    private void insertChatMessageOnExistedChatSessionId(ChatMessageRequest request, String channelId, String userId){

        Future<UserFull> getUserFullFuture = dataRepository.getUserFull(userId);
        long startTime = System.currentTimeMillis();
        getUserFullFuture.compose(userFull -> {
            long elapsed = System.currentTimeMillis() - startTime;
            //System.out.println("Handler: " + elapsed);
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserHash(new UserHash(userFull.getUserId(), userFull.getFullName()));
            chatMessage.setSessionId(request.getSessionId());
            chatMessage.setMessage(request.getMessage());
            chatMessage.setCreatedDate(new Date());

            Future<ChatMessage> insertChatMessagesAndUpdateChatListAndUpdateUnseenCountFuture =
                    apiService.insertChatMessagesAndUpdateChatListAndUpdateUnseenCount(chatMessage);

            Future<ChatList> getChatListBySessionIdFuture = apiService.getChatListBySessionId(chatMessage.getSessionId());

            CompositeFuture cp = CompositeFuture.all(insertChatMessagesAndUpdateChatListAndUpdateUnseenCountFuture, getChatListBySessionIdFuture);
            cp.setHandler(ar -> {
                if (ar.succeeded()) {

                    ChatList chatList = cp.resultAt(1);

                    ChatMessageResponse response = new ChatMessageResponse();
                    response.setType(IWsMessage.TYPE_CHAT_MESSAGE_RESPONSE);
                    response.setCreatedDate(chatMessage.getCreatedDate());
                    response.setName(userFull.getFullName());
                    response.setMessage(chatMessage.getMessage());
                    response.setSessionId(chatMessage.getSessionId());
                    response.setUserId(chatMessage.getUserHash().getUserId());
                    //userWsChannelManager.selfSendMessage(response, channelId);
                    for(UserHash userhash: chatList.getUserHashes()){
                        userWsChannelManager.sendMessage(response, userhash.getUserId());
                    }

                } else {
                    throw new RuntimeException(ar.cause());
                }
            });

        } , Future.future().setHandler(handler -> {
            throw new RuntimeException(handler.cause());
        }));

    }

    private Future<List<ChatItem>> getChatItems(String sessionId) {
        Future<List<ChatItem>> future = Future.future();
        List<ChatItem> chatItems = new ArrayList<>();

        Future<List<ChatMessage>> getChatMessagesFuture = apiService.getChatMessages(sessionId);

        getChatMessagesFuture.compose(chatMessages -> {

            for(ChatMessage chatMessage : chatMessages){
                ChatItem chatItem = new ChatItem();
                chatItem.setUserId(chatMessage.getUserHash().getUserId());
                chatItem.setName(chatMessage.getUserHash().getFullName());
                chatItem.setMessage(chatMessage.getMessage());
                chatItem.setCreatedDate(chatMessage.getCreatedDate());

                chatItems.add(chatItem);
            }

            future.complete(chatItems);


        } , Future.future().setHandler(handler -> {
            future.fail(handler.cause());
        }));

        return future;
    }

    public Future<List<FriendList>> getFriendLists(String userId) {
        return apiService.getFriendLists(userId);
    }

    public Future<UserFull> getUserFull(String userId) {
        return dataRepository.getUserFull(userId);
    }
}
