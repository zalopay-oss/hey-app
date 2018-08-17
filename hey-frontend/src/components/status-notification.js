import {Icon, notification} from 'antd';
import Ravatar from 'react-avatar';
import React from "react";

class StatusNotification extends React.Component {
  onlineNotification(name) {
    notification.open({
      description: name + ' is Online',
      className: 'online-notification',
      //duration: 0,
      icon: <Ravatar name={this.processUsernameForAvatar(name)} className="notification-icon"  size="30"/>
  });
  };

  offlineNotification(name) {
    notification.open({
      description: name + ' is Offline',
      className: 'offline-notification',
      //duration: 0,
      icon: <Ravatar name={this.processUsernameForAvatar(name)} className="notification-icon"  size="30"/>
    });
  };

  processUsernameForAvatar(username) {
    var x1 = username.charAt(0);
    var x2 = username.charAt(1);
    return x1 + ' ' + x2;
  }
}

export const statusNotification =  new StatusNotification();

