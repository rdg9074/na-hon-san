import React from "react";
import "./ProfileToolTip.scss";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "@store/hooks";
import { resetUserInfo } from "@store/ducks/auth/authSlice";

type ProfileToolTipProps = {
  userNickname: string;
  closeTooltip: () => void;
};

function ProfileToolTip({ userNickname, closeTooltip }: ProfileToolTipProps) {
  const dispatch = useAppDispatch();
  const navaigate = useNavigate();
  const onClickLogout = () => {
    dispatch(resetUserInfo());
    navaigate("/");
    closeTooltip();
  };
  return (
    <div id="profile-tool-tip" className="">
      <Link
        className="content notoBold fs-16"
        to={`/userfeed/${userNickname}`}
        onClick={closeTooltip}
      >
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
