import React from 'react';
import CustomAvatar from "./custom-avatar";
import {Popover} from "antd";
import {SlideDown} from "react-slidedown";

class ChatItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showDate: false
    };
    this.handleItemClick = this.handleItemClick.bind(this);
  };

  handleItemClick(e) {
    let newState = this.state.showDate;
    if (newState) {
      newState = false
    } else {
      newState = true;
    }
    this.setState({
      showDate: newState
    })
  }

  render() {
    var cssClass = this.props.type == 1 ? 'chat-item-owner' : 'chat-item-other';
    var cssContentClass = this.props.type == 1 ? 'chat-item-content-owner' : 'chat-item-content-other';
    return (
      <div onClick={this.handleItemClick} className={'chat-item chat-item-outer ' + cssClass}>
        <div className={'chat-item ' + cssClass}>
          <CustomAvatar type="chat-avatar" avatar={this.props.avatar} show={this.props.showavatar}/>
          <div className={'chat-item-content ' + cssContentClass}>{this.props.value}</div>
        </div>
        {this.state.showDate ?
          <SlideDown>
          <div className={'chat-item-date'}>{this.props.date}</div>
          </SlideDown>
          : ''}
      </div>
    )
  }
};

export default ChatItem;