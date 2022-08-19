import React from "react";
import KakaoIcon from "@images/Kakao.svg";
import GoogleIcon from "@images/Google.svg";
import NaverIcon from "@images/Naver.svg";
import "./SocialSecion.scss";
import { useGoogleLogin, GoogleOAuthProvider } from "@react-oauth/google";
import {
  googleClientId,
  KAKAO_AUTH_URL,
  loginWithSocial,
  NAVER_AUTH_URL
} from "@apis/auth";
import { useAppDispatch } from "@store/hooks";
import { useNavigate } from "react-router-dom";
import { getUserInfo } from "@store/ducks/auth/authThunk";

function SocialCompo() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const startKakao = () => {
    window.location.href = KAKAO_AUTH_URL;
  };
  const startNaver = () => {
    window.location.href = NAVER_AUTH_URL;
  };
  const startGoogle = useGoogleLogin({
    onSuccess: async response => {
      const res = await loginWithSocial("google", response.access_token);
      await dispatch(getUserInfo());
      if (res.isRegist === "true") {
        navigate("/join/welcome");
      } else {
        navigate("/");
      }
    }
  });
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
      <button
        type="button"
        className="social__btn flex align-center justify-center google"
        onClick={() => startGoogle()}
      >
        <img className="social__img" src={GoogleIcon} alt="구글" />
        <p className="social__content notoMid fs-15">구글로 시작하기</p>
      </button>
    </section>
  );
}

function SocialSection() {
  return (
    <GoogleOAuthProvider clientId={googleClientId}>
      <SocialCompo />
    </GoogleOAuthProvider>
  );
}

export default SocialSection;
