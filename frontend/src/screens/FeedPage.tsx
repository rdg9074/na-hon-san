/* eslint-disable no-nested-ternary */
import CardCarousel from "@components/common/FeedPage/CardCarousel";
import InFinityScroll from "@components/common/InFinityScroll";
import React, { useEffect, useState } from "react";
import "./FeedPage.scss";
import loadingSpinner from "@images/LoadingSpinner.svg";
import { getProfile } from "@apis/setAccount";
import { useAppSelector } from "@store/hooks";
import { useNavigate } from "react-router-dom";
import NonFeed from "@components/common/FeedPage/NonFeed";

function FeedPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [followCnt, setFollowCnt] = useState(0);
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const navigate = useNavigate();
  useEffect(() => {
    if (!userInfo) {
      navigate("/login");
    }

    (async () => {
      const res = await getProfile(userInfo?.nickname as string);
      if (res.result === "SUCCESS") {
        setFollowCnt(res.data.followCount);
      }
      setIsLoading(true);
    })();
  }, []);
  return (
    <div id="feed-page">
      <CardCarousel />
      {!isLoading ? (
        <img
          src={loadingSpinner}
          title="로딩스피너"
          alt="로딩스피너"
          className="loading-spinner"
        />
      ) : followCnt === 0 ? (
        <NonFeed />
      ) : (
        <div className="card-list">
          <InFinityScroll
            searchType="followTip"
            type="최신순"
            keyword={null}
            category={undefined}
            categorys={undefined}
            state={undefined}
          />
        </div>
      )}
    </div>
  );
}

export default FeedPage;
