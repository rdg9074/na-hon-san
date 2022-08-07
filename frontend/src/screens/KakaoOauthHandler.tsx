import React, { useEffect } from "react";
import KakaoIcon from "@images/Kakao.svg";
import { useSearchParams, useNavigate } from "react-router-dom";
import { getKakaoToken, loginWithSocial } from "@apis/auth";
import { useAppDispatch } from "@store/hooks";
import { getUserInfo } from "@store/ducks/auth/authThunk";
import MsgPageLayout from "./MsgPageLayout";

function KakaoOauthHandler() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  useEffect(() => {
    const code = searchParams.get("code") as string;

    (async () => {
      const token = await getKakaoToken(code);
      const res = await loginWithSocial("kakao", token);
      await dispatch(getUserInfo());
      if (res.isRegist === "true") {
        navigate("/join/welcome");
      } else {
        navigate("/");
      }
    })();
  }, []);
  return (
    <MsgPageLayout
      title="카카오톡 로그인 중입니다."
      subtitle="잠시만 기다려주세요"
      imgSrc={KakaoIcon}
    />
  );
}

export default KakaoOauthHandler;
