import React from "react";
import "./Chat.scss";

export type ChatProps = {
  type: string;
  content: string;
  image: string | null;
};
function Chat({ type, content, image }: ChatProps) {
  return (
    <div id="chat" className={type === "recv" ? "left" : "right"}>
      {image ? (
        <img
          src={`data:image/jpeg;base64,${image}`}
          alt="이미지"
          className="chat__img"
          title="이미지"
        />
      ) : (
        <p className="chat-content fs-16 notoReg">{content}</p>
      )}
    </div>
  );
}

export default Chat;
