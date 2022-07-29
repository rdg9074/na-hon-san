import React, { useState, useRef } from "react";
import "./ResetPw.scss";
import { passwordReg } from "@constants/reg";
import { useNavigate } from "react-router-dom";
import { resetPassword } from "@apis/auth";
import { useAppSelector } from "@store/hooks";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function ResetPw() {
  const [validPassword, setValidPassword] = useState<boolean>(true);
  const [samePassword, setSamePassword] = useState<boolean>(true);
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const passwordRef = useRef<HTMLInputElement>(null);
  const chkPasswordRef = useRef<HTMLInputElement>(null);

  const id = useAppSelector(state => state.auth.tmpId);

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

  const onResetPassword = async () => {
    if (!passwordRef.current?.value) {
      passwordRef.current?.focus();
    } else if (!chkPasswordRef.current?.value) {
      chkPasswordRef.current?.focus();
    } else if (validPassword && samePassword) {
      if (!isLoading) {
        setIsLoading(true);
        const res = await resetPassword(id, passwordRef.current.value);
        if (res === "SUCCESS") {
          navigate("/login");
        }
        setIsLoading(false);
      }
    }
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
            onChange={chkValidPassword}
            onBlur={chkSamePassword}
            ref={passwordRef}
            placeholder="비밀번호를 입력해주세요."
          />
          {validPassword ? (
            <div className="dummy" />
          ) : (
            <p className="password-msg fs-12 notoMid">
              숫자,대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요.
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
            onClick={onResetPassword}
          >
            {isLoading ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "비밀번호 재설정"
            )}
          </button>
        </main>
      </div>
    </div>
  );
}

export default ResetPw;
