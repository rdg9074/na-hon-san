import React, { useState, useEffect, useRef } from "react";
import "./UserFeedPage.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import SetIcon from "@images/SetIcon.svg";
import FeedList from "@components/common/UserFeed/FeedList";
import FollowList from "@components/common/UserFeed/FollowList";
import getCounts from "@utils/getCounts";
import BackImgSkeleton from "@components/common/FeedPage/BackImgSkeleton";
import { Link } from "react-router-dom";
import { useAppSelector } from "@store/hooks";

function UserFeedPage() {
  const [tagSwitch, setTagSwitch] = useState(true);
  const [followClick, setFollowClick] = useState(false);
  const [followModal, setFollowModal] = useState("");
  const [randomBack, setRandomBack] = useState("");
  const [isLoading, setLoading] = useState(true);
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const txtArea = useRef<HTMLTextAreaElement>(null);

  useEffect(() => {
    if (txtArea.current) {
      txtArea.current.style.height = "1px";
      txtArea.current.style.height = `${12 + txtArea.current.scrollHeight}px`;
    }
    setLoading(true);
    fetch("https://picsum.photos/520/200")
      .then(res => {
        setRandomBack(res.url);
      })
      .then(() => setLoading(false));
  }, []);

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
          {isLoading ? (
            <BackImgSkeleton />
          ) : (
            <img
              src={randomBack}
              alt="Thum"
              className="profile-background__img"
              title="background"
            />
          )}
        </div>
        <div className="profile-user">
          <img
            src={
              userInfo?.profileImg
                ? `data:image/jpeg;base64,${userInfo?.profileImg}`
                : UserDummyIcon
            }
            alt="User"
            className="profile-user__img"
            title="User"
          />
        </div>
      </div>
      <div className="info">
        <div className="info__nickname notoBold">
          <p>{userInfo?.nickname}</p>
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
      </div>
      <div className="info__state notoReg flex ">
        <textarea
          className="notoReg"
          value={userInfo?.profileMsg ? (userInfo.profileMsg as string) : ""}
          ref={txtArea}
          readOnly
        />
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
