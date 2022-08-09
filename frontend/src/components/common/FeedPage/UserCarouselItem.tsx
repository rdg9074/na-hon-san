import React from "react";
import "./UserCarouselItem.scss";
import getCounts from "@utils/getCounts";
import UserDummyIcon from "@images/UserDummy.svg";

type UserCarouselItemProps = {
  follow_nickname: string;
  cnt: number;
  profileImg: string | null;
};

function UserCarouselItem({
  info: { follow_nickname: nickname, cnt, profileImg }
}: {
  info: UserCarouselItemProps;
}) {
  return (
    <div id="user-carousel-item">
      <div className="item flex column">
        <div className="item-img flex">
          <img
            src={
              profileImg
                ? `data:image/jpeg;base64,${profileImg}`
                : UserDummyIcon
            }
            alt="asd"
          />
        </div>
        <div className="item-content flex column">
          <p className="fs-20 notoMid">{nickname}</p>
          <p className="fs-12 notoReg">Follower : {getCounts(cnt)} </p>
          <button className="fs-16 notoReg" type="button">
            팔로우
          </button>
        </div>
      </div>
    </div>
  );
}

export default UserCarouselItem;
