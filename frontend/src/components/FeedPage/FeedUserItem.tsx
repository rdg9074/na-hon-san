import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./FeedUserItem.scss";
import { v4 } from "uuid";
import UserDummyIcon from "@images/UserDummy.svg";
import { addFollow, delFollow } from "@apis/userFeed";
import { CardType } from "@components/common/Card";
import FeedListItem from "@components/UserFeed/FeedListItem";

type UserCardType = {
  cnt: number;
  follow_id: string;
  follow_nickname: string;
  profileImg: string | null;
  tipViewDtoList: Array<CardType>;
  isFollow: boolean;
};

function FeedUserItem({
  data: {
    follow_nickname: nickName,
    profileImg,
    tipViewDtoList: tipList,
    cnt,
    isFollow
  },
  changed
}: {
  data: UserCardType;
  changed: () => void;
}) {
  const [isLoading, setIsLoading] = useState(false);

  const setFollow = async () => {
    if (!isLoading) {
      setIsLoading(true);
      if (isFollow) {
        await delFollow(nickName);
      } else {
        await addFollow(nickName);
      }
      setIsLoading(false);
    }
    changed();
    return 0;
  };
  return (
    <div id="feeduser-item">
      <div className="user-item flex">
        <div className="user-item-title flex">
          <Link to={`/userFeed/${nickName}`}>
            <img
              src={
                profileImg
                  ? `data:image/jpeg;base64,${profileImg}`
                  : UserDummyIcon
              }
              alt="icon"
              className="user-item-title__img flex"
            />
          </Link>
          <div className="user-item-title__info flex column">
            <Link
              to={`/userfeed/${nickName}`}
              className="user-item-title__info__nickname notoMid "
            >
              {nickName}
            </Link>
            <p className="user-item-title__info__cnt notoReg grey">
              팔로우 {cnt}
            </p>
            <button
              onClick={setFollow}
              type="button"
              className="info-button notoReg t-none"
            >
              {isFollow ? "언팔로우" : "팔로우"}
            </button>
          </div>
          <button
            onClick={setFollow}
            type="button"
            className="info-button notoReg t-show"
          >
            {isFollow ? "언팔로우" : "팔로우"}
          </button>
        </div>
        {tipList && (
          <div className="user-item-body flex">
            {tipList.length !== 0 &&
              tipList.map(feed => {
                return (
                  <FeedListItem
                    key={v4()}
                    type="tip"
                    feed={{
                      idx: feed.idx,
                      title: "",
                      bannerImg: feed.bannerImg,
                      likeCnt: feed.likes,
                      commentCnt: feed.comment,
                      viewCnt: feed.view
                    }}
                  />
                );
              })}
          </div>
        )}
      </div>
    </div>
  );
}
export default FeedUserItem;
