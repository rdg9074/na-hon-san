import { sendAuthCode } from "@apis/auth";
import { setTmpId } from "@store/ducks/auth/authSlice";
import { useAppDispatch } from "@store/hooks";
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./FindPw.scss";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function FindPw() {
  const [errMsg, setErrMsg] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const idInputRef = useRef<HTMLInputElement>(null);

  const submitAuthCode = async () => {
    if (!idInputRef.current?.value) {
      idInputRef.current?.focus();
      setErrMsg("이메일을 입력해주세요.");
    } else if (!isLoading) {
      setIsLoading(true);
      const tmpId = idInputRef.current.value;
      const res = await sendAuthCode(tmpId, 1);
      if (res === "SUCCESS") {
        dispatch(setTmpId({ tmpId }));
        navigate("chkEmail");
      } else {
        setErrMsg("가입된 이메일이 아닙니다.");
      }
      setIsLoading(false);
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
            {isLoading ? (
              <img
                src={LoadingSpinner}
                alt="로딩스피너"
                className="loading-spinner"
              />
            ) : (
              "인증번호 전송하기"
            )}
          </button>
        </main>
      </div>{" "}
    </div>
  );
}

export default FindPw;
