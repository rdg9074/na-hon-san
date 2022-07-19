import React from "react";
import HeartIcon from "@images/HeartSkelton.svg";
import ViewIcon from "@images/ViewSkelton.svg";
import CommentIcon from "@images/CommentSkelton.svg";
import "./CardSkeleton.scss";

function CardSkeleton() {
  return (
    <div id="card-skeleton">
      <header className="card-header flex align-center">
        <div className="card-header__user-img skeleton" />

        <p className="card-header__user-name skeleton" />
      </header>
      <main className="card-main flex column">
        <div className="img-container skeleton" />
        <div className="card-label">
          <div className="card-label__top--tip  flex align-center justify-center" />
          <div className="card-label__bottom" />
        </div>
        <p className="card-main__title" />
      </main>
      <footer className="card-footer flex align-center justify-center">
        <div className="icon-container flex align-center">
          <img
            className="icon-container__icon"
            src={HeartIcon}
            alt="좋아요수"
          />
          <p className="icon-container__cnt notoReg fs-15" />
        </div>
        <div className="icon-container flex align-center">
          <img
            className="icon-container__icon"
            src={CommentIcon}
            alt="댓글수"
          />
          <p className="icon-container__cnt notoReg fs-15" />
        </div>
        <div className="icon-container flex align-center">
          <img className="icon-container__icon" src={ViewIcon} alt="조회수" />
          <p className="icon-container__cnt notoReg fs-15" />
        </div>
      </footer>
    </div>
  );
}

export default CardSkeleton;
