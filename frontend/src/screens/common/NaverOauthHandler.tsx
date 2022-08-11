import React, { useEffect } from "react";
import NaverIcon from "@images/Naver.svg";
import { useSearchParams, useNavigate } from "react-router-dom";
import { loginWithSocial } from "@apis/auth";
import { useAppDispatch } from "@store/hooks";
import { getUserInfo } from "@store/ducks/auth/authThunk";
import MsgPageLayout from "./MsgPageLayout";

function NaverOauthHandler() {
  const dispatch = useAppDispatch();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  useEffect(() => {
    const code = searchParams.get("code") as string;
    (async () => {
      const res = await loginWithSocial("naver", code);
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
      title="네이버 로그인 중입니다."
      subtitle="잠시만 기다려주세요"
      imgSrc={NaverIcon}
    />
  );
}

export default NaverOauthHandler;
