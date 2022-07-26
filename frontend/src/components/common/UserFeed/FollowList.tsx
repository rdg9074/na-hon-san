import React from "react";
import "./FollowList.scss";
import SearchIcon from "@images/Search.svg";
import getCounts from "@utils/getCounts";
import { v4 } from "uuid";
import FollowItem from "./FollowItem";

interface FollowListProps {
  signal: () => void;
  followModal: string;
}

function FollowList({ signal, followModal }: FollowListProps) {
  const dum: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];
  return (
    <div id="follow-list">
      <div className="list-header flex">
        <p className="list-header__title notoBold flex justify-center">
          {followModal}
          <span>{`(${getCounts(1234)})`}</span>
        </p>
        <div className="list-header__input-box">
          <img src={SearchIcon} alt="Search" />
          <input placeholder="검색" type="text" />
        </div>
      </div>
      <div className="list-body">
        {dum.map(() => (
          <FollowItem key={v4()} />
        ))}
      </div>
      <div className="list-footer notoBold flex">
        <button onClick={signal} type="button">
          닫기
        </button>
      </div>
    </div>
  );
}

export default FollowList;
