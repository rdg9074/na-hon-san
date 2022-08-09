import CardList from "@components/common/CardList";
import CardCarousel from "@components/common/FeedPage/CardCarousel";
import UserCarousel from "@components/common/FeedPage/UserCarousel";
import InFinityScroll from "@components/common/InFinityScroll";
import React from "react";
import "./FeedPage.scss";

function FeedPage() {
  return (
    <div id="feed-page">
      <CardCarousel />
      <UserCarousel />
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
    </div>
  );
}

export default FeedPage;
