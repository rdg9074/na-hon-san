import React from "react";
import { v4 } from "uuid";
import "./NonFeed.scss";
import FeedUserItem from "./FeedUserItem";

function NonFeed() {
  const users = [1, 2, 3, 4];
  return (
    <div id="nonfeed-page">
      <div className="title flex column">
        <div className="flex column align-center">
          <p className="title-title notoMid">팔로우 된 유저가 없습니다 ㅠㅠ</p>
        </div>
        <div className="title-body flex column">
          <p className="title-body-main notoBold">
            나혼자잘산다 유저를 팔로우 해보세요!
          </p>
          <p className="title-body-sub notoMid">
            팔로우한 유저의 다양한 게시글을 한번에 볼 수 있습니다.
          </p>
        </div>
      </div>
      <div className="wrapper flex ">
        <div className="user-list flex">
          {users.map(() => {
            return <FeedUserItem key={v4()} />;
          })}
        </div>
      </div>
    </div>
  );
}

export default NonFeed;
