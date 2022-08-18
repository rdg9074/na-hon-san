import React from "react";
import "./Letter.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import { Link } from "react-router-dom";
import elapsedTime from "@utils/elapsedTime";

export type LetterProps = {
  content: string;
  read: boolean;
  count: number;
  time: string;
  nickname: string;
  image: string | null;
  toProfileImg: string | null;
};

function Letter({
  content,
  read,
  count,
  time,
  nickname,
  image,
  toProfileImg
}: LetterProps) {
  const getCount = (value: number) => {
    if (value > 9) {
      return "9+";
    }
    return value;
  };
  return (
    <Link to={`detail?with=${nickname}`}>
      <div id="letter" className={read ? "read" : ""}>
        <img
          src={
            toProfileImg
              ? `data:image/jpeg;base64,${toProfileImg}`
              : UserDummyIcon
          }
          alt="유저더미"
          className="letter__user-img"
        />
        <div className="flex column main-content ellipsis">
          <p className="letter__user-nick-name notoReg fs-12">{nickname}</p>
          <p className="letter__desc  notoMid ellipsis fs-14">
            {image ? "이미지" : content}
          </p>
        </div>
        <div className="letter__sub flex column">
          <p className="letter__count notoReg flex align-center justify-center  fs-8">
            {getCount(count)}
          </p>
          <p className="letter__time notoReg fs-8">{elapsedTime(time)}</p>
        </div>
      </div>
    </Link>
  );
}

export default Letter;
