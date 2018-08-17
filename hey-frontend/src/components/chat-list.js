import React from 'react';
import {Menu} from 'antd';
import CustomAvatar from '../components/custom-avatar';
import StartChatGroup from "./start-chat-group";
import {connect} from 'react-redux';
import {changeMessageHeader, loadChatContainer, loadChatList, userSelected} from "../actions/chatAction";
import {Scrollbars} from 'react-custom-scrollbars';

class ChatList extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      menuaction: 1
    }
    this.handleChangeChatItem = this.handleChangeChatItem.bind(this);
  }

  componentDidMount() {
    this.props.loadChatList();
  }

  handleChangeChatItem(event) {
    this.props.userSelected(event.key);
    this.props.loadChatContainer(event.key);
    for (var i = 0; i < this.props.chatList.length; i ++) {
      if (this.props.chatList[i].sessionId == event.key) {
        this.props.changeMessageHeader(this.props.chatList[i].name, this.props.chatList[i].avatar, this.props.chatList[i].groupchat);
      }
    }
  }

  render() {

    if (this.props.chatList) {
      return (
        <div className="d-flex flex-column full-height">
          <StartChatGroup/>
          <Scrollbars autoHide autoHideTimeout={500} autoHideDuration={200}>
            <Menu theme="light" mode="inline" className="chat-list"
                  onSelect={this.handleChangeChatItem} selectedKeys={this.props.userSelectedKeys}>
              {this.props.chatList.map((item, index) =>
                <Menu.Item key={item.sessionId}>
                  <div style={{width: 60}}>
                    {item.groupchat ?
                      <CustomAvatar type="group-avatar"/>
                      :
                      <CustomAvatar type="user-avatar" avatar={item.avatar}/>
                    }
                  </div>
                  {item.unread > 0 ?
                    <div className="unread-item" style={{overflow: 'hidden', paddingTop: 5}}>
                      <div className="user-name">{item.name}</div>
                      <div className="history-message">{item.lastMessage}</div>
                    </div>
                    :
                    <div style={{overflow: 'hidden', paddingTop: 5}}>
                      <div className="user-name">{item.name}</div>
                      <div className="history-message">{item.lastMessage}</div>
                    </div>
                  }
                  {item.unread > 0 ?
                    <div className="unread">{item.unread}</div>
                    : ''
                  }
                </Menu.Item>
              )}
            </Menu>
          </Scrollbars>
        </div>)
    } else {
      return (
        "Loading..."
      )
    }
  }
};


function mapStateToProps(state) {
  return {
    chatList: state.chatReducer.chatList,
    userSelectedKeys: state.chatReducer.userSelectedKeys
  }
}

function mapDispatchToProps(dispatch) {
  return {
    loadChatList() {
      dispatch(loadChatList())
    },
    loadChatContainer(sessionId) {
      dispatch(loadChatContainer(sessionId))
    },
    changeMessageHeader(avatar, title, groupchat) {
      dispatch(changeMessageHeader(avatar, title, groupchat))
    },
    userSelected(sessionId) {
      dispatch(userSelected(sessionId))
    }

  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatList);
