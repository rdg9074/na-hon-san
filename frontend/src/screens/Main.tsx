import CardList from "@components/common/CardList";
import { useAppSelector } from "@store/hooks";
import React from "react";
import { Link } from "react-router-dom";
import "./Main.scss";

function Main() {
  const userInfo = useAppSelector(state => state.auth.userInfo);
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
        <p className="title notoBold">
          가장 많이 본 <span>꿀</span>팁!
        </p>
        <CardList
          searchType="tip"
          condition={{
            type: "조회순",
            keyword: null,
            pageSize: 6,
            lastIdx: null,
            lastView: null,
            lastLikes: null,
            category: null,
            categorys: [],
            state: "",
            area: null
          }}
          pure
        />
        <p className="title notoBold">
          {`${
            userInfo?.area ? userInfo?.area?.split(" ")[0].slice(0, 2) : "전체"
          }`}
          에서 가장 많이 본 <span>꿀</span>딜!
        </p>
        <CardList
          searchType="deal"
          condition={{
            type: "조회순",
            keyword: null,
            pageSize: 6,
            lastIdx: null,
            lastView: null,
            lastLikes: null,
            category: null,
            categorys: ["전체"],
            state: "거래 대기",
            area: userInfo?.area
              ? userInfo?.area?.split(" ")[0].slice(0, 2)
              : null
          }}
          pure
        />
      </main>
    </div>
  );
}

export default Main;
