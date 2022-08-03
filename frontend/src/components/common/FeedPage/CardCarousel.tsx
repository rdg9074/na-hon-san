import React, { useState, useRef, useEffect } from "react";
import "./CardCarousel.scss";
import { v4 } from "uuid";
import rArrow from "@images/RightArrow.svg";
import lArrow from "@images/LeftArrow.svg";
import { getDummy } from "@apis/dummy";
import Card from "../Card";
import CardSkeleton from "../CardSkeleton";

function CardCarousel() {
  const [dummy, setDummy] = useState<Array<any>>([]);
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    (async () => {
      const res = await getDummy();
      setDummy(res);
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
        total: 2,
        trans: 40
      });
    } else if (window.innerWidth > 375) {
      setCurrentView({
        total: 3,
        trans: 25
      });
    } else
      setCurrentView({
        total: 5,
        trans: 16.7
      });
  }, [window.innerWidth]);

  return (
    <div id="usercarousel">
      <div className="container">
        <div className="slider flex justify-center" ref={slideRef}>
          {/* {isLoading
            ? dummy.map(value => <Card type="deal" data={value} key={v4()} />)
            : [0, 1, 2, 3].map(() => <CardSkeleton key={v4()} />)} */}
        </div>
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
    </div>
  );
}

export default CardCarousel;
