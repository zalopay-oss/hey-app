import React from 'react';
import {Tabs} from 'antd';
import {LoginForm} from '../components/login-form';
import {RegisterForm} from '../components/register-form';
import {connect} from "react-redux";
import {changeTab} from "../actions/userAction";
import {isAuthenticated} from "../utils/utils";
import {Redirect} from "react-router-dom";

const TabPane = Tabs.TabPane;

class Portal extends React.Component {

  handleTabChanged = (activeKey) => {
    this.props.changeTab(activeKey);
  }

  render() {
    if (!isAuthenticated()) {
      return <Redirect to="/" />;
    }
    return (
      <div id="portal-container">
        <div className="logo" ><img src="logo.png"/></div>
        <div id="authen-panel">
          <Tabs activeKey={this.props.activeTabKey} onChange={this.handleTabChanged}>
            <TabPane tab="Login" key="1">
              <LoginForm/>
            </TabPane>
            <TabPane tab="Register" key="2"><RegisterForm/></TabPane>
          </Tabs>
        </div>
      </div>
    );
  }
}

function mapStateToProps(state) {
  return {
    activeTabKey: state.userReducer.activeTabKey
  }
}

function mapDispatchToProps(dispatch) {
  return {
    changeTab(activeKey) {
      dispatch(changeTab(activeKey))
    }
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Portal);