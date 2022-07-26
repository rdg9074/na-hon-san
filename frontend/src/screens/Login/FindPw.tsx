import { sendAuthCode } from "@apis/auth";
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./FindPw.scss";

function FindPw() {
  const [errMsg, setErrMsg] = useState<string>("");
  const navigate = useNavigate();
  const idInputRef = useRef<HTMLInputElement>(null);

  const submitAuthCode = async () => {
    if (!idInputRef.current?.value) {
      idInputRef.current?.focus();
      setErrMsg("이메일을 입력해주세요.");
    } else {
      const res = await sendAuthCode(idInputRef.current.value, 1);
      if (res === "SUCCESS") {
        navigate("chkEmail");
      } else {
        setErrMsg("가입된 이메일이 아닙니다.");
      }
    }
  };
  return (
    <div className="wrapper">
      <div id="find-pw">
        <header className="header">
          <p className="header__title notoBold fs-24">
            비밀번호를 까먹으셨나요?
          </p>
          <p className="header__sub-title notoReg fs-16">
            가입에 사용하였떤 이메일을 입력해주세요
          </p>
          <p className="header__sub-title notoReg fs-16">
            해당 이메일로 인증코드를 보내드릴게요.
          </p>
        </header>
        <main className="form">
          <p className="form__title notoReg fs-16">이메일</p>
          <input
            type="text"
            className="form__input notoReg fs-15"
            placeholder="이메일을 입력해주세요"
            ref={idInputRef}
          />
          {errMsg !== "" ? (
            <p className="form__msg notoMid fs-12">{errMsg}</p>
          ) : (
            <div className="form__dummy" />
          )}

          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={submitAuthCode}
          >
            인증번호 전송하기
          </button>
        </main>
      </div>{" "}
    </div>
  );
}

export default FindPw;
