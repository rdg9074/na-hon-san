import React from "react";
import { useNavigate } from "react-router-dom";
import "./ChkEmail.scss";

function ChkEmail() {
  const navigate = useNavigate();
  const chkCode = () => {
    navigate("/join/detail");
  };
  return (
    <div id="chk-email">
      <header className="header">
        <p className="header__title notoBold fs-24">
          인증 이메일을 보냈습니다.
        </p>
        <p className="header__sub-title notoReg fs-16">
          jinhoJJANG@gamile.com 에서 이메일을 확인 후 인증코드를 입력해주세요!
        </p>
      </header>
      <main className="form">
        <p className="form__title notoMid fs-16">인증코드</p>
        <input
          type="text"
          className="form__input notoReg fs-15"
          placeholder="인증번호를 입력해주세요"
        />
        <p className="form__msg notoMid fs-12">인증코드가 올바르지 않습니다.</p>
        <button
          className="form__btn notoMid fs-16 flex align-center justify-center"
          type="button"
          onClick={chkCode}
        >
          다음
        </button>
      </main>
      <footer className="footer">
        <div className="footer-top flex align-center justify-center">
          <p className="footer-top__msg notoMid fs-12">
            인증 이메일을 받지 못하셨나요?
          </p>
          <button className="footer-top__btn notoBold fs-12" type="button">
            이메일 재전송
          </button>
        </div>
        <div className="footer-bottom">
          <ul className="notice-ul notoMid fs-12">
            <li className="notice-ul__li">
              인증 이메일은 발송 시점으로 부터 3분간 유효합니다.
            </li>
            <li className="notice-ul__li">
              인증 이메일 재발송 시 기존 인증코드는 무효처리되며, 새로 받은
              인증코드로 인증해야 합니다.
            </li>
            <li className="notice-ul__li">
              이메일이 도착하지 않았다면 스팸메일함을 확인해주세요.
            </li>
          </ul>
        </div>
      </footer>
    </div>
  );
}

export default ChkEmail;
