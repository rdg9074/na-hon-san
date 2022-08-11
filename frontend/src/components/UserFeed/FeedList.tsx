/* eslint-disable no-nested-ternary */
import { getFeedItemList } from "@apis/setAccount";
import React, { useEffect, useState } from "react";
import { v4 } from "uuid";
import "./FeedList.scss";
import loadingSpinner from "@images/LoadingSpinner.svg";
import FeedListItem, { FeedItemType } from "./FeedListItem";

type FeedListProps = {
  type: "deal" | "tip";
  userNickname: string;
};

function FeedList({ type, userNickname }: FeedListProps) {
  const [feedList, setFeedList] = useState<Array<FeedItemType>>([]);
  const [isLoading, setIsLoading] = useState(true);
  useEffect(() => {
    setIsLoading(true);
    (async () => {
      const res = await getFeedItemList(userNickname, type === "tip" ? 0 : 1);
      if (res.result === "SUCCESS") {
        setFeedList(res.data);
      }
      setIsLoading(false);
    })();
  }, [type, userNickname]);
  return (
    <div id="feedlist" className="flex">
      {isLoading ? (
        <div className="spinner-wrapper flex justify-center">
          <img
            src={loadingSpinner}
            title="로딩스피너"
            alt="로딩스피너"
            className="loading-spinner"
          />
        </div>
      ) : feedList.length === 0 ? (
        <div className="no-cotent fs-24 notoReg flex justify-center align-center">
          작성한 글이 없습니다!
        </div>
      ) : (
        feedList.map(feed => (
          <FeedListItem type={type} feed={feed} key={v4()} />
        ))
      )}
    </div>
  );
}

export default FeedList;
