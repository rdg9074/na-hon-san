import React, { useEffect, useState } from "react";
import { Link, NavLink } from "react-router-dom";
import HeaderLogoImg from "@images/HeaderLogo.svg";
import "./MainNavBar.scss";
import AlarmIcon from "@images/Alarm.svg";
import MsgIcon from "@images/Msg.svg";
import UserDummyIcon from "@images/UserDummy.svg";
import MenuIcon from "@images/Menu.svg";
import { useAppSelector } from "@store/hooks";
import AlarmToolTip from "./MainNavBar/AlarmToolTip";
import ProfileToolTip from "./MainNavBar/ProfileToolTip";

function MainNavBar() {
  const [alarmVisible, setAlarmVisible] = useState(false);
  const [profileTooltipVisible, setProfileTooltipVisible] = useState(false);
  const activeTabClassName = (active: boolean) => {
    const prefix = "left-nav__link fs-20 btn--";
    return active ? `${prefix}active` : `${prefix}unactive`;
  };
  const userInfo = useAppSelector(state => state.auth.userInfo);

  return (
    <div className="nav-wrapper">
      <nav id="main-nav-bar" className="flex align-center">
        <img className="logo" alt="나혼자잘산다로고" src={HeaderLogoImg} />
        <p className="title notoBold fs-28 desktopOnly">나혼자잘산다</p>
        <div className="nav-container flex justify-space-between align-center">
          <nav className="left-nav notoBold">
            <NavLink
              className={({ isActive }) => activeTabClassName(isActive)}
              to="/"
            >
              홈
            </NavLink>
            <NavLink
              className={({ isActive }) => activeTabClassName(isActive)}
              to="/tip"
            >
              꿀팁
            </NavLink>
            <NavLink
              className={({ isActive }) => activeTabClassName(isActive)}
              to="/feed"
            >
              피드
            </NavLink>
            <NavLink
              className={({ isActive }) => activeTabClassName(isActive)}
              to="/deal"
            >
              꿀딜
            </NavLink>
            <NavLink
              className={({ isActive }) => activeTabClassName(isActive)}
              to="/news"
            >
              뉴스
            </NavLink>
          </nav>
          <nav className="right-nav notoReg flex align-center">
            {userInfo ? (
              <>
                <button
                  type="button"
                  className="right-nav__link alarm"
                  onClick={() => setAlarmVisible(!alarmVisible)}
                >
                  <p className="alarm__cnt fs-8 flex align-center justify-center">
                    123
                  </p>
                  <img className="alarm__icon" src={AlarmIcon} alt="알림" />
                  {alarmVisible && <AlarmToolTip />}
                </button>
                <Link className="right-nav__link alarm" to="/letters">
                  <p className="alarm__cnt fs-8 flex align-center justify-center">
                    1
                  </p>
                  <img className="alarm__icon" src={MsgIcon} alt="쪽지함" />
                </Link>
                <button
                  type="button"
                  className="right-nav__link profile fs-16"
                  onClick={() =>
                    setProfileTooltipVisible(!profileTooltipVisible)
                  }
                >
                  <img className="user" src={UserDummyIcon} alt="더미유저" />
                </button>
                <div className="profile">
                  {profileTooltipVisible && <ProfileToolTip userId="123" />}
                </div>
              </>
            ) : (
              <>
                <Link className="right-nav__link fs-16" to="/login">
                  로그인
                </Link>
                <Link className="right-nav__link fs-16" to="/join">
                  회원가입
                </Link>
              </>
            )}
          </nav>
          <img className="phoneOnly menu-icon" src={MenuIcon} alt="더보기" />
        </div>
      </nav>
    </div>
  );
}

export default MainNavBar;
