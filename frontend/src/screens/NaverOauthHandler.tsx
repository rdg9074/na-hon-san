import React, { useEffect } from "react";
import NaverIcon from "@images/Naver.svg";
import { useSearchParams } from "react-router-dom";
import { loginNaver } from "@store/ducks/auth/authThunk";
import MsgPageLayout from "./MsgPageLayout";

function NaverOauthHandler() {
  const [searchParams] = useSearchParams();
  useEffect(() => {
    const code = searchParams.get("code") as string;
    console.log(code);
    (async () => {
      loginNaver(code);
    })();
  }, []);
  return (
    <MsgPageLayout
      title="네이버 로그인 중입니다."
      subtitle="잠시만 기다려주세요"
      imgSrc={NaverIcon}
    />
  );
}

export default NaverOauthHandler;
