import React from 'react';

class SmallAvatar extends React.Component {
  render() {

      if (this.props.show)
        return ( <div className="small-avatar"/>)
    else
        return (<div className="mock-small-avatar"/>)
  }
}

export default SmallAvatar;