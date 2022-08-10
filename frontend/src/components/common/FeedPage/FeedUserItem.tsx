import React from "react";
import "./FeedUserItem.scss";
import { v4 } from "uuid";
import UserDummyIcon from "@images/UserDummy.svg";
import { CardType } from "../Card";
import FeedListItem from "../UserFeed/FeedListItem";

type UserCardType = {
  // cnt: number;
  // follow_id: string;
  follow_nickname: string;
  profileImg: string | null;
  tipViewDtoList: Array<CardType>;
};

function FeedUserItem({
  data: { follow_nickname: nickName, profileImg, tipViewDtoList: tipList }
}: {
  data: UserCardType;
}) {
  return (
    <div id="feeduser-item">
      <div className="user-item">
        <div className="user-item-title flex">
          <img
            src={
              profileImg
                ? `data:image/jpeg;base64,${profileImg}`
                : UserDummyIcon
            }
            alt="icon"
            className="user-item-title__img flex"
          />
          <div className="user-item-title__info flex column">
            <p className="notoMid fs-24">{nickName}</p>
            <button type="button" className="notoReg fs-16">
              팔로우
            </button>
          </div>
        </div>
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
      </div>
    </div>
  );
}
export default FeedUserItem;
