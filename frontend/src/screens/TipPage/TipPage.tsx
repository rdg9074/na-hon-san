import React, { useEffect, useState } from "react";
import "./TipPage.scss";
import tipIcon from "@images/TipIcon.svg";
import searchIcon from "@images/Search.svg";
import HoneyRecipe from "@images/HoneyRecipe.svg";
import HoneyTem from "@images/HoneyTem.svg";
import HoneyTip from "@images/HoneyTip.svg";
import { useNavigate } from "react-router-dom";
import InFinityScroll from "@components/common/InFinityScroll";
import { getTipTotalCnt } from "@apis/honeyTip";
import { useAppSelector } from "@store/hooks";
import TipBanner from "@images/TipBanner.jpg";

function TipPage() {
  const navigate = useNavigate();
  const isLoggedIn = !!useAppSelector(state => state.auth.userInfo);
  const goEdit = () => {
    if (!isLoggedIn) {
      navigate("/login");
    } else {
      navigate("create");
    }
  };
  const [conditions, setConditions] = useState({
    category: "tip",
    sort: "최신순",
    keyword: null
  });
  const [totalCnt, setTotalCnt] = useState(0);

  const handleConditions = (type: string, value: string) => {
    if (type === "keyword" && value === "") {
      setConditions({ ...conditions, [type]: null });
    } else {
      setConditions({ ...conditions, [type]: value });
    }
  };
  useEffect(() => {
    (async () => {
      const res = await getTipTotalCnt();
      setTotalCnt(res.total);
    })();
  }, []);
  return (
    <div id="tip-page">
      <div className="tip-banner">
        <img src={TipBanner} alt="TipBanner" title="TipBanner" />
      </div>
      <div className="intro flex">
        <div className="intro-info">
          <div className="intro-info__title flex">
            <p className="fs-48 notoBold">
              <span>꿀</span>팁
            </p>
            <button className="notoReg" type="button" onClick={goEdit}>
              꿀팁쓰기
            </button>
          </div>
          <p className="intro-info__desc p-none notoReg">
            나혼자 잘사는 자취 만렙러들의 꿀팁 <br />
            엄청난 꿀팁을 한눈에!
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
            나 혼자 잘 사는 꿀팁
            <br />
            <span>{totalCnt}</span>개
          </p>
        </div>
      </div>
      <div className="tip">
        <div className="tip-header flex">
          <p className="fs-36 notoBold">혼자 잘 사는 꿀팁</p>
          <div>
            <img src={searchIcon} alt="tip" />
            <input
              type="text"
              placeholder="검색어를 입력해 주세요."
              onChange={e => handleConditions("keyword", e.target.value)}
            />
          </div>
        </div>
        <div className="notoBold tag flex">
          <button
            type="button"
            className={`tag-item flex column ${
              conditions.category === "recipe" ? "active" : ""
            }`}
            onClick={() => {
              handleConditions("category", "recipe");
            }}
          >
            <img src={HoneyRecipe} alt="recipe" title="honey-recipe" />
            <p>여긴 꿀시피</p>
          </button>
          <button
            type="button"
            className={`tag-item flex column ${
              conditions.category === "tip" ? "active" : ""
            }`}
            onClick={() => {
              handleConditions("category", "tip");
            }}
          >
            <img src={HoneyTip} alt="tip" title="honey-tip" />
            <p>여긴 꿀생</p>
          </button>
          <button
            type="button"
            className={`tag-item flex column ${
              conditions.category === "item" ? "active" : ""
            }`}
            onClick={() => {
              handleConditions("category", "item");
            }}
          >
            <img src={HoneyTem} alt="item" title="honey-tem" />
            <p>여긴 꿀템</p>
          </button>
        </div>
        <div className="tip-sort flex">
          <select
            className="tip-sort__list notoMid"
            onChange={e => handleConditions("sort", e.target.value)}
          >
            <option value="최신순">최신순</option>
            <option value="조회순">조회순</option>
            <option value="좋아요순">좋아요순</option>
          </select>
        </div>
        <InFinityScroll
          searchType="tip"
          type={conditions.sort}
          keyword={conditions.keyword}
          category={conditions.category}
          categorys={undefined}
          state={undefined}
          area={null}
        />
      </div>
    </div>
  );
}

export default TipPage;
