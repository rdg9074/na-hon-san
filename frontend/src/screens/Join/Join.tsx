import React, { useState, useRef } from "react";
import "./Join.scss";
import { Link, useNavigate } from "react-router-dom";
import { emailReg } from "@constants/reg";
import { sendAuthCode } from "@apis/auth";
import { useDispatch } from "react-redux";
import { setTmpId } from "@store/ducks/auth/authSlice";
import SocialSection from "@components/common/SocialSection";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function Join() {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const [errMsg, setErrMsg] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const inputRef = useRef<HTMLInputElement>(null);

  const chkValidate = async () => {
    if (inputRef.current) {
      if (inputRef.current?.value === "") {
        inputRef.current?.focus();
        setErrMsg("이메일을 입력해주세요.");
        return;
      }
      if (errMsg === "이메일 형식을 확인해주세요.") {
        inputRef.current?.focus();
        return;
      }
      const userId = inputRef.current?.value;
      if (!isLoading) {
        setIsLoading(true);
        const res = await sendAuthCode(userId, 0);
        if (res === "FAIL") {
          setErrMsg("이미 존재하는 아이디입니다.");
          inputRef.current?.focus();
        } else if (res === "SUCCESS") {
          dispatch(setTmpId({ tmpId: userId }));
          navigate("chkEmail");
        }
        setIsLoading(false);
      }
    }
  };

  const chkEmailReg = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      chkValidate();
      return;
    }
    if (emailReg.test(e.target.value)) {
      setErrMsg("");
    } else {
      setErrMsg("이메일 형식을 확인해주세요.");
    }
  };
  return (
    <div className="wrapper">
      <div id="join">
        <header className="header">
          <p className="header__title notoBold fs-24">
            환영합니다! <br />
            나혼자 잘살아 봐요!
          </p>
        </header>
        <SocialSection />
        <div className="or flex align-center justify-center">
          <span className="or__line" />
          <p className="or__title notoBold fs-14">또는</p>
          <span className="or__line" />
        </div>
        <section className="form">
          <p className="form__title notoReg fs-16">이메일로 회원가입하기</p>
          <input
            className="form__input notoReg fs-15"
            type="text"
            placeholder="이메일을 입력해주세요."
            onKeyUp={chkEmailReg}
            ref={inputRef}
          />
          {errMsg === "" ? (
            <p className="dummy" />
          ) : (
            <p className="form__msg notoMid fs-12">{errMsg}</p>
          )}

          <button
            type="button"
            className="form__btn notoMid fs-15 flex align-center justify-center"
            onClick={chkValidate}
          >
            {isLoading ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "다음"
            )}
          </button>
        </section>
        <footer className="footer notoMid fs-12 flex align-center justify-center">
          <p className="footer__msg">이미 계정이 있으신가요?</p>
          <Link className="footer__link" to="/login">
            로그인
          </Link>
        </footer>
      </div>
    </div>
  );
}

export default Join;
