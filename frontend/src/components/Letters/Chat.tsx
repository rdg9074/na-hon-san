import React from "react";
import "./Chat.scss";

export type ChatProps = {
  type: string;
  content: string;
};
function Chat({ type, content }: ChatProps) {
  return (
    <div id="chat" className={type === "from" ? "left" : "right"}>
      <p className="chat-content fs-16 notoReg">{content}</p>
    </div>
  );
}

export default Chat;
