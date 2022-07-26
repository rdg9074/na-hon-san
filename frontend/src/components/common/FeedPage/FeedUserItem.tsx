import React from "react";
import "./FeedUserItem.scss";
import { v4 } from "uuid";
import Thum from "@images/ThumnailDummy.jpg";
import UserDummyIcon from "@images/UserDummy.svg";

function FeedUserItem() {
  const three = [1, 2, 3];
  return (
    <div id="feeduser-item">
      <div className="item">
        <div className="item-title flex">
          <div className="item-title__img flex">
            <img src={UserDummyIcon} alt="icon" />
          </div>
          <div className="item-title__info flex column">
            <p className="notoMid">username</p>
            <button type="button" className="notoReg">
              팔로우
            </button>
          </div>
        </div>
        <div className="item-body flex">
          {three.map(() => {
            return <img src={Thum} alt="thum" key={v4()} />;
          })}
        </div>
      </div>
    </div>
  );
}
export default FeedUserItem;
