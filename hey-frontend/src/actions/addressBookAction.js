import {store} from "../index";
import {loadChatContainer, startNewChatSingle} from "./chatAction";
import {api} from "../api/api";
import {isEmptyString} from "../utils/utils";
import deepcopy from "deepcopy";
import {Icon, notification} from 'antd';
import {statusNotification} from '../components/status-notification'

export const ADDRESSBOOK_FETCHED = 'addressBook.ADDRESSBOOK_FETCHED'
export const ADD_FRIEND_FAIL = 'addressBook.ADD_FRIEND_FAIL'
export const ADD_FRIEND = 'addressBook.ADD_FRIEND'
export const ADD_FRIEND_POPUP_STATE = 'addressBook.ADD_FRIEND_POPUP_STATE'
export const EMPTY = 'addressBook.EMPTY'

export function loadAddressBookList() {
  return function (dispatch) {
    return getAddressBookList().then(result => {
      dispatch(receivedAddressBook(result));
    });
  }
}

export function receivedAddressBook(addressbook) {
  const fetchedAddressBook = addressbook;
  let fetchedNewAddressBookList = store.getState().addressBookReducer.newAddressBookList;
  return {type: ADDRESSBOOK_FETCHED, fetchedAddressBookList: fetchedAddressBook, fetchedNewAddressBookList: fetchedNewAddressBookList};
}


export function handleChangeAddressBook(userId) {

  return function (dispatch) {
    api.post(`/api/protected/sessionidbyuserid`, createGetSessionIdRequest(userId)).then(result => {
      dispatch(receivedSessionId(result, userId));
    });
  }
}

export function receivedSessionId(result, userId) {
  if (result.data.data.sessionId != '-1') {
    store.dispatch(loadChatContainer(result.data.data.sessionId));
  } else {
    store.dispatch(startNewChatSingle(userId));
  }
  return {type: EMPTY};
}

export function addNewFriend(userName) {
  if (isEmptyString(userName)) {
    let error = "Please input username :(";
    return {type: ADD_FRIEND_FAIL, error: error}
  } else {
    return function (dispatch) {
      return api.post(`/api/protected/addfriend`, createAddFriendRequest(userName)).then(result => {
        dispatch(receiveAddFriendResult(result));
      });
    }
  }
}

export function receiveAddFriendResult(result) {
  if (result.data.error) {
    let error = result.data.error.message;
    return {type: ADD_FRIEND_FAIL, error: error}
  } else {
    let newAddressBookList = deepcopy(store.getState().addressBookReducer.newAddressBookList);
    let newFriend = {
      'name': result.data.data.item.name,
      'userId': result.data.data.item.userId,
      'avatar': processUsernameForAvatar(result.data.data.item.name),
      'status': result.data.data.item.status,
      'isOnline': result.data.data.item.online
    };

    newAddressBookList.push(newFriend);
    return {type: ADD_FRIEND, newAddressBookList: newAddressBookList}
  }
}

export function changeStateAddFriendPopup(state) {
  return {type: ADD_FRIEND_POPUP_STATE, popupstate: state}
}

export function changeUserOnlineStatus(userId, status) {
  let fetchedAddressBook = deepcopy(store.getState().addressBookReducer.addressBookList);
  let fetchedNewAddressBook = deepcopy(store.getState().addressBookReducer.newAddressBookList);
  for (var i = 0; i < fetchedAddressBook.length; i++) {
    if (fetchedAddressBook[i].userId == userId) {
      fetchedAddressBook[i].isOnline = status;
    }
  }
  for (var i = 0; i < fetchedNewAddressBook.length; i++) {
    if (fetchedNewAddressBook[i].userId == userId) {
      fetchedNewAddressBook[i].isOnline = status;
    }
  }
  var onlineResults = [];
  var offlineResults = [];
  for (var index = 0; index < fetchedAddressBook.length; ++index) {
    if (fetchedAddressBook[index].isOnline) {
      onlineResults.push(fetchedAddressBook[index]);
    } else {
      offlineResults.push(fetchedAddressBook[index]);
    }
  }

  onlineResults.sort(function(a, b){
    if(a.name < b.name) return -1;
    if(a.name > b.name) return 1;
    return 0;
  });
  offlineResults.sort(function(a, b){
    if(a.name < b.name) return -1;
    if(a.name > b.name) return 1;
    return 0;
  });

  fetchedAddressBook = onlineResults.concat(offlineResults);

  return {type: ADDRESSBOOK_FETCHED, fetchedAddressBookList: fetchedAddressBook, fetchedNewAddressBookList: fetchedNewAddressBook};
}

function processUsernameForAvatar(username) {
  var x1 = username.charAt(0);
  var x2 = username.charAt(1);
  return x1 + ' ' + x2;
}

function getAddressBookList() {
  var promise = new Promise(function (resolve, reject) {
    api.get(`/api/protected/addressbook`)
      .then(res => {
        var items = res.data.data.items;
        var results = [];
        var onlineResults = [];
        var offlineResults = [];
        for (var index = 0; index < items.length; ++index) {
          var addressbookItem = {
            'name': items[index].name,
            'userId': items[index].userId,
            'avatar': processUsernameForAvatar(items[index].name),
            'status': items[index].status,
            'isOnline': items[index].online
          }
          if (items[index].online) {
            onlineResults.push(addressbookItem);
          } else {
            offlineResults.push(addressbookItem);
          }
          onlineResults.sort(function(a, b){
            if(a.name < b.name) return -1;
            if(a.name > b.name) return 1;
            return 0;
          });
          offlineResults.sort(function(a, b){
            if(a.name < b.name) return -1;
            if(a.name > b.name) return 1;
            return 0;
          });

          results = onlineResults.concat(offlineResults);
        }
        resolve(results);
      })
  });
  return promise;
}

function createAddFriendRequest(username) {
  const req = {
    username: username
  }
  return req;
}

function createGetSessionIdRequest(userId) {
  const req = {
    userId: userId
  }
  return req;
}