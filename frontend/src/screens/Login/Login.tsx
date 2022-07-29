import React, { useRef, useState } from "react";
import "./Login.scss";
import { login } from "@apis/auth";
import { Link, useNavigate } from "react-router-dom";
import SocialSection from "@components/common/SocialSection";
import { getUserInfo } from "@store/ducks/auth/authThunk";
import { useAppDispatch } from "@store/hooks";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function Login() {
  const [errMsg, setErrMsg] = useState<string>("");
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const idInputRef = useRef<HTMLInputElement>(null);
  const passwordInputRef = useRef<HTMLInputElement>(null);

  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const startLogin = async () => {
    if (!idInputRef.current?.value) {
      setErrMsg("아이디를 입력해주세요.");
      idInputRef.current?.focus();
    } else if (!passwordInputRef.current?.value) {
      setErrMsg("비밀번호를 입력해주세요.");
      passwordInputRef.current?.focus();
    } else if (!isLoading) {
      setIsLoading(true);
      const res = await login(
        idInputRef.current.value,
        passwordInputRef.current.value
      );
      if (res === "SUCCESS") {
        await dispatch(getUserInfo());
        navigate("/");
      } else {
        setErrMsg("아이디 또는 비밀번호가 일치하지 않습니다.");
      }
      setIsLoading(false);
    }
  };
  return (
    <div className="wrapper">
      <div id="login" className="wrapper">
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
          <p className="form__title notoReg fs-16">이메일</p>
          <input
            className="form__input notoReg fs-15"
            type="text"
            placeholder="이메일을 입력해주세요"
            ref={idInputRef}
          />
          <p className="form__title notoReg fs-16">비밀번호</p>
          <input
            className="form__input notoReg fs-15"
            type="password"
            placeholder="비밀번호를 입력해주세요"
            ref={passwordInputRef}
          />
          <p className="form__msg notoMid fs-12">{errMsg}</p>
          <button
            type="button"
            className="form__btn notoMid fs-15 flex align-center justify-center"
            onClick={startLogin}
          >
            {isLoading ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "로그인"
            )}
          </button>
        </section>
        <footer className="footer notoMid fs-12">
          <div className="footer__container flex align-center justify-center">
            <p className="footer__msg">아직 계정이 없으신가요?</p>
            <Link className="footer__link" to="/join">
              회원가입
            </Link>
          </div>
          <div className="footer__container flex align-center justify-center">
            <p className="footer__msg">이미 계정이 있으신가요?</p>
            <Link className="footer__link" to="/find/pw">
              비밀번호찾기
            </Link>
          </div>
        </footer>
      </div>
    </div>
  );
}

export default Login;
