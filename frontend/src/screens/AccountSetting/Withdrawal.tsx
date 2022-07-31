import React, { useState, useRef } from "react";
import { useNavigate } from "react-router-dom";
import "./Withdrawal.scss";
import { withdrawal } from "@apis/setAccount";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import { useAppDispatch } from "@store/hooks";
import { resetUserInfo } from "@store/ducks/auth/authSlice";

function FindPw() {
  const inputRef = useRef<HTMLInputElement>(null);
  const [errMsg, setErrMsg] = useState(false);
  const [spinner, setSpinner] = useState(false);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const goBack = () => {
    navigate(-1);
  };

  const isPaste = (e: React.ClipboardEvent<HTMLInputElement>) => {
    e.preventDefault();
  };
  const sendWithdrawal = async () => {
    if (
      inputRef.current &&
      inputRef.current.value === "누 가뭐 래도탈 퇴할거 야"
    ) {
      setSpinner(true);
      const res = await withdrawal();
      if (res === "SUCCESS") {
        dispatch(resetUserInfo());
        navigate("/");
      }
      return;
    }
    if (inputRef.current) {
      inputRef.current.value = "";
      setErrMsg(true);
    }
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
            onPaste={e => isPaste(e)}
            ref={inputRef}
          />
          {errMsg ? (
            <p className="form__msg notoMid fs-12">일치하지 않습니다.</p>
          ) : (
            <div className="form__dummy" />
          )}

          <button
            type="button"
            className="form__btn notoMid fs-16"
            onClick={sendWithdrawal}
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
          <br />
          <button
            type="button"
            className="form__btn cancle notoMid fs-16"
            onClick={goBack}
          >
            취소
          </button>
        </main>
      </div>
    </div>
  );
}

export default FindPw;
