import React from "react";
import UserDummyIcon from "@images/UserDummy.svg";
import CloseIcon from "@images/X.svg";
import "./Alarm.scss";
import elapsedTime from "@utils/elapsedTime";
import { Link } from "react-router-dom";
import { readAlarm, reqDeleteAlarm } from "@apis/alarm";
import { useAppDispatch } from "@store/hooks";
import { deleteAlarm } from "@store/ducks/alarm/alarmSlice";

export type AlarmProps = {
  idx: number;
  noticeType: string;
  postIdx: number | null;
  fromUserNickname: string;
  postType: string | null;
  read: boolean;
  time: string;
  closeTooltip: () => void;
};

function Alarm({
  idx,
  noticeType,
  postIdx,
  fromUserNickname,
  postType,
  read,
  time,
  closeTooltip
}: AlarmProps) {
  const onClickAlarm = () => {
    if (!read) {
      readAlarm(idx);
    }
    closeTooltip();
  };
  const dispatch = useAppDispatch();
  const handleDeleteBtn = () => {
    reqDeleteAlarm(idx);
    dispatch(deleteAlarm(idx));
  };
  const getContent = (name: string, type: string) => {
    if (type === "like") {
      return (
        <Link
          to={
            postType === "tip"
              ? `/tip/detail/${postIdx}`
              : `/deal/detail/${postIdx}`
          }
          className="content notoReg fs-14"
          onClick={onClickAlarm}
        >
          {name}님이 회원님의 글을 좋아합니다.
        </Link>
      );
    }
    if (type === "follow") {
      return (
        <Link
          to={`/userfeed/${name}`}
          className="content notoReg fs-14"
          onClick={onClickAlarm}
        >
          {name}님이 회원님을 팔로우하였습니다.
        </Link>
      );
    }
    if (type === "comment") {
      return (
        <Link
          to={
            postType === "tip"
              ? `/tip/detail/${postIdx}`
              : `/deal/detail/${postIdx}`
          }
          className="content notoReg fs-14"
          onClick={onClickAlarm}
        >
          {name}님이 회원님의 글에 댓글을 남겼습니다.
        </Link>
      );
    }
    return (
      <Link
        to={
          postType === "tip"
            ? `/tip/detail/${postIdx}`
            : `/deal/detail/${postIdx}`
        }
        className="content notoReg fs-14"
        onClick={onClickAlarm}
      >
        {name}님이 회원님의 댓글에 대댓글을 남겼습니다.
      </Link>
    );
  };

  return (
    <div
      id="alarm"
      className={read ? `flex align-center read` : `flex align-center`}
    >
      <img src={UserDummyIcon} alt="유저더미" className="user-icon" />
      {getContent(fromUserNickname, noticeType)}
      <div className="right flex column">
        <button type="button" onClick={handleDeleteBtn}>
          <img src={CloseIcon} alt="삭제아이콘" className="right__close-icon" />
        </button>

        <p className="right__time fs-10">{elapsedTime(time)}</p>
      </div>
    </div>
  );
}

export default Alarm;
