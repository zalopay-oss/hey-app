import React from 'react';
import {Modal, Input, Alert} from 'antd';
import CustomAvatar from '../components/custom-avatar';
import {addNewUserChatGroup, removeUserChatGroup, startNewChatGroup} from "../actions/chatAction";
import {addNewFriend, changeStateAddFriendPopup} from "../actions/addressBookAction";
import {connect} from "react-redux";
import $ from "jquery";

class AddFriend extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      visible: false
    };
    this.handleOk = this.handleOk.bind(this);
    this.handleCancel = this.handleCancel.bind(this);
    this.showModal = this.showModal.bind(this);
  };

  showModal = () => {
    this.props.changeStateAddFriendPopup(true);
  }

  handleOk = (e) => {
    console.log(e);
    var un = $('#add-user-name').val();
    $('#add-user-name').val('');
    this.props.addNewFriend(un);
  }

  handleCancel = (e) => {
    this.props.changeStateAddFriendPopup(false);
  }

  render() {
    return (
      <div>
        <div className="new-action-menu" onClick={this.showModal}>
          <a href="#">
            <CustomAvatar type="new-avatar"/>
            <div className="new-text">Add New Friend</div>
          </a>
        </div>
        <Modal
          width="420px"
          title="Add New Friend"
          visible={this.props.addFriendPopup}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
          okText="Add"
          cancelText="Cancel"
        >
          {this.props.addFriendError ?
            < Alert message={this.props.addFriendErrorMessage} type="error" />
            : ''
          }
          <p className="model-label">Please enter user name:</p>
          <Input  id="add-user-name" className="add-user-name" onPressEnter={this.handleOk}/>
        </Modal>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    addFriendError: state.addressBookReducer.addFriendError,
    addFriendErrorMessage: state.addressBookReducer.addFriendErrorMessage,
    addFriendPopup: state.addressBookReducer.addFriendPopup
  }
}

function mapDispatchToProps(dispatch) {
  return {
    addNewFriend(username) {
      dispatch(addNewFriend(username))
    },
    changeStateAddFriendPopup(state) {
      dispatch(changeStateAddFriendPopup(state))
    }
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddFriend);