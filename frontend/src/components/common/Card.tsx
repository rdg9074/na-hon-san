import React, { useEffect, useState } from "react";
import "./Card.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import ThumDummy from "@images/ThumnailDummy.jpg";
import HeartIcon from "@images/HeartSkelton.svg";
import ViewIcon from "@images/ViewSkelton.svg";
import CommentIcon from "@images/CommentSkelton.svg";
import getCounts from "@utils/getCounts";
import TipIcon from "@images/Tip.svg";
import DealIcon from "@images/Deal.svg";
import { getDummyImg } from "@apis/dummy";

type CardProps = {
  type: "tip" | "deal";
  data: any;
};

function Card({ type, data }: CardProps) {
  // const [imgSrc, setImgSrc] = useState("");
  // useEffect(() => {
  //   let url: string;
  //   (async () => {
  //     url = await getDummyImg(data.download_url);
  //     setImgSrc(url);
  //   })();
  //   return () => window.URL.revokeObjectURL(url);
  // }, []);
  return (
    <div id="card">
      <header className="card-header flex align-center">
        <img
          className="card-header__user-img"
          src={UserDummyIcon}
          alt="유저더미"
        />

        <p className="card-header__user-name notoBold fs-14">YEOM-JINHO</p>
      </header>
      <main className="card-main flex column">
        <div className="img-container">
          <img
            className="card-main__thumnail"
            src={data.imgSrc}
            alt="썸네일더미"
          />
        </div>

        <div className="card-label">
          <div
            className={
              type === "tip"
                ? "card-label__top--tip  flex align-center justify-center"
                : "card-label__top--deal flex align-center justify-center"
            }
          >
            <img
              className="card-label__img"
              src={type === "tip" ? TipIcon : DealIcon}
              alt="꿀팁"
            />
            <p className="card-label__title notoMid fs-12">
              {type === "tip" ? "꿀팁" : "꿀딜"}
            </p>
          </div>
          <div className="card-label__bottom" />
        </div>

        <p className="card-main__title notoBold fs-14 ellipsis">
          저의 침대는 어쩌고 저쩌고~~~~~~~그랬다구요~~~~~~ 13123123213
        </p>
      </main>
      <footer className="card-footer flex align-center justify-center">
        <div className="icon-container flex align-center">
          <img
            className="icon-container__icon"
            src={HeartIcon}
            alt="좋아요수"
          />
          <p className="icon-container__cnt notoReg fs-15">
            {getCounts(12345)}
          </p>
        </div>
        <div className="icon-container flex align-center">
          <img
            className="icon-container__icon"
            src={CommentIcon}
            alt="댓글수"
          />
          <p className="icon-container__cnt notoReg fs-15">
            {getCounts(12345)}
          </p>
        </div>
        <div className="icon-container flex align-center">
          <img className="icon-container__icon" src={ViewIcon} alt="조회수" />
          <p className="icon-container__cnt notoReg fs-15">
            {getCounts(12345)}
          </p>
        </div>
      </footer>
    </div>
  );
}

export default Card;
