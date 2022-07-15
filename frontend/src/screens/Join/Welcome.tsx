import React from "react";
import "./Welcome.scss";
import WelcomeIcon from "@images/Welcome.svg";
import { Link } from "react-router-dom";

function Welcome() {
  return (
    <div id="welcome">
      <header className="header flex justify-center">
        <img className="header__img" src={WelcomeIcon} alt="환영아이콘" />
      </header>
      <main className="main flex column justify-center">
        <div className="main__title notoBold fs-28 flex justify-center">
          염진호님, 환영합니다.
        </div>
        <div className="main__title notoBold fs-28 flex justify-center">
          회원가입을 완료하였습니다!
        </div>
      </main>
      <footer className="footer flex justify-center">
        <Link
          to="/"
          className="footer__btn flex align-center justify-center notoBold fs-16"
        >
          나혼자 잘살러 가기
        </Link>
      </footer>
    </div>
  );
}

export default Welcome;
