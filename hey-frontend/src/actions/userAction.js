import {api} from '../api/api';
import {message} from 'antd';
import {receiveNewUserChatGroup} from "./chatAction";

export const CHANGE_TAB = 'portal.CHANGE_TAB'
export const REGISTER_SUCCEEDED = 'user.REGISTER_SUCCEEDED'
export const REGISTER_FAILED = 'user.REGISTER_FAILED'
export const USER_PROFILE = 'user.USER_PROFILE'
export const CHANGE_STATUS = 'user.CHANGE_STATUS'

export function changeTab(activeTabKey) {
  return {type: CHANGE_TAB, activeTabKey: activeTabKey};
}

export function register(user) {

  return function (dispatch) {
    return callRegisterApi(user).then(result => {
      dispatch(registerSucceeded(result.data));
    });
  }
}

export function logout() {

  return {type: 'USER_LOGOUT'};
}

function callRegisterApi(user) {

  var promise = new Promise(function (resolve, reject) {
    api.post(`/api/public/user`, user)
      .then(res => {
        resolve(res);
      })
  });
  return promise;

}

export function registerSucceeded(user) {
  message.success('Register successfully. You can proceed to login with your account :)');
  return {type: REGISTER_SUCCEEDED, user: user};
}

export function receivedUserProfile(result) {
  var status = "You are online";
  if (result.data.data.status != '') {
    status = result.data.data.status;
  }
  return {
    type: USER_PROFILE,
    userName: result.data.data.userName,
    userFullName: result.data.data.userFullName,
    userStatus: status
  };
}

export function getProfile() {
  return function (dispatch) {
    api.get(`/api/protected/user`)
      .then(res => {
        dispatch(receivedUserProfile(res));
      })
  };
}


export function changeUserStatus(status) {
  let userStatus = 'You are online';
  if (status !='') {
    userStatus = status;
  }
  api.post(`/api/protected/status`, createChangeStatusRequest(status));
  return {type: CHANGE_STATUS, userStatus: userStatus};
}


function createChangeStatusRequest(status) {
  const req = {
    status: status
  }
  return req;
}