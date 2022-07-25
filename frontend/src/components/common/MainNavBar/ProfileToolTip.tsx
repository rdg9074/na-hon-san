import React from "react";
import "./ProfileToolTip.scss";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "@store/hooks";
import { resetUserInfo } from "@store/ducks/auth/authSlice";

type ProfileToolTipProps = {
  userId: string;
};

function ProfileToolTip({ userId }: ProfileToolTipProps) {
  const dispatch = useAppDispatch();
  const navaigate = useNavigate();
  const onClickLogout = () => {
    dispatch(resetUserInfo());
    navaigate("/");
  };
  return (
    <div id="profile-tool-tip" className="">
      {/* 차후 나의피드 id로 라우팅 */}
      <Link className="content notoBold fs-16" to={`/userfeed/${userId}`}>
        마이페이지
      </Link>
      <button
        type="button"
        className="content notoBold fs-16"
        onClick={onClickLogout}
      >
        로그아웃
      </button>
    </div>
  );
}

export default ProfileToolTip;
