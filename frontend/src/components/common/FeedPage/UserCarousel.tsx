import React, { useState, useRef, useEffect } from "react";
import "./UserCarousel.scss";
import { v4 } from "uuid";
import rArrow from "@images/RightArrow.svg";
import lArrow from "@images/LeftArrow.svg";
import UserCarouselItem from "./UserCarouselItem";

function UserCarousel() {
  const [currentView, setCurrentView] = useState({
    total: 0,
    trans: 0
  });
  const arr = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
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
        total: 3,
        trans: 25
      });
    } else
      setCurrentView({
        total: 5,
        trans: 16.8
      });
  }, [window.innerWidth]);

  return (
    <div id="usercarousel">
      <div className="container">
        <div className="slider flex" ref={slideRef}>
          {arr.map(() => {
            return (
              <div className="fs-48" key={v4()}>
                <UserCarouselItem />
              </div>
            );
          })}
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

export default UserCarousel;
