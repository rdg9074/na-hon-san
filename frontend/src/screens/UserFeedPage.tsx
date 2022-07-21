import React, { useState } from "react";
import "./UserFeedPage.scss";
import ThumDummy from "@images/ThumnailDummy.jpg";
import UserDummyIcon from "@images/UserDummy.svg";
import SetIcon from "@images/SetIcon.svg";
import FeedList from "@components/common/UserFeed/FeedList";
import FollowList from "@components/common/UserFeed/FollowList";
import getCounts from "@utils/getCounts";
import { Link } from "react-router-dom";

function UserFeedPage() {
  const [tagSwitch, setTagSwitch] = useState(true);
  const [followClick, setFollowClick] = useState(false);
  const [followModal, setFollowModal] = useState("");

  const tagChange = (str: string) => {
    if (str === "tip") {
      setTagSwitch(true);
    } else {
      setTagSwitch(false);
    }
  };
  const follow = (state: string) => {
    setFollowModal(state);
    setFollowClick(true);
  };
  const signal = () => {
    setFollowClick(false);
  };

  return (
    <div id="userfeed-page">
      <div className="profile">
        <div className="profile-background flex column">
          <img src={ThumDummy} alt="Thum" className="profile-background__img" />
        </div>
        <div className="profile-user">
          <img src={UserDummyIcon} alt="User" className="profile-user__img" />
        </div>
      </div>
      <div className="info">
        <div className="info__nickname notoBold">
          <p>UserName</p>
          <Link to="/account">
            <img src={SetIcon} alt="set" />
          </Link>
        </div>
        <div className="info__follow flex">
          <button
            className="notoMid"
            type="button"
            onClick={() => {
              follow("Follower");
            }}
          >
            팔로워
            <span>{getCounts(12324)}</span>
          </button>
          <button
            className="notoMid"
            type="button"
            onClick={() => {
              follow("Following");
            }}
          >
            팔로잉
            <span>{getCounts(12324)}</span>
          </button>
        </div>
        <div className="info__btn flex">
          <button type="button">팔로우</button>
          <button type="button">DM</button>
        </div>
        <div className="info__state notoReg">
          <p>
            Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sapiente
            eos mollitia qui dolores sed facilis quidem voluptate
          </p>
        </div>
      </div>
      <div className="feed">
        <div className="feed-tag flex">
          <button
            onClick={() => {
              tagChange("tip");
            }}
            className={`notoBold ${tagSwitch ? "active" : null}`}
            type="button"
          >
            꿀팁보기<span>{`(${getCounts(1234)})`}</span>
          </button>
          <button
            onClick={() => {
              tagChange("deal");
            }}
            className={`notoBold ${tagSwitch ? null : "active"}`}
            type="button"
          >
            꿀딜보기<span>{`(${getCounts(1234)})`}</span>
          </button>
        </div>
        <FeedList />
      </div>
      {followClick ? (
        <FollowList signal={signal} followModal={followModal} />
      ) : null}
    </div>
  );
}

export default UserFeedPage;
