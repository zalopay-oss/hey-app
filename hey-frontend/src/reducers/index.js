import {combineReducers} from "redux";
import chatReducer from "./chatReducer";
import addressBookReducer from "./addressBookReducer";
import userReducer from "./userReducer";

const appReducer = combineReducers({
  chatReducer, addressBookReducer, userReducer
});

const rootReducer = (state, action) => {
  if (action.type === 'USER_LOGOUT') {
    state = undefined
  }
  return appReducer(state, action)
}

export default rootReducer