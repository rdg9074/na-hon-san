import React from "react";
import "./FollowItem.scss";
import UserDummyIcon from "@images/UserDummy.svg";

function FollowItem() {
  return (
    <div id="follow-item">
      <div className="item-box flex">
        <div className="container flex">
          <img src={UserDummyIcon} alt="dum" />
        </div>
        <p className="notoMid flex">UserName</p>
        <div className="button-box flex">
          <button type="button">Del</button>
        </div>
      </div>
    </div>
  );
}

export default FollowItem;
