import React from "react";
import "./Chat.scss";

type ChatProps = {
  type: string;
  content: string;
};
function Chat({ type, content }: ChatProps) {
  return (
    <div id="chat" className={type === "recv" ? "left" : "right"}>
      <p className="chat-content fs-32 notoReg">{content}</p>
    </div>
  );
}

export default Chat;
