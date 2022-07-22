import React from "react";
import KakaoIcon from "@images/Kakao.svg";
import GoogleIcon from "@images/Google.svg";
import NaverIcon from "@images/Naver.svg";
import "./SocialSecion.scss";
import {
  KAKAO_AUTH_URL,
  NAVER_AUTH_URL,
  googleClientId
} from "@store/ducks/auth/authThunk";
import GoogleLogin from "react-google-login";

function SocialSection() {
  const startKakao = () => {
    window.location.href = KAKAO_AUTH_URL;
  };
  const startNaver = () => {
    window.location.href = NAVER_AUTH_URL;
  };
  const startGoogle = (res: any) => {
    console.log(res);
  };
  return (
    <section className="social">
      <button
        type="button"
        className="social__btn flex align-center justify-center kakao"
        onClick={startKakao}
      >
        <img className="social__img" src={KakaoIcon} alt="카카오" />
        <p className="social__content notoMid fs-15">카카오로 시작하기</p>
      </button>
      <button
        type="button"
        className="social__btn flex align-center justify-center naver"
        onClick={startNaver}
      >
        <img className="social__img" src={NaverIcon} alt="네이버" />
        <p className="social__content notoMid fs-15">네이버로 시작하기</p>
      </button>
      <GoogleLogin
        clientId={googleClientId}
        onSuccess={startGoogle}
        className="simple-login-form__google-login-btn modal-box fs-16"
        render={renderProps => (
          <button
            type="button"
            className="social__btn flex align-center justify-center google"
            onClick={renderProps.onClick}
          >
            <img className="social__img" src={GoogleIcon} alt="구글" />
            <p className="social__content notoMid fs-15">구글로 시작하기</p>
          </button>
        )}
      />
    </section>
  );
}

export default SocialSection;
