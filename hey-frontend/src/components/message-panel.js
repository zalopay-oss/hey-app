import React from 'react';
import {connect} from "react-redux";
import ChatItem from "./chat-item";


class MessagePanel extends React.Component {
  scrollToBottom = () => {
    this.messagesEnd.scrollIntoView({behavior: "smooth"});
  }

  componentDidUpdate() {
    this.scrollToBottom();
  }

  render() {
    return (
      <div className='chat-content'>
        <div
          ref={(el) => {this.messagesEnd = el}}>
        </div>
        {this.props.messageItems.map((item, index) =>
          <ChatItem key={index} type={item.type} value={item.message} showavatar={item.showavatar}
                    avatar={item.avatar} date={item.createdDate}/>
        )}

      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    messageItems: state.chatReducer.messageItems
  }
}

function mapDispatchToProps(dispatch) {
  return {}
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(MessagePanel);