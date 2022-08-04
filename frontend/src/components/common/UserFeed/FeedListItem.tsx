/* eslint-disable no-nested-ternary */
import React, { useState } from "react";
import "./FeedListItem.scss";
import ThumDummy from "@images/ThumnailDummy.jpg";
import HeartIcon from "@images/HeartSkelton.svg";
import ViewIcon from "@images/ViewSkelton.svg";
import CommentIcon from "@images/CommentSkelton.svg";
import getCounts from "@utils/getCounts";
import { Link } from "react-router-dom";
import tipIcon from "@images/TipIcon.svg";
import DealImg from "@images/DealImg.svg";

export type FeedItemType = {
  idx: number;
  title: string;
  bannerImg: string | null;
  likeCnt: number;
  commentCnt: number;
  viewCnt: number;
};

function FeedListItem({
  type,
  feed: { idx, title, bannerImg, likeCnt, commentCnt, viewCnt }
}: {
  type: "deal" | "tip";
  feed: FeedItemType;
}) {
  const [hideInfo, setHideInfo] = useState(true);
  const hide = () => {
    if (hideInfo) {
      setHideInfo(false);
    } else {
      setHideInfo(true);
    }
  };
  return (
    <Link
      id="feed-item"
      className="item flex column"
      to={`/${type}/detail/${idx}`}
    >
      <div className="img-container" onMouseEnter={hide} onMouseLeave={hide}>
        {hideInfo ? null : (
          <div className="feed-info flex column">
            <p className="flex align-center notoReg fs-16">
              <img src={HeartIcon} alt="heart" />
              {getCounts(likeCnt)}
            </p>
            <p className="flex align-center notoReg fs-16">
              <img src={CommentIcon} alt="comment" />
              {getCounts(commentCnt)}
            </p>
            <p className="flex align-center notoReg fs-16">
              <img src={ViewIcon} alt="view" />
              {getCounts(viewCnt)}
            </p>
          </div>
        )}
        <img
          className="item__img"
          src={
            bannerImg
              ? `data:image/jpeg;base64,${bannerImg}`
              : type === "tip"
              ? tipIcon
              : DealImg
          }
          alt="Dum"
        />
        <p className="item__title notoBold fs-14 ellipsis">{title}</p>
      </div>
    </Link>
  );
}

export default FeedListItem;
