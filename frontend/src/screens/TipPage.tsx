import React, { useState } from "react";
import "./TipPage.scss";
import CardList from "@components/common/CardList";
import tipIcon from "@images/TipIcon.svg";
import searchIcon from "@images/Search.svg";
import HoneyRecipe from "@images/HoneyRecipe.svg";
import HoneyTem from "@images/HoneyTem.svg";
import HoneyTip from "@images/HoneyTip.svg";

function TipPage() {
  const [tag, setTag] = useState(0);
  return (
    <div id="tip-page">
      <div className="intro flex">
        <div className="intro-info">
          <div className="intro-info__title flex">
            <p className="fs-48 notoReg">
              <span>꿀</span>팁
            </p>
            <button className="notoReg" type="button">
              꿀팁쓰기
            </button>
          </div>
          <p className="intro-info__desc p-none notoReg">
            뭔가 좋은 문구가 있으면 좋을 것 같다는 생각이 조금 씩 들긴 하는데
            이걸 조금 더 길게 만들어서 이쁘게 만들어두면
          </p>
        </div>
        <div className="intro-container notoBold">
          <img
            className="intro-container__img"
            src={tipIcon}
            alt="deal"
            title="Tip"
          />
          <p className="intro-container__count">
            광주 지역 꿀팁러들의 꿀팁
            <br />
            <span>366</span>개
          </p>
        </div>
      </div>
      <div className="tip">
        <div className="tip-header flex">
          <p className="fs-36 notoBold">광주 지역 꿀팁</p>
          <div>
            <img src={searchIcon} alt="tip" />
            <input type="text" placeholder="검색어를 입력해 주십쇼..." />
          </div>
        </div>
        <div className="notoBold tag flex">
          <button
            type="button"
            className={`tag-item flex column ${tag === 0 ? "active" : ""}`}
            onClick={() => {
              setTag(0);
            }}
          >
            <img src={HoneyRecipe} alt="recipe" title="honey-recipe" />
            <p>여긴 꿀시피</p>
          </button>
          <button
            type="button"
            className={`tag-item flex column ${tag === 1 ? "active" : ""}`}
            onClick={() => {
              setTag(1);
            }}
          >
            <img src={HoneyTip} alt="recipe" title="honey-tip" />
            <p>여긴 꿀생</p>
          </button>
          <button
            type="button"
            className={`tag-item flex column ${tag === 2 ? "active" : ""}`}
            onClick={() => {
              setTag(2);
            }}
          >
            <img src={HoneyTem} alt="recipe" title="honey-tem" />
            <p>여긴 꿀템</p>
          </button>
        </div>
        <div className="tip-sort flex">
          <select className="tip-sort__list notoMid">
            <option value="1">최신순</option>
            <option value="2">조회순</option>
            <option value="3">인기순</option>
          </select>
        </div>
        <CardList type="tip" />
      </div>
    </div>
  );
}

export default TipPage;
