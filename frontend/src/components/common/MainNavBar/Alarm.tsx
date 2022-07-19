import React from "react";
import UserDummyIcon from "@images/UserDummy.svg";
import CloseIcon from "@images/X.svg";
import "./Alarm.scss";

function Alarm() {
  return (
    <div id="alarm" className="flex align-center">
      <img src={UserDummyIcon} alt="유저더미" className="user-icon" />
      <p className="content notoReg fs-14">
        최강님이 회원님의 글을 좋아합니다.
      </p>
      <div className="right flex column">
        <img src={CloseIcon} alt="삭제아이콘" className="right__close-icon" />
        <p className="right__time fs-8">00분전</p>
      </div>
    </div>
  );
}

export default Alarm;
