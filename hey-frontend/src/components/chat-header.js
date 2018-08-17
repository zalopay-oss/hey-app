import React from 'react';
import {connect} from "react-redux";
import CustomAvatar from "./custom-avatar";


class ChatHeader extends React.Component {
  render() {
    return (
      <div className='chat-header'>
        <div style={{width: 50}}>
          {this.props.header.groupchat ?
            <CustomAvatar type="panel-group-avatar"/>
            :
            <CustomAvatar type="panel-avatar" avatar={this.props.header.avatar}/>
          }
        </div>
        <div style={{overflow: 'hidden', paddingTop: 5}}>
          <div className="panel-message">{this.props.header.title}</div>
        </div>

      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    header: state.chatReducer.messageHeader
  }
}

function mapDispatchToProps(dispatch) {
  return {
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ChatHeader);