import React, { useState, useEffect, useRef, useMemo } from "react";
import "./UserFeedPage.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import SetIcon from "@images/SetIcon.svg";
import FeedList from "@components/UserFeed/FeedList";
import FollowList from "@components/UserFeed/FollowList";
import getCounts from "@utils/getCounts";
import BackImgSkeleton from "@components/FeedPage/BackImgSkeleton";
import { Link, useNavigate, useParams } from "react-router-dom";
import { addFollow, delFollow } from "@apis/userFeed";
import { useAppSelector } from "@store/hooks";
import { getProfile } from "@apis/setAccount";
import images from "@images/bc/imgIndex";

type UserProfile = {
  id: string | null;
  nickname: string;
  profileMsg: string | null;
  profileImg: string | null;
  followCount: number;
  followerCount: number;
  tipCount: number;
  dealCount: number;
  social: string;
  isFollow: boolean;
  followOpen: boolean;
  followerOpen: boolean;
};

function UserFeedPage() {
  const [tagType, setTagType] = useState<"deal" | "tip">("tip");
  const [followClick, setFollowClick] = useState(false);
  const [followModal, setFollowModal] = useState("");
  const [randomBack, setRandomBack] = useState("");
  const [isLoading, setLoading] = useState(true);
  const [isChanged, setIsChanged] = useState(false);
  const [wait, setWait] = useState(false);
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const [userProfile, setUserProfile] = useState<UserProfile>({
    id: null,
    nickname: "",
    profileMsg: null,
    profileImg: null,
    followCount: 0,
    followerCount: 0,
    tipCount: 0,
    dealCount: 0,
    social: "",
    isFollow: false,
    followOpen: false,
    followerOpen: false
  });
  const txtArea = useRef<HTMLTextAreaElement>(null);

  const { nickName } = useParams();
  const navigate = useNavigate();
  useEffect(() => {
    (async () => {
      const res = await getProfile(nickName as string);
      if (res.result === "FAIL") {
        navigate("/404");
      }
      if (res.result === "SUCCESS") {
        setUserProfile(res.data);
      }
    })();
  }, [nickName, isChanged]);

  const follow = (state: string) => {
    setFollowModal(state);
    setFollowClick(true);
  };

  const signal = () => {
    setFollowClick(false);
    setIsChanged(state => !state);
  };

  const goDM = () => {
    if (!userInfo) {
      return navigate("/login");
    }
    return navigate(`/letters/detail?with=${nickName}`);
  };
  //
  const setFollow = async () => {
    if (!userInfo) {
      return navigate("/login");
    }
    if (!wait) {
      setWait(true);
      if (userProfile.isFollow) {
        await delFollow(userProfile.nickname);
      } else {
        await addFollow(userProfile.nickname);
      }
      setIsChanged(state => !state);
      setWait(false);
    }
    return 0;
  };
  const randomImg = useMemo(() => {
    return Math.floor(Math.random() * 9);
  }, [nickName]);

  return (
    <div id="userfeed-page">
      <div className="profile">
        <div className="profile-background flex column">
          <img
            src={images[randomImg]}
            alt="Thum"
            className="profile-background__img"
            title="background"
          />
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
          {nickName === userInfo?.nickname &&
            (userInfo?.social === "normal" ? (
              <Link to="/account">
                <img src={SetIcon} alt="set" />
              </Link>
            ) : (
              <Link to="/account/set">
                <img src={SetIcon} alt="set" />
              </Link>
            ))}
        </div>
        <div className="info__follow flex">
          <button
            className="notoMid"
            type="button"
            onClick={() => {
              follow("팔로워");
            }}
          >
            팔로워
            <span>{getCounts(userProfile?.followerCount)}</span>
          </button>
          <button
            className="notoMid"
            type="button"
            onClick={() => {
              follow("팔로잉");
            }}
          >
            팔로잉
            <span>{getCounts(userProfile?.followCount)}</span>
          </button>
        </div>
        {userProfile.nickname !== userInfo?.nickname && (
          <div className="info__btn flex">
            <button
              type="button"
              onClick={setFollow}
              className={userProfile.isFollow ? "grey" : "yellow"}
            >
              {userProfile.isFollow ? "언팔로우" : "팔로우"}
            </button>
            <button onClick={goDM} type="button" className="yellow">
              DM
            </button>
          </div>
        )}
      </div>
      <div className="info__state notoReg flex ">
        <textarea
          className="info__state__textarea notoReg"
          value={
            userProfile?.profileMsg ? (userProfile.profileMsg as string) : ""
          }
          ref={txtArea}
          readOnly
        />
      </div>
      <div className="feed flex column align-center">
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
        <FollowList
          isMine={userProfile.nickname === userInfo?.nickname}
          isOpen={[userProfile.followerOpen, userProfile.followOpen]}
          idx={nickName as string}
          signal={signal}
          followModal={followModal}
        />
      ) : null}
    </div>
  );
}

export default UserFeedPage;
