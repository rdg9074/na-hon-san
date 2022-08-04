import React, { useMemo, useState } from "react";
import "./DealPage.scss";
import DealImg from "@images/DealImg.svg";
import CardList from "@components/common/CardList";
import { v4 } from "uuid";
import searchIcon from "@images/Search.svg";
import InFinityScroll from "@components/common/InFinityScroll";
import dealCategory from "@constants/dealCategory";

function DealPage() {
  const [conditions, setConditions] = useState({
    categorys: ["전체", ...dealCategory],
    sort: "최신순",
    keyword: null,
    state: "거래 대기"
  });

  const hotCondition = useMemo(
    () => ({
      type: "좋아요순",
      keyword: null,
      pageSize: 6,
      category: null,
      lastIdx: null,
      lastView: null,
      lastLikes: null,
      categorys: ["전체"],
      state: "거래 대기"
    }),
    []
  );

  const handleConditions = (type: string, value: string | string[]) => {
    if (type === "keyword" && value === "") {
      setConditions({ ...conditions, [type]: null });
    } else {
      setConditions({ ...conditions, [type]: value });
    }
  };

  const toggleCategorys = (value: string) => {
    const index = conditions.categorys.indexOf(value);
    if (index === -1) {
      const nextConditions = [...conditions.categorys, value];
      if (
        value === "전체" ||
        JSON.stringify(nextConditions.concat().sort()) ===
          JSON.stringify(dealCategory.concat().sort())
      ) {
        handleConditions("categorys", ["전체", ...dealCategory]);
      } else {
        handleConditions("categorys", [...conditions.categorys, value]);
      }
    } else {
      const nextConditions = conditions.categorys.filter(
        category => category !== value
      );
      if (
        value === "전체" ||
        JSON.stringify(nextConditions) === JSON.stringify(["전체"])
      ) {
        handleConditions("categorys", []);
      } else {
        handleConditions("categorys", nextConditions);
      }
    }
  };

  const categoryClass = (value: string) => {
    const prefix = "notoReg";
    if (conditions.categorys.indexOf(value) === -1) {
      return prefix;
    }
    return `${prefix} selected`;
  };

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
        {/* <CardList searchType="deal" condition={hotCondition} pure /> */}
      </div>
      <div className="deal">
        <div className="deal-header flex">
          <p className="fs-36 notoBold">광주 지역 꿀딜</p>
          <div>
            <img src={searchIcon} alt="deal" />
            <input
              type="text"
              placeholder="검색어를 입력해 주십쇼..."
              onChange={e => handleConditions("keyword", e.target.value)}
            />
          </div>
        </div>
        <div className="deal-tag">
          <button
            className={categoryClass("전체")}
            type="button"
            onClick={() => toggleCategorys("전체")}
          >
            전체
          </button>
          {dealCategory.map(category => {
            return (
              <button
                className={categoryClass(category)}
                key={v4()}
                type="button"
                onClick={() => toggleCategorys(category)}
              >
                {category}
              </button>
            );
          })}
        </div>
        <div className="deal-state notoBold flex">
          <button
            onClick={() => {
              handleConditions("state", "거래 대기");
            }}
            className={conditions.state === "거래 대기" ? `${"active"}` : ""}
            type="button"
          >
            거래 대기
          </button>
          <button
            onClick={() => {
              handleConditions("state", "거래 완료");
            }}
            className={conditions.state === "거래 대기" ? "" : `${"active"}`}
            type="button"
          >
            거래 완료
          </button>
        </div>
        <div className="deal-sort flex">
          <select
            className="deal-sort__list notoMid"
            onChange={e => handleConditions("sort", e.target.value)}
          >
            <option value="최신순">최신순</option>
            <option value="조회순">조회순</option>
            <option value="좋아요순">좋아요순</option>
          </select>
        </div>
        <InFinityScroll
          searchType="deal"
          type={conditions.sort}
          keyword={conditions.keyword}
          categorys={conditions.categorys}
          category={undefined}
          state={conditions.state}
        />
      </div>
    </div>
  );
}

export default DealPage;
