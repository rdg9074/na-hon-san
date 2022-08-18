/* eslint-disable no-nested-ternary */
import CardCarousel from "@components/FeedPage/CardCarousel";
import InFinityScroll from "@components/common/InFinityScroll";
import React, { useEffect, useState } from "react";
import "./FeedPage.scss";
import loadingSpinner from "@images/LoadingSpinner.svg";
import { getProfile } from "@apis/setAccount";
import { useAppSelector } from "@store/hooks";
import { useNavigate } from "react-router-dom";
import NonFeed from "@components/FeedPage/NonFeed";

function FeedPage() {
  const [isLoading, setIsLoading] = useState(false);
  const [followCnt, setFollowCnt] = useState(0);
  const [change, setChange] = useState(false);
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const navigate = useNavigate();
  useEffect(() => {
    if (!userInfo) {
      navigate("/login");
      return;
    }

    (async () => {
      const res = await getProfile(userInfo?.nickname as string);
      if (res.result === "SUCCESS") {
        setFollowCnt(res.data.followCount);
      }
      setIsLoading(true);
    })();
  }, [change]);

  const changed = () => {
    setChange(state => !state);
  };

  return (
    <div id="feed-page">
      <div className="carousel-label flex justify-center">
        <p className="fs-36 notoBold flex justify-center">
          맞춤형 <span> 꿀</span>딜 추천!
        </p>
      </div>
      {userInfo && <CardCarousel />}
      {!isLoading ? (
        <img
          src={loadingSpinner}
          title="로딩스피너"
          alt="로딩스피너"
          className="loading-spinner"
        />
      ) : followCnt === 0 ? (
        <NonFeed changed={changed} />
      ) : (
        <div className="card-list">
          <div className="carousel-label flex justify-center">
            <p className="fs-36 notoBold flex justify-center">
              팔로워들 <span> 꿀</span>팁!
            </p>
          </div>
          <InFinityScroll
            searchType="followTip"
            type="최신순"
            keyword={null}
            category={undefined}
            categorys={undefined}
            state={undefined}
            area={null}
          />
        </div>
      )}
    </div>
  );
}

export default FeedPage;
