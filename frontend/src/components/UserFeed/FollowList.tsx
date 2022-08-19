import React, { useEffect, useState, useRef } from "react";
import "./FollowList.scss";
import SearchIcon from "@images/Search.svg";
import getCounts from "@utils/getCounts";
import { v4 } from "uuid";
import {
  readFollow,
  readFollower,
  searchFollow,
  searchFollower
} from "@apis/userFeed";
import { useAppSelector } from "@store/hooks";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import FollowItem from "./FollowItem";

interface FollowListProps {
  signal: () => void;
  followModal: string;
  idx: string;
  isMine: boolean;
  isOpen: boolean[];
}

export interface FollowData {
  id: string;
  nickname: string;
  profileImg: string;
}

function FollowList({
  signal,
  followModal,
  idx,
  isMine,
  isOpen
}: FollowListProps) {
  const [data, setData] = useState<Array<FollowData>>([]);
  const [isLoading, setIsLoading] = useState(false);
  const isAuthor =
    useAppSelector(state => state.auth.userInfo?.nickname) === idx;
  const inputRef = useRef<HTMLInputElement>(null);
  const [isChanged, setIsChanged] = useState(false);
  const [open, setOpen] = useState(true);

  useEffect(() => {
    if (!isLoading) {
      setIsLoading(true);
      if (followModal === "팔로잉" && (isOpen[1] || isMine)) {
        readFollow(idx).then(res => {
          setData(res.data.data);
        });
      } else if (followModal === "팔로워" && (isOpen[0] || isMine)) {
        readFollower(idx).then(res => {
          setData(res.data.data);
        });
      } else {
        setOpen(false);
      }
      setIsLoading(false);
    }
  }, [idx, isChanged]);

  const changed = () => {
    setIsChanged(state => !state);
  };

  const send = async (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      if (!isLoading && inputRef.current) {
        setIsLoading(true);
        if (followModal === "팔로잉") {
          const keyword = inputRef.current?.value;
          const res = await searchFollow(idx, keyword as string);
          setData(res.data.data);
        } else {
          const keyword = inputRef.current?.value;
          const res = await searchFollower(idx, keyword as string);
          setData(res.data.data);
        }
      }
      setIsLoading(false);
    }
  };

  if (!open && inputRef.current) {
    inputRef.current.disabled = true;
  }

  return (
    <div id="follow-list">
      <div className="list-header flex">
        <p className="list-header__title notoBold flex justify-center">
          {followModal}
          <span>
            {data && open && `(${getCounts(data?.length as number)})`}
          </span>
        </p>
        <div className="list-header__input-box">
          <img src={SearchIcon} alt="Search" />
          <input
            placeholder="검색"
            type="text"
            ref={inputRef}
            onKeyUp={e => send(e)}
          />
        </div>
      </div>
      {!data ? (
        <div className="list-body flex justify-center align-center">
          <img
            src={LoadingSpinner}
            className="loading-spinner"
            alt="로딩스피너"
          />
        </div>
      ) : (
        <div
          className={`list-body ${
            !data?.length && "flex justify-center align-center"
          }`}
        >
          {data?.map(item => (
            <FollowItem
              signal={signal}
              data={item}
              key={v4()}
              isAuthor={isAuthor}
              type={followModal}
              changed={changed}
            />
          ))}
          {!data?.length && (
            <p className="notoReg fs-14">
              {open ? "아무도 없어요..." : "비공개 설정입니다."}
            </p>
          )}
        </div>
      )}
      <div className="list-footer notoBold flex">
        <button onClick={signal} type="button">
          닫기
        </button>
      </div>
    </div>
  );
}

export default React.memo(FollowList);
