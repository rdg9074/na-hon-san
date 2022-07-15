import React from "react";
import "./ProfileToolTip.scss";
import { Link } from "react-router-dom";

function ProfileToolTip() {
  return (
    <div id="profile-tool-tip" className="">
      {/* 차후 나의피드 id로 라우팅 */}
      <Link className="content notoBold fs-16" to="/">
        마이페이지
      </Link>
      <button type="button" className="content notoBold fs-16">
        로그아웃
      </button>
    </div>
  );
}

export default ProfileToolTip;
