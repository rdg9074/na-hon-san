import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "./ChkPw.scss";

function FindPw() {
  const [errMsg, setErrMsg] = useState(true);
  const navigate = useNavigate();
  const sendAuthCode = () => {
    setErrMsg(false);
    navigate("set", { replace: true });
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
            type="text"
            className="form__input notoReg fs-15"
            placeholder="비밀번호를 입력해주세요"
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
        </main>
      </div>{" "}
    </div>
  );
}

export default FindPw;
