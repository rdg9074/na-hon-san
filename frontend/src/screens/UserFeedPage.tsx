import React, { useState, useEffect, useRef } from "react";
import "./UserFeedPage.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import SetIcon from "@images/SetIcon.svg";
import FeedList from "@components/common/UserFeed/FeedList";
import FollowList from "@components/common/UserFeed/FollowList";
import getCounts from "@utils/getCounts";
import BackImgSkeleton from "@components/common/FeedPage/BackImgSkeleton";
import { Link, useNavigate, useParams } from "react-router-dom";
import { useAppSelector } from "@store/hooks";
import { getProfile } from "@apis/setAccount";

type UserProfile = {
  id: string | null;
  nickname: string | null;
  profileMsg: string | null;
  profileImg: string | null;
  followCount: number;
  followerCount: number;
  tipCount: number;
  dealCount: number;
};

function UserFeedPage() {
  const [tagType, setTagType] = useState<"deal" | "tip">("tip");
  const [followClick, setFollowClick] = useState(false);
  const [followModal, setFollowModal] = useState("");
  const [randomBack, setRandomBack] = useState("");
  const [isLoading, setLoading] = useState(true);
  const [userProfile, setUserProfile] = useState<UserProfile>({
    id: null,
    nickname: null,
    profileMsg: null,
    profileImg: null,
    followCount: 0,
    followerCount: 0,
    tipCount: 0,
    dealCount: 0
  });
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const txtArea = useRef<HTMLTextAreaElement>(null);

  const { nickName } = useParams();
  const navigate = useNavigate();
  useEffect(() => {
    (async () => {
      const res = await getProfile(nickName as string);
      if (res.result === "Fail") {
        navigate("/404");
      }
      setUserProfile(res.data);
    })();
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
  }, [nickName]);

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
              userProfile?.profileImg
                ? `data:image/jpeg;base64,${userProfile?.profileImg}`
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
          <p>{userProfile?.nickname}</p>
          {userProfile.nickname === userInfo?.nickname && (
            <Link to="/account">
              <img src={SetIcon} alt="set" />
            </Link>
          )}
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
            <span>{getCounts(userProfile?.followerCount)}</span>
          </button>
          <button
            className="notoMid"
            type="button"
            onClick={() => {
              follow("Following");
            }}
          >
            팔로잉
            <span>{getCounts(userProfile?.followCount)}</span>
          </button>
        </div>
        {userProfile.nickname !== userInfo?.nickname && (
          <div className="info__btn flex">
            <button type="button">팔로우</button>
            <button type="button">DM</button>
          </div>
        )}
      </div>
      <div className="info__state notoReg flex ">
        <textarea
          className="notoReg"
          value={
            userProfile?.profileMsg ? (userProfile.profileMsg as string) : ""
          }
          ref={txtArea}
          readOnly
        />
      </div>
      <div className="feed">
        <div className="feed-tag flex">
          <button
            onClick={() => {
              setTagType("tip");
            }}
            className={`notoBold ${tagType === "tip" ? "active" : null}`}
            type="button"
          >
            꿀팁보기<span>{`(${getCounts(userProfile.tipCount)})`}</span>
          </button>
          <button
            onClick={() => {
              setTagType("deal");
            }}
            className={`notoBold ${tagType === "tip" ? null : "active"}`}
            type="button"
          >
            꿀딜보기<span>{`(${getCounts(userProfile.dealCount)})`}</span>
          </button>
        </div>
        <FeedList type={tagType} userNickname={nickName as string} />
      </div>
      {followClick ? (
        <FollowList signal={signal} followModal={followModal} />
      ) : null}
    </div>
  );
}

export default UserFeedPage;
