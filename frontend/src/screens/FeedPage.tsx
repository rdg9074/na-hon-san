import CardList from "@components/common/CardList";
import CardCarousel from "@components/common/FeedPage/CardCarousel";
import UserCarousel from "@components/common/FeedPage/UserCarousel";
import React from "react";
import "./FeedPage.scss";

function FeedPage() {
  return (
    <div id="feed-page">
      <CardCarousel />
      <UserCarousel />
      <div className="card-list">{/* <CardList type="tip" /> */}</div>
    </div>
  );
}

export default FeedPage;
