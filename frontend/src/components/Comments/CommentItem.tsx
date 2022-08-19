import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import "./CommentItem.scss";
import UserDummyIcon from "@images/UserDummy.svg";
import KaKao from "@images/kakao_map.png";
import X from "@images/X.svg";
import { commentDelete } from "@apis/comment";
import { getTime } from "@utils/getTime";
import MapModal from "@components/common/MapModal";
import { commentType } from "./Comments";
import CommentEdit from "./CommentEdit";
import BigImg from "./BigImg";

interface CommentProps {
  info: commentType;
  type: string;
  isAuthor: boolean;
  changed: () => void;
  postIdx: string;
  isArticleAuthor: boolean;
}

function CommentItem({
  info,
  type,
  isAuthor,
  changed,
  postIdx,
  isArticleAuthor
}: CommentProps) {
  const [editInput, setEditInput] = useState(false);
  const navigate = useNavigate();
  const [imgToggle, setImgToggle] = useState(false);

  const goFeed = () => {
    navigate(`/userfeed/${info.userNickname}`);
  };

  const deleteComment = async () => {
    await commentDelete(info.idx, type);
    changed();
  };

  const editComment = () => {
    setEditInput(true);
  };

  const closeEdit = () => {
    setEditInput(false);
  };

  return (
    <div id="comment-item">
      {imgToggle && info.bannerImg && (
        <BigImg
          imgProps={info.bannerImg}
          signal={() => {
            setImgToggle(false);
          }}
        />
      )}
      <div className="comment-wrapper flex column align-center">
        <div className="comment-head flex">
          <div className="comment-head-profile flex align-center">
            <button
              type="button"
              className="comment-head-profile_img flex justify-center"
              onClick={goFeed}
            >
              <img
                src={
                  info.userProfileImg
                    ? `data:image/jpeg;base64,${info.userProfileImg}`
                    : UserDummyIcon
                }
                alt="user"
              />
            </button>
            <div className="comment-head-profile_info">
              <Link to={`/userfeed/${info?.userNickname}`} className="notoReg">
                {info?.userNickname}
              </Link>
              <p className="notoReg">
                {info?.updateTime
                  ? `${getTime(info?.updateTime)} (수정됨)`
                  : getTime(info?.time)}
              </p>
            </div>
          </div>
          {type === "Deal" && (
            <div className="comment-head-map flex justify-end">
              {isArticleAuthor && !isAuthor && (
                <MapModal targetUser={info.userNickname} />
              )}
            </div>
          )}
        </div>
        <div className="comment-body flex">
          {info.bannerImg && (
            <div className="comment-img-container flex jusify-center">
              <button
                type="button"
                onClick={() => {
                  setImgToggle(true);
                }}
              >
                <img
                  src={`data:image/jpeg;base64,${info.bannerImg}`}
                  alt="user"
                  title="user"
                />
              </button>
            </div>
          )}
          <div className="comment-body-content flex column">
            <p className="comment-body-content_text notoReg">{info?.content}</p>
            {isAuthor ? (
              <div className="comment-body-content_btn flex">
                <button onClick={editComment} type="button" className="notoReg">
                  수정
                </button>
                <button
                  onClick={deleteComment}
                  type="button"
                  className="notoReg"
                >
                  삭제
                </button>
              </div>
            ) : (
              <div className="comment-body-content_btn flex">
                <button onClick={editComment} type="button" className="notoReg">
                  댓글달기
                </button>
              </div>
            )}
          </div>
        </div>
      </div>
      {editInput && (
        <div className="edit-input flex">
          <CommentEdit
            type={type}
            signal={closeEdit}
            commentInfo={info}
            changed={changed}
            postIdx={postIdx}
            isAuthor={isAuthor}
          />
          <button
            onClick={() => {
              setEditInput(false);
            }}
            type="button"
            className="flex align-center"
          >
            <img src={X} alt="close" />
          </button>
        </div>
      )}
    </div>
  );
}

export default CommentItem;
