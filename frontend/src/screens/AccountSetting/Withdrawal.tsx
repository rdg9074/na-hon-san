import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./Withdrawal.scss";

function FindPw() {
  const [errMsg, setErrMsg] = useState(true);
  const navigate = useNavigate();
  const sendAuthCode = () => {
    setErrMsg(false);
    navigate(-1);
  };
  return (
    <div className="wrapper">
      <div id="chk-pw">
        <header className="header">
          <p className="header__title notoBold fs-24">
            우리 회원님 탈퇴하신다.
          </p>
          <p className="header__sub-title notoReg fs-16">
            회원 탈퇴 페이지입니다.
          </p>
          <p className="header__sub-title notoReg fs-16">
            아래 문구를 정확히 입력해주세요.
          </p>
        </header>
        <main className="form">
          <p className="form__title notoReg fs-16">누 가뭐 래도탈 퇴할거 야</p>
          <input
            type="text"
            className="form__input notoReg fs-15"
            placeholder="탈퇴 문구를 입력해주세요"
          />
          {errMsg ? (
            <p className="form__msg notoMid fs-12">일치하지 않습니다.</p>
          ) : (
            <div className="form__dummy" />
          )}

          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={sendAuthCode}
          >
            확인
          </button>
          <br />
          <button
            type="button"
            className="form__btn cancle notoMid fs-16"
            onClick={sendAuthCode}
          >
            취소
          </button>
        </main>
      </div>{" "}
    </div>
  );
}

export default FindPw;
