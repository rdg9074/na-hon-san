import React, { useState } from "react";
import "./AccountSettingPage.scss";
import { Link, useNavigate } from "react-router-dom";
import ThumDummy from "@images/ThumnailDummy.jpg";
import UserDummyIcon from "@images/UserDummy.svg";
import { v4 } from "uuid";

function AccountSettingPage() {
  const [errMsg] = useState(true);
  const navigate = useNavigate();
  const noti = ["좋아요", "팔로잉", "댓글", "대댓글"];
  const followState = ["팔로우", "팔로잉"];
  const goBack = () => {
    navigate(-1);
  };
  const goWithdrawal = () => {
    navigate("/account/withdrawal", { replace: true });
  };
  const [notiList, setNotiList] = useState([
    false,
    false,
    false,
    false,
    false,
    false
  ]);

  return (
    <div id="accountSetting-page">
      <div className="setprofile notoMid fs-36">
        <div className="setprofile__title">프로필 설정</div>
        <div className="img-container flex column">
          <div className="back-container flex">
            <img className="img-container__back" src={ThumDummy} alt="dum" />
            <p className="img-container__back-add">+</p>
          </div>
          <div className="user-container flex">
            <img
              className="img-container__user"
              src={UserDummyIcon}
              alt="dumm"
            />
            <p className="img-container__user-add">+</p>
          </div>
        </div>
        <div className="text-wrapper flex column">
          <p>UserName</p>
          <textarea
            className="state notoReg"
            maxLength={100}
            readOnly
            value="Lorem ipsum dolor sit amet, consectetur adipisicing elit. Sapiente
              eos mollitia qui dolores sed facilis quidem voluptate"
          >
            s
          </textarea>
        </div>
      </div>
      <div className="setaccount">
        <div className="title notoMid fs-36 p-none">계정 설정</div>
        <div className="main">
          <div className="main-account__title flex">
            <p className="fs-24 notoMid">회원정보 수정</p>
            <button onClick={goWithdrawal} type="button" className="fs-12">
              회원탈퇴
            </button>
          </div>
          <div className="main-account flex column">
            <div className="main-account__nickname flex">
              <p className="fs-16 notoReg">닉네임</p>
              <div>
                <input type="text" />
                {errMsg ? (
                  <p className="notoReg">중복된 닉네임 입니다.</p>
                ) : null}
              </div>
              <button className="notoMid fs-12" type="button">
                중복확인
              </button>
            </div>
            <div className="main-account__addinfo flex">
              <p className="fs-16 notoReg">지역·태그</p>
              <Link to="/join/more" className="notoMid fs-12">
                변경하기
              </Link>
              <div> </div>
            </div>
            <div className="main-account__addinfo flex">
              <p className="fs-16 notoReg">비밀번호</p>
              <Link to="/reset/pw" className="notoMid fs-12">
                변경하기
              </Link>
              <div> </div>
            </div>
          </div>
          <div className="main-noti__title fs-24 notoMid">알림 설정</div>
          <div className="main-noti__toggle flex">
            {noti.map((title, idx) => {
              return (
                <div className="main-noti__toggle-item flex" key={v4()}>
                  <p className="notoReg fs-14">{title}</p>
                  <button
                    onClick={() => {
                      setNotiList(
                        notiList.map((state, num) => {
                          if (num === idx) {
                            return !state;
                          }
                          return state;
                        })
                      );
                    }}
                    type="button"
                    className={`main-noti__toggle-btn flex ${
                      notiList[idx] ? "justify-end" : null
                    }`}
                  >
                    <div
                      className={
                        notiList[idx] ? "active-toggle-bar" : "toggle-bar"
                      }
                    >
                      {null}
                    </div>
                    <div
                      className={
                        notiList[idx] ? "active-toggle-circle" : "toggle-circle"
                      }
                    >
                      {null}
                    </div>
                  </button>
                </div>
              );
            })}
          </div>
          <div className="main-noti__title fs-24 notoMid">공개 설정</div>
          <div className="main-noti__toggle flex">
            {followState.map((title, idx) => {
              const idxx = idx + 4;
              return (
                <div className="main-noti__toggle-item flex" key={v4()}>
                  <p className="notoReg fs-14">{title}</p>
                  <button
                    onClick={() => {
                      setNotiList(
                        notiList.map((state, num) => {
                          if (num === idxx) {
                            return !state;
                          }
                          return state;
                        })
                      );
                    }}
                    type="button"
                    className={`main-noti__toggle-btn flex ${
                      notiList[idxx] ? "justify-end" : null
                    }`}
                  >
                    <div
                      className={
                        notiList[idxx] ? "active-toggle-bar" : "toggle-bar"
                      }
                    >
                      {null}
                    </div>
                    <div
                      className={
                        notiList[idxx]
                          ? "active-toggle-circle"
                          : "toggle-circle"
                      }
                    >
                      {null}
                    </div>
                  </button>
                </div>
              );
            })}
          </div>
        </div>
      </div>
      <div className="setaccount-submit flex fs-24 notoMid">
        <button className="setaccount-submit__submit" type="button">
          설정
        </button>
        <button
          onClick={goBack}
          className="setaccount-submit__cancle"
          type="button"
        >
          취소
        </button>
      </div>
    </div>
  );
}

export default AccountSettingPage;
