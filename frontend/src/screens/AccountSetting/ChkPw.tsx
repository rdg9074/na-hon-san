import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ChkPw.scss";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import { passwordChk } from "@apis/setAccount";

function FindPw() {
  const [errMsg, setErrMsg] = useState(false);
  const [spinner, setSpinner] = useState(false);
  const navigate = useNavigate();
  const inputRef = useRef<HTMLInputElement>(null);
  const sendAuthCode = async () => {
    if (inputRef.current) {
      const password = inputRef.current.value;
      if (!password) {
        inputRef.current.focus();
        return;
      }
      setSpinner(true);
      const res = await passwordChk(password);
      if (res === "SUCCESS") {
        navigate("/account/set", { replace: true });
      } else {
        setErrMsg(true);
        setSpinner(false);
      }
    }
  };
  return (
    <div className="wrapper">
      <div id="chk-pw">
        <header className="header">
          <p className="header__title notoBold fs-24">
            본인 인증을 완료해주세요.
          </p>
          <p className="header__sub-title notoReg fs-16">
            설정을 위해 본인 인증이 필요해요.
          </p>
          <p className="header__sub-title notoReg fs-16">
            비밀번호를 입력해주세요.
          </p>
        </header>
        <main className="form">
          <p className="form__title notoReg fs-16">비밀번호</p>
          <input
            type="password"
            className="form__input notoReg fs-15"
            placeholder="비밀번호를 입력해주세요"
            ref={inputRef}
          />
          {errMsg ? (
            <p className="form__msg notoMid fs-12">
              비밀번호가 일치하지 않습니다.
            </p>
          ) : (
            <div className="form__dummy" />
          )}

          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={sendAuthCode}
          >
            {spinner ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "확인"
            )}
          </button>
        </main>
      </div>
    </div>
  );
}

export default FindPw;
