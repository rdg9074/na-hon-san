import React, { useState, useRef, useEffect } from "react";
import "./UserCarousel.scss";
import { v4 } from "uuid";
import rArrow from "@images/RightArrow.svg";
import lArrow from "@images/LeftArrow.svg";
import { getPopUsers } from "@apis/feed";
import UserCarouselItem from "./UserCarouselItem";

function UserCarousel() {
  const [currentView, setCurrentView] = useState({
    total: 0,
    trans: 0
  });
  const [popUsers, setPopUsers] = useState([]);
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
    (async () => {
      const res = await getPopUsers();
      if (res.result === "SUCCESS") {
        setPopUsers(res.data);
      }
    })();
  }, []);
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
          {popUsers &&
            popUsers.map(info => {
              return (
                <div key={v4()}>
                  <UserCarouselItem info={info} />
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
