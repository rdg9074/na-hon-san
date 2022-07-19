import React from "react";
import { Link } from "react-router-dom";
import "./MsgPageLayout.scss";

interface MsgPageLayoutProps {
  title: string;
  subtitle: string;
  imgSrc: string;
  // eslint-disable-next-line react/require-default-props
  subButton?: string;
}

function MsgPageLayout({
  title,
  subtitle,
  imgSrc,
  subButton
}: MsgPageLayoutProps) {
  return (
    <div id="msg-page-layout">
      <header className="header flex justify-center">
        <img className="header__img" src={imgSrc} alt="환영아이콘" />
      </header>
      <main className="main flex column justify-center">
        <div className="main__title notoBold fs-28 flex justify-center">
          {title}
        </div>
        <div className="main__sub-title notoBold fs-28 flex justify-center">
          {subtitle}
        </div>
      </main>
      <footer className="footer flex column align-center">
        <Link
          to="/"
          className="footer__btn flex align-center justify-center notoBold fs-16"
        >
          나혼자 잘살러 가기
        </Link>
        {subButton && (
          <Link
            to="/join/more"
            className="footer__btn flex align-center justify-center notoBold fs-16"
          >
            {subButton}
          </Link>
        )}
      </footer>
    </div>
  );
}

export default MsgPageLayout;
