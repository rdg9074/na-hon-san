import React from "react";
import "./UserCarouselItem.scss";
import getCounts from "@utils/getCounts";
import UserDummyIcon from "@images/UserDummy.svg";

function UserCarouselItem() {
  return (
    <div id="user-carousel-item">
      <div className="item flex column">
        <div className="item-img flex">
          <img src={UserDummyIcon} alt="asd" />
        </div>
        <div className="item-content flex column">
          <p className="fs-24 notoMid">UserName</p>
          <p className="fs-12 notoReg">Follower : {getCounts(12345)} </p>
          <button className="fs-16 notoReg" type="button">
            팔로우
          </button>
        </div>
      </div>
    </div>
  );
}

export default UserCarouselItem;
