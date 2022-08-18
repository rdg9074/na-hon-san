/* eslint-disable no-nested-ternary */
import React, { useState, useRef, useEffect } from "react";
import "./CardCarousel.scss";
import { v4 } from "uuid";
import rArrow from "@images/RightArrow.svg";
import lArrow from "@images/LeftArrow.svg";
import { getHoneyDealList } from "@apis/feed";
import Card, { CardType } from "@components/common/Card";
import CardSkeleton from "@components/common/CardSkeleton";
import { useAppSelector } from "@store/hooks";

function CardCarousel() {
  const [cardList, setCardList] = useState<Array<CardType>>([]);
  const [isLoading, setIsLoading] = useState(false);
  const likeCategorys = useAppSelector(state => state.auth.userInfo?.categorys);
  useEffect(() => {
    (async () => {
      const res = await getHoneyDealList();
      if (res.result === "SUCCESS") {
        setCardList(res.data);
      }
      setIsLoading(true);
    })();
  }, []);
  const [currentView, setCurrentView] = useState({
    total: 0,
    trans: 0
  });
  const [currentSlide, setCurrentSlide] = useState(0);
  const slideRef = useRef<HTMLDivElement>(null);
  const nextSlide = () => {
    if (currentSlide >= currentView.total) {
      setCurrentSlide(0);
    } else {
      setCurrentSlide(currentSlide + 1);
    }
  };
  const prevSlide = () => {
    if (currentSlide === 0) {
      setCurrentSlide(currentView.total);
    } else {
      setCurrentSlide(currentSlide - 1);
    }
  };
  useEffect(() => {
    if (slideRef.current !== null) {
      slideRef.current.style.transition = "all 0.5s ease-in-out";
      slideRef.current.style.transform = `translateX(-${
        currentSlide * currentView.trans
      }%)`;
    }
  }, [currentSlide]);

  useEffect(() => {
    if (window.innerWidth > 768) {
      setCurrentView({
        total: 1,
        trans: 50
      });
    } else if (window.innerWidth > 375) {
      setCurrentView({
        total: 2,
        trans: 33.5
      });
    } else
      setCurrentView({
        total: 5,
        trans: 16.7
      });
  }, [window.innerWidth]);

  return (
    <div id="usercarousel">
      {likeCategorys?.length === 0 ? (
        <p className="no-category notoReg fs-28 flex align-center justify-center">
          관심 카테고리를 설정해주세요!
        </p>
      ) : (
        <div className="container">
          {cardList && (
            <div className="slider flex justify-center" ref={slideRef}>
              {isLoading ? (
                cardList.length !== 0 ? (
                  cardList.map(value => (
                    <Card type="deal" data={value} key={v4()} />
                  ))
                ) : (
                  <p className="empty-message notoReg fs-28 flex align-center justify-center">
                    관심 카테고리에 알맞은 꿀딜이 없어요.
                  </p>
                )
              ) : (
                [0, 1, 2, 3, 4, 5].map(() => <CardSkeleton key={v4()} />)
              )}
            </div>
          )}

          <button
            className="prevbtn fs-48 flex"
            type="button"
            onClick={prevSlide}
          >
            <img src={lArrow} alt="lArrow" />
          </button>
          <button
            className="nextbtn fs-48 flex"
            type="button"
            onClick={nextSlide}
          >
            <img src={rArrow} alt="rArrow" />
          </button>
        </div>
      )}
    </div>
  );
}

export default CardCarousel;
