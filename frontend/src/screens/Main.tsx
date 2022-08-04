import CardList from "@components/common/CardList";
import React from "react";
import { Link } from "react-router-dom";
import "./Main.scss";

function Main() {
  return (
    <div id="main">
      <div className="banner flex">
        <div className="banner-info">
          <p className="banner-info__desc notoBold fs-40">
            나한테 딱 맞는
            <br />
            자취꿀팁!
          </p>
          <Link
            to="/tip"
            className="banner-info__btn--move-list notoBold flex align-center justify-center fs-22"
          >
            자취 꿀팁 보러가기
          </Link>
        </div>
        <div className="banner-chat flex column justify-center">
          <div className="banner-chats flex column">
            <p className="banner-chats__item fs-20 flex align-center justify-center notoMid">
              자취가 처음인데..
            </p>
            <p className="banner-chats__item fs-20 flex align-center justify-center notoMid">
              어떤거부터 사야하지???
            </p>
            <p className="banner-chats__item fs-20 flex align-center justify-center notoMid">
              ㅠㅠㅠㅠ
            </p>
          </div>
        </div>
      </div>
      <main className="main flex column align-center">
        <p className="title notoBold">현재 핫한 꿀팁들!</p>
        <CardList
          searchType="tip"
          condition={{
            type: "좋아요순",
            keyword: null,
            pageSize: 6,
            lastIdx: null,
            lastView: null,
            lastLikes: null,
            category: null,
            categorys: [],
            state: ""
          }}
          pure
        />
        <p className="title notoBold">현재 핫한 꿀딜들!</p>
        <CardList
          searchType="deal"
          condition={{
            type: "좋아요순",
            keyword: null,
            pageSize: 6,
            lastIdx: null,
            lastView: null,
            lastLikes: null,
            category: null,
            categorys: ["전체"],
            state: "거래 대기"
          }}
          pure
        />
      </main>
    </div>
  );
}

export default Main;
