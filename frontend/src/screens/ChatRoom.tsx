import Chat, { ChatProps } from "@components/Letters/Chat";
import React, { useEffect, useRef, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { v4 } from "uuid";
import "./ChatRoom.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import ImgIcon from "@images/ImgIcon.svg";
import { getDmDetailList, sendDm } from "@apis/dm";
import loadingSpinner from "@images/LoadingSpinner.svg";

function ChatRoom() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const withId = searchParams.get("with") as string;
  const [dmList, setDmList] = useState<Array<ChatProps>>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [firstLoading, setFirstLoading] = useState(false);
  const [page, setPage] = useState(0);
  const observerTarget = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const getDmList = async () => {
    setIsLoading(true);
    const res = await getDmDetailList(withId as string);
    if (res.message === "SUCCESS") {
      const dummyTest = res.data.map((dm: ChatProps) => ({
        ...dm,
        content: dm.content + page
      }));
      if (!firstLoading) {
        setFirstLoading(true);
        setDmList([...dummyTest.reverse()]);
      } else {
        setDmList([...dmList, ...dummyTest.reverse()]);
      }
    }
    setIsLoading(false);
  };

  const onIntersect = (entries: IntersectionObserverEntry[]) => {
    entries.forEach((entry: IntersectionObserverEntry) => {
      if (entry.isIntersecting && !isLoading) {
        setPage(prev => prev + 1);
      }
    });
  };

  const submitDm = async (content: string, image?: string) => {
    setDmList([{ type: "to", content }, ...dmList]);
    const res = await sendDm(withId, content, image);
  };

  const handleKeyUp = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      if (e.target.value !== "" && firstLoading) {
        submitDm(e.target.value);
        e.target.value = "";
      }
    }
  };
  useEffect(() => {
    if (!withId) {
      navigate("/404");
    }
  }, []);

  useEffect(() => {
    getDmList();
  }, [page]);

  useEffect(() => {
    const observer = new IntersectionObserver(onIntersect, { threshold: 0.1 });
    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }
    return () => {
      if (observerTarget.current) {
        observer.unobserve(observerTarget.current);
      }
    };
  }, [dmList]);

  return (
    <div className="wrapper">
      <div id="chat-room">
        <header className="header flex align-center">
          <img
            className="chat-room__user-img"
            src={UserDummyIcon}
            alt="유저더미"
          />
          <p className="chat-room__user-nick-name notoBold fs-24">{withId}</p>
        </header>
        <div className="chat-list">
          {dmList.length !== 0 ? (
            <>
              {dmList.map((dm: ChatProps) => (
                <Chat type={dm.type} content={dm.content} key={v4()} />
              ))}
              {isLoading ? (
                <div
                  className="spinner-wrraper
                flex justify-center"
                >
                  <img
                    src={loadingSpinner}
                    title="로딩스피너"
                    alt="로딩스피너"
                    className="loading-spinner"
                  />
                </div>
              ) : (
                <div ref={observerTarget} className="observerTarget">
                  여기!
                </div>
              )}
            </>
          ) : (
            <div className="no-chat flex align-center justify-center fs-24">
              {firstLoading && "주고받은 대화가 없습니다!"}
            </div>
          )}
        </div>
        <input
          type="text"
          placeholder="채팅을 입력해주세요."
          className="chat-input notoReg fs-15"
          onKeyUp={handleKeyUp}
        />
        <footer className="chat-footer">
          <button type="button" className="chat-btn">
            <img src={ImgIcon} alt="이미지업로드" className="chat__img-icon" />
          </button>
        </footer>
      </div>
    </div>
  );
}

export default ChatRoom;
