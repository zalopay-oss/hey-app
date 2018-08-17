import {CHANGE_STATUS, CHANGE_TAB, REGISTER_SUCCEEDED, USER_PROFILE} from "../actions/userAction";

const initialState = {
  user: {},
  activeTabKey : "1",
  userFullName: "",
  userName: "",
  userStatus: ""
}

export default function reduce(state = initialState, action) {
  switch (action.type) {
    case CHANGE_TAB:
      console.log(action.activeTabKey);
      return {
        ...state,
        activeTabKey: action.activeTabKey.toString()
      };
    case REGISTER_SUCCEEDED:
      return {
        ...state,
        user: action.user,
        activeTabKey: "1"
      };
    case USER_PROFILE:
      return {
        ...state,
        userFullName: action.userFullName,
        userName: action.userName,
        userStatus: action.userStatus
      };
    case CHANGE_STATUS:
      return {
        ...state,
        userStatus: action.userStatus
      }
    default:
      return state;
  }
}
