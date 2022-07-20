import React from "react";
import "./Letter.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import { Link } from "react-router-dom";

function Letter() {
  return (
    <Link to="detail?send=진호&recv=최강">
      <div id="letter" className="flex align-center">
        <img src={UserDummyIcon} alt="유저더미" className="letter__user-img" />
        <div className="flex column main-content ellipsis">
          <p className="letter__user-nick-name notoReg">최강</p>
          <p className="letter__desc  notoMid ellipsis">
            연봉 7천 갈끄니까~~~~~~~~~~~~!@#!#!@#1231312313
          </p>
        </div>
        <p className="letter__time notoReg flex">12분전</p>
      </div>
    </Link>
  );
}

export default Letter;
