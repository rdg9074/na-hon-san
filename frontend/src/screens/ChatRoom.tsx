import Chat from "@components/Letters/Chat";
import React, { useEffect, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { v4 } from "uuid";
import "./ChatRoom.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import ImgIcon from "@images/ImgIcon.svg";

function ChatRoom() {
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();
  const [chatInfo, setChatInfo] = useState({ send: "", recv: "", letters: [] });
  const dummy = [
    { content: "어떤거부터사야지?", type: "recv" },
    { content: "벤츠?", type: "send" },
    { content: "아유아유ㅏ?", type: "recv" },
    {
      content:
        "뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?",
      type: "send"
    },
    { content: "어떤거부터사야지?", type: "recv" },
    { content: "벤츠?", type: "send" },
    { content: "아유아유ㅏ?", type: "recv" },
    {
      content:
        "뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?",
      type: "send"
    },
    { content: "어떤거부터사야지?", type: "send" },
    {
      content:
        "뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?",
      type: "send"
    },
    { content: "어떤거부터사야지?", type: "send" },
    {
      content:
        "뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?",
      type: "send"
    },
    { content: "어떤거부터사야지?", type: "send" },
    {
      content:
        "뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?뭐라고?",
      type: "send"
    },
    { content: "어떤거부터사야지?", type: "send" }
  ];
  useEffect(() => {
    const send = searchParams.get("send");
    const recv = searchParams.get("recv");
    if (send && recv) {
      setChatInfo({
        ...chatInfo,
        send,
        recv
      });
    } else {
      navigate("/404");
    }
  }, []);
  return (
    <div className="wrapper">
      <div id="chat-room">
        <header className="header flex align-center">
          <img
            className="chat-room__user-img"
            src={UserDummyIcon}
            alt="유저더미"
          />
          <p className="chat-room__user-nick-name notoBold fs-24">
            {chatInfo.recv}
          </p>
        </header>
        <div className="chat-list">
          {dummy.map(item => (
            <Chat type={item.type} content={item.content} key={v4()} />
          ))}
        </div>
        <input
          type="text"
          placeholder="채팅을 입력해주세요."
          className="chat-input notoReg fs-15"
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
