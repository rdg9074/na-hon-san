import React from "react";
import "./Login.scss";
import KakaoIcon from "@images/Kakao.svg";
import GoogleIcon from "@images/Google.svg";
import NaverIcon from "@images/Naver.svg";
import { Link, useNavigate } from "react-router-dom";

function Login() {
  return (
    <div id="login">
      <header className="header">
        <p className="header__title notoBold fs-24">
          환영합니다! <br />
          나혼자 잘살아 봐요!
        </p>
      </header>
      <section className="social">
        <button
          type="button"
          className="social__btn flex align-center justify-center kakao"
        >
          <img className="social__img" src={KakaoIcon} alt="카카오" />
          <p className="social__content notoMid fs-15">카카오로 시작하기</p>
        </button>
        <button
          type="button"
          className="social__btn flex align-center justify-center naver"
        >
          <img className="social__img" src={NaverIcon} alt="네이버" />
          <p className="social__content notoMid fs-15">네이버로 시작하기</p>
        </button>
        <button
          type="button"
          className="social__btn flex align-center justify-center google"
        >
          <img className="social__img" src={GoogleIcon} alt="구글" />
          <p className="social__content notoMid fs-15">구글로 시작하기</p>
        </button>
      </section>
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
        />
        <p className="form__title notoReg fs-16">비밀번호</p>
        <input
          className="form__input notoReg fs-15"
          type="password"
          placeholder="비밀번호를 입력해주세요"
        />
        <p className="form__msg notoMid fs-12">
          아이디 또는 비밀번호가 일치하지 않습니다.
        </p>
        <button
          type="button"
          className="form__btn notoMid fs-15 flex align-center justify-center"
        >
          로그인
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
  );
}

export default Login;
