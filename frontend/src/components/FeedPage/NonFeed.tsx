import React, { useState, useEffect } from "react";
import { v4 } from "uuid";
import "./NonFeed.scss";
import loadingSpinner from "@images/LoadingSpinner.svg";
import { getPopUsers } from "@apis/feed";
import FeedUserItem from "./FeedUserItem";

type NonFeedProps = {
  changed: () => void;
};

function NonFeed({ changed }: NonFeedProps) {
  const [popUsers, setPopUsers] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    (async () => {
      const res = await getPopUsers();
      if (res.result === "SUCCESS") {
        setPopUsers(res.data);
        setIsLoading(true);
      }
    })();
  }, []);

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
      <div className="user-title flex justify-center">
        <p className="notoBold fs-36">
          나혼자 <span className="notoBold">잘</span> 사는 사람들
        </p>
      </div>
      <div className="wrapper flex ">
        <div className="user-list flex align-center">
          {isLoading ? (
            popUsers.map(user => {
              return <FeedUserItem changed={changed} data={user} key={v4()} />;
            })
          ) : (
            <img
              src={loadingSpinner}
              title="로딩스피너"
              alt="로딩스피너"
              className="loading-spinner"
            />
          )}
        </div>
      </div>
    </div>
  );
}

export default NonFeed;
