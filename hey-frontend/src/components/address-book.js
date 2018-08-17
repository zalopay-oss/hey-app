import React from 'react';
import {Menu} from 'antd';
import CustomAvatar from '../components/custom-avatar';
import AddFriend from "./add-friend";
import {connect} from "react-redux";
import {handleChangeAddressBook, loadAddressBookList} from "../actions/addressBookAction";
import {Scrollbars} from "react-custom-scrollbars";
import {changeMessageHeader} from "../actions/chatAction";

class AddressBook extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      current: [],
      newselect: []
    };
    this.handleCurrentChange = this.handleCurrentChange.bind(this);
    this.handleNewChange = this.handleNewChange.bind(this);
  }

  componentDidMount() {
    this.props.loadAddressBookList();
  }

  handleCurrentChange(event) {
    this.setState({
      ...this.state,
      current: [event.key],
      newselect: []
    });
    this.props.handleChangeAddressBook(this.props.addressBookList[event.key].userId);
    this.props.changeMessageHeader(this.props.addressBookList[event.key].name, this.props.addressBookList[event.key].avatar, false);
  }

  handleNewChange(event) {
    this.setState({
      ...this.state,
      newselect: [event.key],
      current: []
    });
    console.log(event.key);
    this.props.handleChangeAddressBook(this.props.newAddressBookList[event.key].userId);
    this.props.changeMessageHeader(this.props.newAddressBookList[event.key].name, this.props.newAddressBookList[event.key].avatar, false);
  }

  render() {
    return (
      <div className="d-flex flex-column full-height address-book-menu">
        <AddFriend/>
        <Scrollbars autoHide autoHideTimeout={500} autoHideDuration={200}>
          {this.props.newAddressBookList.length > 0 ?
            <div>
            <hr className="hr-sub-menu-title"/>
            < div className = "sub-menu-title new-add" > New Friends ({this.props.newAddressBookList.length})</div>
            <Menu theme="light" mode="inline" defaultSelectedKeys={[]} selectedKeys={this.state.newselect} className="address-book new-address-book" onSelect={this.handleNewChange}>
            {this.props.newAddressBookList.map((item, index) =>
              <Menu.Item key={index}>
                <div style={{width: 60}}>
                  <CustomAvatar type="user-avatar" avatar={item.avatar}/>
                </div>
                {item.isOnline ?
                  <div className="status-point online"/>
                  :
                  <div className="status-point offline"/>
                }
                <div style={{overflow: 'hidden', paddingTop: 5}}>
                  <div className="user-name">{item.name}</div>
                  <div className="history-message">{item.status}</div>
                </div>
              </Menu.Item>
            )}
            </Menu>
            </div>
            :
            ''
          }
          <hr className="hr-sub-menu-title"/>
          <div className="sub-menu-title">Friends ({this.props.addressBookList.length})</div>
          <Menu theme="light" mode="inline" defaultSelectedKeys={[]} selectedKeys={this.state.current}
                className="address-book" onSelect={this.handleCurrentChange}>
            {this.props.addressBookList.map((item, index) =>
              <Menu.Item key={index}>
                <div style={{width: 60}}>
                  <CustomAvatar type="user-avatar" avatar={item.avatar}/>
                </div>
                {item.isOnline ?
                  <div className="status-point online"/>
                  :
                  <div className="status-point offline"/>
                }
                <div style={{overflow: 'hidden', paddingTop: 5}}>
                  <div className="user-name">{item.name}</div>
                  <div className="history-message">{item.status}</div>
                </div>
              </Menu.Item>
            )}
          </Menu>
        </Scrollbars>
      </div>
    )
  }
};

function mapStateToProps(state) {
  return {
    addressBookList: state.addressBookReducer.addressBookList,
    newAddressBookList: state.addressBookReducer.newAddressBookList
  }
}

function mapDispatchToProps(dispatch) {
  return {
    loadAddressBookList() {
      dispatch(loadAddressBookList())
    },
    changeMessageHeader(avatar, title, groupchat) {
      dispatch(changeMessageHeader(avatar, title, groupchat))
    },
    handleChangeAddressBook(userId) {
      dispatch(handleChangeAddressBook(userId))
    }
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AddressBook);
