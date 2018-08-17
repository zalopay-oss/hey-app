import {store} from "../index";
import {api, ws_host} from '../api/api';
import Sockette from 'sockette';
import {getJwtFromStorage, getUserIdFromStorage, isEmptyString} from "../utils/utils";
import deepcopy from "deepcopy";
import {message} from "antd/lib/index";
import {changeUserOnlineStatus} from "./addressBookAction";
import {statusNotification} from "../components/status-notification";

export const EMPTY = 'chatlist.EMPTY'
export const CHATLIST_FETCHED = 'chatlist.CHATLIST_FETCHED'
export const CHATLIST_REFETCHED = 'chatlist.CHATLIST_REFETCHED'
export const MESSAGE_HEADER_FETCHED = 'chatlist.MESSAGE_HEADER_FETCHED'
export const MESSAGE_PANEL_FETCHED = 'chatlist.MESSAGE_PANEL_FETCHED'
export const NEW_MESSAGE_IN_PANEL_FETCHED = 'chatlist.NEW_MESSAGE_IN_PANEL_FETCHED'
export const ADD_NEW_START_CHAT_GROUP = 'chatlist.ADD_NEW_START_CHAT_GROUP'
export const REMOVE_START_CHAT_GROUP = 'chatlist.REMOVE_START_CHAT_GROUP'
export const START_CHAT_GROUP = 'chatlist.START_CHAT_GROUP'
export const START_CHAT_SINGLE = 'chatlist.START_CHAT_SINGLE'
export const ADD_NEW_START_CHAT_GROUP_FAIL = 'chatlist.ADD_NEW_START_CHAT_GROUP_FAIL'
export const USER_SELECTED = 'chatlist.USER_SELECTED'
export const WEBSOCKET_FETCHED = 'chatlist.WEBSOCKET_FETCHED'

export function initialWebSocket() {
  const jwt = getJwtFromStorage();
  const webSocket = new Sockette(ws_host + '?jwt=' + jwt, {
    timeout: 5e3,
    maxAttempts: 10,
    onopen: e => {

    },
    onmessage: e => {
      var data = JSON.parse(e.data);
      switch (data.type) {
        case 'CHAT_ITEMS_RESPONSE':
          store.dispatch(changeMessageItems(data.chatItems, data.sessionId));
          break;
        case 'CHAT_MESSAGE_RESPONSE':
          store.dispatch(receivedNewMessage(data));
          break;
        case 'CHAT_NEW_SESSION_RESPONSE':
          store.dispatch(receivedNewChatSession(data));
          break;
        case 'USER_ONLINE_RESPONSE':
          store.dispatch(receivedUserOnline(data));
          break;
        case 'USER_OFFLINE_RESPONSE':
          store.dispatch(receivedUserOffline(data));
          break;

      }
    },
    onreconnect: e => console.log('Reconnecting...', e),
    onmaximum: e => console.log('Stop Attempting!', e),
    onclose: e => console.log('Closed!', e),
    onerror: e => console.log('Error:', e)
  });
  //ws.close(); // graceful shutdown
  return {type: WEBSOCKET_FETCHED, webSocket: webSocket};
}

export function closeWebSocket() {
  store.getState().chatReducer.webSocket.close();
  return {type: EMPTY};
}

export function loadChatList() {
  return function (dispatch) {
    return getChatList().then(result => {

      dispatch(receivedChatlist(result));
    });
  }
}

export function reloadChatList() {
  return function (dispatch) {
    return getChatList().then(result => {
      dispatch(receivedReloadChatlist(result));
    });
  }
}

export function loadChatContainer(sessionId) {
  store.getState().chatReducer.webSocket.json(createLoadChatContainerRequest(sessionId));
  return {type: EMPTY};
}

export function specialLoadChatContainer(sessionId) {
  let chatList = [];
  let timeout = function () {
    try {
      store.getState().chatReducer.webSocket.json(createLoadChatContainerRequest(sessionId));
      if (store.getState().chatReducer.chatList.length > 0) {
        chatList = deepcopy(store.getState().chatReducer.chatList);
        for (var i = 0; i < chatList.length; i++) {
          if (chatList[i].sessionId == sessionId) {
            chatList[i].unread = 0;
          }
        }
      }
    } catch (e) {
      setTimeout(timeout, 500);
    }
  }
  timeout();

  return {type: EMPTY};
}

export function submitChatMessage(message) {
  let sessionId = store.getState().chatReducer.currentSessionId;
  let waitingGroupUsernames = store.getState().chatReducer.waitingGroupUsernames;
  store.getState().chatReducer.webSocket.json(createChatMessageRequest(sessionId, message, waitingGroupUsernames));
  return {type: EMPTY};
}

export function receivedChatlist(chatlist) {
  const fetchedChatlist = chatlist;
  let header = {};
  if (fetchedChatlist.length > 0) {
    header = {
      'title': fetchedChatlist[0].name,
      'avatar': fetchedChatlist[0].avatar,
      'groupchat': fetchedChatlist[0].groupchat
    };
    store.dispatch(specialLoadChatContainer(fetchedChatlist[0].sessionId));
  }

  return {type: CHATLIST_FETCHED, fetchedChatlist: fetchedChatlist, messageHeader: header};
}

export function receivedReloadChatlist(chatlist) {
  const fetchedChatlist = chatlist;
  return {type: CHATLIST_REFETCHED, fetchedChatlist: fetchedChatlist};
}

export function receivedNewMessage(message) {
  let currentSessionId = store.getState().chatReducer.currentSessionId;
  let userId = getUserIdFromStorage();
  let userSelected = store.getState().chatReducer.userSelected;
  let messageItems = [];
  if (store.getState().chatReducer.messageItems.length > 0) {
    messageItems = deepcopy(store.getState().chatReducer.messageItems);
  }

  let chatList = [];
  if (store.getState().chatReducer.chatList.length > 0) {
    chatList = deepcopy(store.getState().chatReducer.chatList);
  }

  if (currentSessionId == message.sessionId) {
    let type = 1;
    if (message.userId != userId) {
      type = 2;
    }
    let showAvatar = true;
    if (messageItems.length > 0 && messageItems[0].type == type && messageItems[0].userId == message.userId) {
      showAvatar = false;
    }
    let messageItem = {
      'message': message.message,
      'type': type,
      'showavatar': showAvatar,
      'avatar': processUsernameForAvatar(message.name),
      'userId': message.userId,
      'createdDate': new Date(message.createdDate).toLocaleString()
    }
    messageItems.unshift(messageItem);

    //re-arrange chat list
    for (var i = 0; i < chatList.length; i++) {
      if (chatList[i].sessionId == message.sessionId) {
        userSelected = message.sessionId;
        chatList[i].lastMessage = message.message;
        var temp = chatList[i];
        chatList.splice(i, 1);
        chatList.unshift(temp);
        break;
      }
    }

  } else {
    //re-arrange chat list
    for (var i = 0; i < chatList.length; i++) {
      if (chatList[i].sessionId == message.sessionId) {
        chatList[i].lastMessage = message.message;
        chatList[i].unread = chatList[i].unread + 1;
        var temp = chatList[i];
        chatList.splice(i, 1);
        chatList.unshift(temp);
        break;
      }
    }


  }
  return {type: NEW_MESSAGE_IN_PANEL_FETCHED, messageItems: messageItems, chatList: chatList, userSelected: userSelected};
}

export function receivedNewChatSession(message) {
  if (store.getState().chatReducer.currentSessionId == '-1') {

    store.dispatch(loadChatContainer(message.sessionId));
  }
  store.dispatch(reloadChatList());
  store.dispatch(userSelected(message.sessionId));
  return {type: EMPTY}
}

export function changeMessageItems(chatItems, sessionId) {
  const messageItems = getMessageItems(chatItems);
  let chatList = [];
  if (store.getState().chatReducer.chatList.length > 0) {
    chatList = deepcopy(store.getState().chatReducer.chatList);
    for (var i = 0; i < chatList.length; i++) {
      if (chatList[i].sessionId == sessionId) {
        chatList[i].unread = 0;
      }
    }
    store.dispatch(userSelected(sessionId));
  }
  return {type: MESSAGE_PANEL_FETCHED, messageItems: messageItems, currentSessionId: sessionId, chatList: chatList};
}

export function changeMessageHeader(title, avatar, groupchat) {
  const header = {
    'title': title,
    'avatar': avatar,
    'groupchat': groupchat
  };
  return {type: MESSAGE_HEADER_FETCHED, messageHeader: header};
}

export function addNewUserChatGroup(userName) {
  if (isEmptyString(userName)) {
    let error = "Please input username :(";
    return {type: ADD_NEW_START_CHAT_GROUP_FAIL, error: error}
  } else {
    return function (dispatch) {
      return api.post(`/api/protected/usernameexisted`, createCheckUsernameExistedRequest(userName)).then(result => {
        dispatch(receiveNewUserChatGroup(result));
      });
    }
  }
}

export function removeUserChatGroup(userName) {
  let startChatGroupList = deepcopy(store.getState().chatReducer.startChatGroupList);
  let index = startChatGroupList.indexOf(userName);
  if (index > -1) {
    startChatGroupList.splice(index, 1);
  }

  return {type: REMOVE_START_CHAT_GROUP, startChatGroupList: startChatGroupList}

}

export function receiveNewUserChatGroup(result) {
  if (!result.data.data.existed) {
    let error = "Username is not existed :(";
    return {type: ADD_NEW_START_CHAT_GROUP_FAIL, error: error}
  } else {
    let startChatGroupList = deepcopy(store.getState().chatReducer.startChatGroupList);
    startChatGroupList.push(result.data.data.username);
    return {type: ADD_NEW_START_CHAT_GROUP, startChatGroupList: startChatGroupList}
  }
}

export function startNewChatGroup() {
  if (store.getState().chatReducer.startChatGroupList.length > 1) {
    let messageItems = [];
    let waitingGroupUsernames = store.getState().chatReducer.startChatGroupList;
    let currentSessionId = '-1';
    api.post(`/api/protected/waitingchatheader`, createWaitingChatHeaderRequest(waitingGroupUsernames)).then(res => {
      store.dispatch(changeMessageHeader(res.data.data.title, '', true));
    });
    return {
      type: START_CHAT_GROUP,
      messageItems: messageItems,
      waitingGroupUsernames: waitingGroupUsernames,
      currentSessionId: currentSessionId
    }
  } else {
    message.error('Sorry, but a group chat must contains more than 2 people :(');
    return {
      type: EMPTY
    }
  }
}

export function startNewChatSingle(userId) {
  let messageItems = [];
  let waitingGroupUsernames = [userId];
  let currentSessionId = '-1';
  console.log(userId);
  return {
    type: START_CHAT_SINGLE,
    messageItems: messageItems,
    waitingGroupUsernames: waitingGroupUsernames,
    currentSessionId: currentSessionId
  }
}

export function receivedUserOnline(res) {
  var userId = res.userId;
  var userFullname = res.fullName;
  statusNotification.onlineNotification(userFullname);
  store.dispatch(changeUserOnlineStatus(userId, true));
  return {
    type: EMPTY
  }
}

export function receivedUserOffline(res) {
  var userId = res.userId;
  var userFullname = res.fullName;
  statusNotification.offlineNotification(userFullname);
  store.dispatch(changeUserOnlineStatus(userId, false));
  return {
    type: EMPTY
  }
}

export function userSelected(sessionId) {
  console.log(sessionId);
  var userSelectedKeys = [sessionId];
  return {
    type: USER_SELECTED, userSelectedKeys: userSelectedKeys
  }
}

function getMessageItems(chatItems) {
  var userId = getUserIdFromStorage();
  var results = [];
  for (var i = 0; i < chatItems.length; i++) {

    var type = 1;
    if (chatItems[i].userId != userId) {
      type = 2;
    }
    var showAvatar = true;
    if (i > 0 && results[i - 1].type == type && results[i - 1].userId == chatItems[i].userId) {
      showAvatar = false;
    }
    var messageItem = {
      'message': chatItems[i].message,
      'type': type,
      'showavatar': showAvatar,
      'avatar': processUsernameForAvatar(chatItems[i].name),
      'userId': chatItems[i].userId,
      'createdDate': new Date(chatItems[i].createdDate).toLocaleString()
    }
    results.push(messageItem);
  }
  results.reverse();
  return results;

}

function getChatList() {
  var promise = new Promise(function (resolve, reject) {
    api.get(`/api/protected/chatlist`)
      .then(res => {
        var items = res.data.data.items;
        var results = [];
        for (var index = 0; index < items.length; ++index) {
          var chatItem = {
            'name': items[index].name,
            'sessionId': items[index].sessionId,
            'avatar': processUsernameForAvatar(items[index].name),
            'lastMessage': items[index].lastMessage,
            'unread': items[index].unread,
            'groupchat': items[index].groupChat,
            'updatedDate': items[index].updatedDate
          }
          results.push(chatItem);
        }
        results.sort(function (a, b) {
          if (a.updatedDate < b.updatedDate) return 1;
          if (a.updatedDate > b.updatedDate) return -1;
          return 0;
        });
        resolve(results);
      })
  });
  return promise;
}

function processUsernameForAvatar(username) {
  var x1 = username.charAt(0);
  var x2 = username.charAt(1);
  return x1 + ' ' + x2;
}

function createLoadChatContainerRequest(sessionId) {
  const req = {
    type: 'CHAT_ITEMS_REQUEST',
    sessionId: sessionId
  }
  return req;
}

function createChatMessageRequest(sessionId, message, waitingGroupUsernames) {
  const req = {
    type: 'CHAT_MESSAGE_REQUEST',
    sessionId: sessionId,
    message: message,
    usernames: waitingGroupUsernames,
    groupChat: sessionId == '-1'
  }
  return req;
}

function createCheckUsernameExistedRequest(username) {
  const req = {
    username: username
  }
  return req;
}

function createWaitingChatHeaderRequest(usernames) {
  const req = {
    usernames: usernames
  }
  return req;
}