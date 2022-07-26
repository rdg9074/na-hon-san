import React from "react";
import "./News.scss";
import ImgIcon from "@images/ImgIcon.svg";

function News() {
  return (
    <div id="news">
      <div className="item flex">
        <div className="item__img flex">
          <img src={ImgIcon} alt="thumnail" />
        </div>
        <div className="item__content">
          <div className="item__content__head flex justify-space-between">
            <p className="item__content__head__title notoReg">title</p>
            <p className="item__content__head__date notoReg">date</p>
          </div>
          <p className="item__content__description flex align-center column notoReg">
            Lorem ipsum dolor sit amet consectetur adipisicing elit. Quibusdam
            quam obcaecati odit, unde facere a voluptatum dignissimos saepe odio
            rem modi cumque cupiditate tempore quos autem provident ab nam
            porro.Lorem ipsum dolor sit amet consectetur adipisicing elit.
            Quibusdam quam obcaecati odit, unde facere a voluptatum dignissimos
            saepe odio rem modi cumque cupiditate tempore quos autem provident
            ab nam porro.
          </p>
        </div>
      </div>
    </div>
  );
}

export default News;
