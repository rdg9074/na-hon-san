import { useAppSelector } from "@store/hooks";
import React, {
  useState,
  useEffect,
  Dispatch,
  SetStateAction,
  useRef
} from "react";
import { useNavigate } from "react-router-dom";
import { chkAuthCode, sendAuthCode } from "@apis/auth";
import "./ChkEmail.scss";
import { v4 } from "uuid";
import LoadingSpinner from "@images/LoadingSpinner.svg";

type ChkEmailProps = {
  type: "findPw" | "login";
};

type TimerProps = {
  setErrMsg: Dispatch<SetStateAction<string>>;
  initTime: number;
  timerKey: string;
};

function Timer({ setErrMsg, initTime, timerKey }: TimerProps) {
  const [timer, setTimer] = useState<ReturnType<typeof setTimeout> | null>();
  const [time, setTime] = useState<number>(initTime);

  useEffect(() => {
    if (time !== 0 && !timer) {
      const timerId = setTimeout(() => {
        setTimer(null);
        setTime(prev => prev - 1);
      }, 1000);
      setTimer(timerId);
    }
    if (time === 0) setErrMsg("인증코드를 새로 발급 받아주세요.");
  }, [time]);

  useEffect(() => {
    setTime(initTime);
  }, [timerKey]);

  return (
    <p className="form__time notoReg fs-12">
      {Math.floor(time / 60)}:{`${time % 60}`.padStart(2, "0")}
    </p>
  );
}

function ChkEmail({ type }: ChkEmailProps) {
  const navigate = useNavigate();

  const userId = useAppSelector(state => state.auth.tmpId);
  const [timerKey, setTimerKey] = useState(v4());
  const [errMsg, setErrMsg] = useState<string>(" ");
  const [isLoading, setIsLoading] = useState<boolean>(false);

  const [debounceTimer, setDebounceTimer] =
    useState<ReturnType<typeof setTimeout>>();

  const inputRef = useRef<HTMLInputElement>(null);

  const chkCode = async () => {
    if (!inputRef.current?.value) {
      setErrMsg("인증코드를 입력해주세요");
      inputRef.current?.focus();
      return;
    }
    if (!isLoading) {
      setIsLoading(true);
      let typeNumber = -1;
      if (type === "login") typeNumber = 0;
      if (type === "findPw") typeNumber = 1;
      const res = await chkAuthCode(
        userId,
        inputRef.current?.value as string,
        typeNumber
      );
      if (res === "SUCCESS") {
        if (type === "login") navigate("/join/detail");
        if (type === "findPw") navigate("/reset/pw");
      } else {
        setErrMsg("인증코드가 올바르지 않습니다.");
        inputRef.current?.focus();
      }
      setIsLoading(false);
    }
  };

  const reSendAuthCode = () => {
    if (debounceTimer) {
      clearTimeout(debounceTimer);
    }
    const timeoutId = setTimeout(() => {
      let typeNumber = -1;
      if (type === "login") typeNumber = 0;
      if (type === "findPw") typeNumber = 1;
      sendAuthCode(userId, typeNumber);
      setErrMsg(" ");
      setTimerKey(v4());
    }, 500);
    setDebounceTimer(timeoutId);
  };
  const handleKeyUP = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      chkCode();
    }
  };
  return (
    <div className="wrraper">
      <div id="chk-email">
        <header className="header">
          <p className="header__title notoBold fs-24">
            인증 이메일을 보냈습니다.
          </p>
          <p className="header__sub-title notoReg fs-16">
            {userId}에서 이메일을 확인 후 인증코드를 입력해주세요!
          </p>
        </header>
        <main className="form">
          <p className="form__title notoMid fs-16">인증코드</p>
          <input
            type="text"
            className="form__input notoReg fs-15"
            placeholder="인증번호를 입력해주세요"
            ref={inputRef}
            onKeyUp={handleKeyUP}
          />
          <Timer setErrMsg={setErrMsg} initTime={180} timerKey={timerKey} />
          <p className="form__msg notoMid fs-12">{errMsg}</p>
          <button
            className="form__btn notoMid fs-16 flex align-center justify-center"
            type="button"
            onClick={chkCode}
            disabled={errMsg === "인증코드를 새로 발급 받아주세요."}
          >
            {isLoading ? (
              <img
                src={LoadingSpinner}
                className="loading-spinner"
                alt="로딩스피너"
              />
            ) : (
              "다음"
            )}
          </button>
        </main>
        <footer className="footer">
          <div className="footer-top flex align-center">
            <p className="footer-top__msg notoMid fs-12 ellipsis">
              인증 이메일을 받지 못하셨나요?
            </p>
            <button
              className="footer-top__btn notoBold fs-12"
              type="button"
              onClick={reSendAuthCode}
            >
              이메일 재전송
            </button>
          </div>
          <div className="footer-bottom flex align-center justify-center">
            <ul className="notice-ul notoMid fs-12 flex column">
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
    </div>
  );
}

export default ChkEmail;
