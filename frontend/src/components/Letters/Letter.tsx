import React from "react";
import "./Letter.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import { Link } from "react-router-dom";
import elapsedTime from "@utils/elapsedTime";
import { useAppSelector } from "@store/hooks";

export type LetterProps = {
  toId: string;
  fromId: string;
  content: string;
  read: boolean;
  count: number;
  time: string;
  nickname: string;
};

function Letter({
  fromId,
  content,
  toId,
  read,
  count,
  time,
  nickname
}: LetterProps) {
  const getCount = (value: number) => {
    if (value > 9) {
      return "9+";
    }
    return value;
  };
  const userId = useAppSelector(state => state.auth.userInfo?.id);

  const withId = userId === fromId ? toId : fromId;

  return (
    <Link to={`detail?with=${withId}`}>
      <div id="letter" className={read ? "read" : ""}>
        <img src={UserDummyIcon} alt="유저더미" className="letter__user-img" />
        <div className="flex column main-content ellipsis">
          <p className="letter__user-nick-name notoReg fs-12">{nickname}</p>
          <p className="letter__desc  notoMid ellipsis fs-14">{content}</p>
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
