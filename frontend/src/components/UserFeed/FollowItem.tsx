import React from "react";
import "./FollowItem.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import { useNavigate } from "react-router-dom";
import { delFollow } from "@apis/userFeed";
import { FollowData } from "./FollowList";

interface FollowItemProps {
  data: FollowData;
  signal: () => void;
  isAuthor: boolean;
  type: string;
  changed: () => void;
}

function FollowItem({
  data,
  signal,
  isAuthor,
  type,
  changed
}: FollowItemProps) {
  const navigate = useNavigate();

  const goFeed = () => {
    navigate(`/userFeed/${data.nickname}`);
    signal();
  };
  return (
    <div id="follow-item">
      <div className="item-box flex">
        <div className="container flex">
          <button type="button" onClick={goFeed}>
            <img
              src={
                data.profileImg
                  ? `data:image/jpeg;base64,${data.profileImg}`
                  : UserDummyIcon
              }
              alt="User"
              className="profile-user__img"
              title="User"
            />
          </button>
        </div>
        <button onClick={goFeed} type="button" className="notoMid flex">
          {data.nickname}
        </button>
        <div className="button-box flex">
          {isAuthor && type === "팔로잉" && (
            <button
              onClick={async () => {
                await delFollow(data.nickname);
                changed();
              }}
              type="button"
              className="notoReg"
            >
              Del
            </button>
          )}
        </div>
      </div>
    </div>
  );
}

export default FollowItem;
