/* eslint-disable no-nested-ternary */
import React, { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./JoinDetail.scss";
import { passwordReg } from "@constants/reg";
import { useAppSelector } from "@store/hooks";
import { chkNickNameExist, join } from "@apis/auth";

type nickNameDupliType = "" | "err" | "success";

function JoinDetail() {
  const userId = useAppSelector(state => state.auth.userId);
  const navigate = useNavigate();
  const [validPassword, setValidPassword] = useState(true);
  const [samePassword, setSamePassword] = useState(true);
  const [nickNameDupli, setNickNameDupli] = useState<nickNameDupliType>("");

  const nickNameRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);
  const chkPasswordRef = useRef<HTMLInputElement>(null);

  const [form, setForm] = useState({
    nickName: "",
    password: ""
  });

  const submitUserInfo = async () => {
    if (nickNameDupli !== "success" || form.nickName === "") {
      nickNameRef.current?.focus();
      return;
    }
    if (!validPassword || form.password === "") {
      passwordRef.current?.focus();
      return;
    }
    if (!samePassword) {
      chkPasswordRef.current?.focus();
      return;
    }

    const res = await join(userId, form.password, form.nickName);
    if (res === "SUCCESS") {
      navigate("/join/welcome");
    }
  };

  const chkValidPassword = (e: React.ChangeEvent<HTMLInputElement>) => {
    setValidPassword(passwordReg.test(e.target.value));
  };

  const chkSamePassword = () => {
    if (chkPasswordRef.current && passwordRef.current)
      setSamePassword(
        chkPasswordRef.current.value === passwordRef.current.value
      );
  };
  const changeForm = (type: string, value: string) => {
    setForm({ ...form, [type]: value });
  };

  const chkNickNameDupli = async () => {
    if (nickNameRef.current?.value === "") {
      nickNameRef.current.focus();
      return;
    }
    const res = await chkNickNameExist(nickNameRef.current?.value as string);

    if (res === "SUCCESS") {
      setNickNameDupli("success");
      changeForm("nickName", nickNameRef.current?.value as string);
    } else {
      setNickNameDupli("err");
    }
  };

  return (
    <div id="join-detail">
      <header className="header">
        <p className="header__title notoBold fs-24">회원정보를 입력해주세요</p>
      </header>
      <form className="form">
        <p className="form__type notoBold fs-16">이메일</p>
        <input
          type="text"
          className="form__input fs-15 notoReg"
          defaultValue={userId}
          disabled
        />
        <p className="form__type notoBold fs-16">닉네임</p>
        <input
          type="text"
          className="form__input input-nickName fs-15 notoReg"
          placeholder="닉네임을 입력해주세요."
          autoComplete="nickname"
          ref={nickNameRef}
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
          autoComplete="new-password"
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
          autoComplete="new-password"
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
      </form>
    </div>
  );
}

export default JoinDetail;
