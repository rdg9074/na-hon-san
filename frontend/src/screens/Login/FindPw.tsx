import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./FindPw.scss";

function FindPw() {
  const [errMsg, setErrMsg] = useState(true);
  const navigate = useNavigate();
  const sendAuthCode = () => {
    navigate("chkEmail");
  };
  return (
    <div className="wrapper">
      <div id="find-pw">
        <header className="header">
          <p className="header__title notoBold fs-24">
            비밀번호를 까먹으셨나요?
          </p>
          <p className="header__sub-title notoRef fs-16">
            가입에 사용하였떤 이메일을 입력해주세요
          </p>
          <p className="header__sub-title notoRef fs-16">
            해당 이메일로 인증코드를 보내드릴게요.
          </p>
        </header>
        <main className="form">
          <p className="form__title notoReg fs-16">이메일</p>
          <input
            type="text"
            className="form__input notoReg fs-15"
            placeholder="이메일을 입력해주세요"
          />
          {errMsg ? (
            <p className="form__msg notoMid fs-12">가입된 이메일이 아닙니다.</p>
          ) : (
            <div className="form__dummy" />
          )}

          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={sendAuthCode}
          >
            인증번호 전송하기
          </button>
        </main>
      </div>{" "}
    </div>
  );
}

export default FindPw;
