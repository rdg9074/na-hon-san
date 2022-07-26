import React from "react";
import "./TipDetail.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import EmptyHeart from "@images/ArticleEmptyHeart.svg";

function TipDetail() {
  return (
    <div id="tip-detail-page">
      <div className="article flex column">
        <p className="title notoMid">
          💕자취 꿀팁 공유합니다~ 지금 배민 쿠폰뿌림 아무나 가져가쇼
        </p>
        <div className="header flex">
          <div className="header-info flex">
            <div className="header-info__img-container flex">
              <img src={UserDummyIcon} alt="dum" title="user-icon" />
            </div>
            <div className="header-info__text flex column">
              <p className="user-name notoMid">username</p>
              <p className="created notoReg">3시간 전</p>
            </div>
            <button className="header-info__btn notoReg" type="button">
              팔로우
            </button>
          </div>
          <div className="header-func flex">
            <div className="header-func-btn flex">
              <img src={EmptyHeart} alt="123" title="like" />
            </div>
          </div>
        </div>
        <div className="body"> </div>
        <div className="comment flex column">
          <div className="comment-head">
            <p className="notoMid">
              댓글<span className="">123</span>
            </p>
          </div>
          <div className="comment-input flex">
            <div className="input-img-container flex">
              <img src={UserDummyIcon} alt="dum" title="user-icon" />
            </div>
            <input type="text" placeholder="댓글을 입력해" />
          </div>
          <div className="comment-component"> </div>
        </div>
      </div>
    </div>
  );
}

export default TipDetail;
