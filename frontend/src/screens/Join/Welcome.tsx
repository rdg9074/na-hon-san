import React from "react";
import MsgPageLayout from "@screens/MsgPageLayout";
import WelcomeIcon from "@images/Welcome.svg";
import { useAppSelector } from "@store/hooks";

function Welcome() {
  const nickname = useAppSelector(state => state.auth.userInfo?.nickname);
  return (
    <div className="wrapper">
      <MsgPageLayout
        title={`${nickname}님, 환영합니다`}
        subtitle="회원가입을 완료하였습니다."
        imgSrc={WelcomeIcon}
        subButton="추가정보 입력하기"
      />
    </div>
  );
}

export default Welcome;
