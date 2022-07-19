import React, { useState, useRef } from "react";
import "./ResetPw.scss";
import { passwordReg } from "@constants/reg";
import { useNavigate } from "react-router-dom";

function ResetPw() {
  const [validPassword, setValidPassword] = useState(false);
  const [samePassword, setSamePassword] = useState(false);
  const [password, setPassword] = useState("");

  const passwordRef = useRef<HTMLInputElement>(null);
  const chkPasswordRef = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();
  const chkValidPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValidPassword(passwordReg.test(e.target.value));
  };

  const chkSamePassword = () => {
    if (chkPasswordRef.current && passwordRef.current)
      setSamePassword(
        chkPasswordRef.current.value === passwordRef.current.value
      );
  };

  const resetPassword = () => {
    navigate("/login");
  };
  return (
    <div className="wrapper">
      <div id="reset-pw">
        <header className="header">
          <p className="header__title notoBold fs-24">
            비밀번호 재설정을 진행해주세요.
          </p>
        </header>
        <main className="form">
          <p className="form__type notoBold fs-16">비밀번호</p>
          <input
            type="password"
            className="form__input fs-15 notoReg password"
            onChange={e => {
              chkValidPassword(e);
              setPassword(e.target.value);
            }}
            onBlur={chkSamePassword}
            ref={passwordRef}
            placeholder="비밀번호를 입력해주세요."
          />
          {validPassword ? (
            <div className="dummy" />
          ) : (
            <p className="password-msg fs-12 notoMid">
              대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요.
            </p>
          )}

          <p className="form__type notoBold fs-16">비밀번호확인</p>
          <input
            type="password"
            className="form__input fs-15 notoReg password-chk"
            onChange={chkSamePassword}
            ref={chkPasswordRef}
            placeholder="비밀번호를 다시 한번 입력해주세요."
          />
          {samePassword ? (
            <div className="dummy" />
          ) : (
            <p className="password-chk-msg fs-12 notoMid">
              비밀번호가 일치하지 않습니다.
            </p>
          )}
          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={resetPassword}
          >
            비밀번호 재설정
          </button>
        </main>
      </div>
    </div>
  );
}

export default ResetPw;
