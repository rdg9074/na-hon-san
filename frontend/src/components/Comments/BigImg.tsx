import React from "react";
import X from "@images/X.svg";
import "./BigImg.scss";

interface BiggerImgProps {
  imgProps: string;
  signal: () => void;
}

function BigImg({ imgProps, signal }: BiggerImgProps) {
  return (
    <div id="big-img">
      <div className="big-img flex column">
        <button type="button" onClick={signal}>
          <img src={X} alt="취소 아이콘" />
        </button>
        <img
          src={`data:image/jpeg;base64,${imgProps}`}
          alt="댓글 이미지"
          title="comment-img"
        />
      </div>
    </div>
  );
}

export default BigImg;
