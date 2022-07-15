/* eslint-disable no-nested-ternary */
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./JoinDetail.scss";

function JoinDetail() {
  const email = "JinhoJJANG@gmail.com";
  const navigate = useNavigate();
  const [validPassword, setValidPassword] = useState(true);
  const [samePassword, setSamePassword] = useState(true);
  const [nickNameDupli, setNickNameDupli] = useState("");
  const passwordRef = useRef<HTMLInputElement>(null);
  const chkPasswordRef = useRef<HTMLInputElement>(null);
  const [form, setForm] = useState({
    email,
    nickName: "",
    password: "",
    chkPassword: ""
  });

  const submitUserInfo = () => navigate("/join/more");

  const chkValidPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    const reg =
      /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
    setValidPassword(reg.test(e.target.value));
  };

  const chkSamePassword = () => {
    if (chkPasswordRef.current && passwordRef.current)
      setSamePassword(
        chkPasswordRef.current.value === passwordRef.current.value
      );
  };

  const chkNickNameDupli = () => {
    if (nickNameDupli === "") {
      setNickNameDupli("err");
    } else if (nickNameDupli === "err") {
      setNickNameDupli("success");
    } else {
      setNickNameDupli("");
    }
  };

  const changeForm = (type: string, value: string) => {
    setForm({ ...form, [type]: value });
  };

  return (
    <div id="join-detail">
      <header className="header">
        <p className="header__title notoBold fs-24">회원정보를 입력해주세요</p>
      </header>
      <main className="form">
        <p className="form__type notoBold fs-16">이메일</p>
        <input
          type="text"
          className="form__input fs-15 notoReg"
          defaultValue={email}
          disabled
        />
        <p className="form__type notoBold fs-16">닉네임</p>
        <input
          type="text"
          className="form__input input-nickName fs-15 notoReg"
          placeholder="닉네임을 입력해주세요."
        />
        <button
          type="button"
          className="form__btn--dupli notoReg fs-12 inline-block"
          onClick={chkNickNameDupli}
        >
          중복확인
        </button>
        {nickNameDupli === "" ? (
          <div className="dummy" />
        ) : nickNameDupli === "err" ? (
          <p className="password-msg fs-12 notoMid">
            이미 사용중인 닉네임입니다.
          </p>
        ) : (
          <p className="password-msg success fs-12 notoMid">
            사용가능한 닉네임입니다.
          </p>
        )}
        <p className="form__type notoBold fs-16">비밀번호</p>
        <input
          type="password"
          className="form__input fs-15 notoReg password"
          onChange={e => {
            chkValidPassword(e);
            changeForm("password", e.target.value);
          }}
          onBlur={chkSamePassword}
          ref={passwordRef}
          placeholder="비밀번호를 입력해주세요."
        />
        {validPassword ? (
          <div className="dummy" />
        ) : (
          <p className="password-msg fs-12 notoMid">
            대소문자,특수문자를 혼합하여 8~16자리로 입력해주세요.
          </p>
        )}

        <p className="form__type notoBold fs-16">비밀번호확인</p>
        <input
          type="password"
          className="form__input fs-15 notoReg password-chk"
          onChange={chkSamePassword}
          ref={chkPasswordRef}
          placeholder="비밀번호를 다시 한번 입력해주세요."
        />
        {samePassword ? (
          <div className="dummy" />
        ) : (
          <p className="password-chk-msg fs-12 notoMid">
            비밀번호가 일치하지 않습니다.
          </p>
        )}

        <button
          type="button"
          className="form__btn flex align-center justify-center notoMid fs-16"
          onClick={submitUserInfo}
        >
          다음
        </button>
      </main>
    </div>
  );
}

export default JoinDetail;
