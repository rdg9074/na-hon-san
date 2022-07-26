import React, { useState } from "react";
import "./DealPage.scss";
import DealImg from "@images/DealImg.svg";
import CardList from "@components/common/CardList";
import { v4 } from "uuid";
import searchIcon from "@images/Search.svg";

function DealPage() {
  const [dealState, setDealState] = useState(true);
  const cate = [
    "전체",
    "생활",
    "가전",
    "주방",
    "욕실",
    "취미",
    "의류",
    "인테리어",
    "기타"
  ];
  return (
    <div id="deal-page">
      <div className="intro flex">
        <div className="intro-info">
          <div className="intro-info__title flex">
            <p className="fs-48 notoReg">
              <span>꿀</span>딜
            </p>
            <button className="notoReg" type="button">
              꿀딜쓰기
            </button>
          </div>
          <p className="intro-info__desc p-none notoReg">
            뭔가 좋은 문구가 있으면 좋을 것 같다는 생각이 조금 씩 들긴 하는데
            이걸 조금 더 길게 만들어서 이쁘게 만들어두면
          </p>
        </div>
        <div className="intro-container notoBold">
          <img
            className="intro-container__img p-nonee"
            src={DealImg}
            alt="deal"
            title="deal"
          />
          <p className="intro-container__count">
            광주 지역에서 진행중인 꿀딜
            <br />
            <span>366</span>건
          </p>
        </div>
      </div>
      <div className="hotdeal">
        <p className="fs-36 notoBold">광주 지역 인기 꿀딜</p>
        <CardList type="deal" />
      </div>
      <div className="deal">
        <div className="deal-header flex">
          <p className="fs-36 notoBold">광주 지역 꿀딜</p>
          <div>
            <img src={searchIcon} alt="deal" />
            <input type="text" placeholder="검색어를 입력해 주십쇼..." />
          </div>
        </div>
        <div className="deal-tag">
          {cate.map(value => {
            return (
              <button className="notoReg" key={v4()} type="button">
                {value}
              </button>
            );
          })}
        </div>
        <div className="deal-state notoBold flex">
          <button
            onClick={() => {
              setDealState(true);
            }}
            className={dealState ? `${"active"}` : ""}
            type="button"
          >
            거래 대기
          </button>
          <button
            onClick={() => {
              setDealState(false);
            }}
            className={dealState ? "" : `${"active"}`}
            type="button"
          >
            거래 완료
          </button>
        </div>
        <div className="deal-sort flex">
          <select className="deal-sort__list notoMid">
            <option value="1">최신순</option>
            <option value="2">조회순</option>
            <option value="3">인기순</option>
          </select>
        </div>
        <CardList type="deal" />
      </div>
    </div>
  );
}

export default DealPage;
