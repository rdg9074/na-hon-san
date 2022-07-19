import React, { useEffect } from "react";
import KakaoIcon from "@images/Kakao.svg";
import { useSearchParams } from "react-router-dom";
import { loginKakao } from "@store/ducks/auth/authThunk";
import MsgPageLayout from "./MsgPageLayout";

function KakaoOauthHandler() {
  const [searchParams] = useSearchParams();
  useEffect(() => {
    const code = searchParams.get("code") as string;

    (async () => {
      loginKakao(code);
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
