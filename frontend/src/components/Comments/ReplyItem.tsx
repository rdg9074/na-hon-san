import React, { useState } from "react";
import { Link } from "react-router-dom";
import "./ReplyItem.scss";
import ReplyArrow from "@images/ReplyCommentArrow.svg";
import UserDummyIcon from "@images/UserDummy.svg";
import KaKao from "@images/kakao_map.png";
import { commentDelete } from "@apis/comment";
import { getTime } from "@utils/getTime";
import X from "@images/X.svg";
import MapModal from "@components/common/MapModal";
import { commentType } from "./Comments";
import CommentEdit from "./CommentEdit";
import BigImg from "./BigImg";

interface ReplyProps {
  info: commentType;
  type: string;
  isAuthor: boolean;
  changed: () => void;
  postIdx: string;
  isArticleAuthor: boolean;
}

function ReplyItem({
  info,
  type,
  isAuthor,
  changed,
  postIdx,
  isArticleAuthor
}: ReplyProps) {
  const [replyEdit, setReplyEdit] = useState(false);
  const [imgToggle, setImgToggle] = useState(false);

  const deleteComment = async () => {
    await commentDelete(info.idx, type);
    changed();
  };

  const openEdit = () => {
    setReplyEdit(true);
  };

  const closeEdit = () => {
    setReplyEdit(false);
  };

  return (
    <div id="reply-item">
      {imgToggle && info.bannerImg && (
        <BigImg
          imgProps={info.bannerImg}
          signal={() => {
            setImgToggle(false);
          }}
        />
      )}
      <div className="reply-item flex column">
        <div className="flex">
          <div className="arrow flex justify-center">
            <img src={ReplyArrow} alt="arrow" />
          </div>
          <div className="reply-wrapper flex column align-center">
            <div className="reply-head flex">
              <div className="reply-head-profile flex align-center">
                <button
                  type="button"
                  className="reply-head-profile_img flex justify-center"
                >
                  <img
                    src={
                      info.userProfileImg
                        ? `data:image/jpeg;base64,${info.userProfileImg}`
                        : UserDummyIcon
                    }
                    alt=""
                  />
                </button>
                <div className="reply-head-profile_info">
                  <Link
                    to={`/userfeed/${info?.userNickname}`}
                    className="notoReg"
                  >
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
                <div className="reply-head-map flex justify-end">
                  {isArticleAuthor && !isAuthor && (
                    <MapModal targetUser={info.userNickname} />
                  )}
                </div>
              )}
            </div>
            <div className="reply-body flex">
              {info.bannerImg && (
                <div className="reply-img-container flex jusify-center">
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

              <div className="reply-body-content flex column">
                <p className="reply-body-content_text notoReg">
                  {info?.content}
                </p>
                {isAuthor && (
                  <div className="reply-body-content_btn flex">
                    <button
                      type="button"
                      className="notoReg"
                      onClick={openEdit}
                    >
                      수정
                    </button>
                    <button
                      type="button"
                      className="notoReg"
                      onClick={deleteComment}
                    >
                      삭제
                    </button>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
        {replyEdit && (
          <div className="edit-input flex">
            <div className="empty-space" />
            <CommentEdit
              type={type}
              commentInfo={info}
              signal={closeEdit}
              changed={changed}
              postIdx={postIdx}
              isAuthor={isAuthor}
            />
            <button
              onClick={() => {
                setReplyEdit(false);
              }}
              type="button"
              className="flex justify-end align-center"
            >
              <img src={X} alt="close" />
            </button>
          </div>
        )}
      </div>
    </div>
  );
}

export default ReplyItem;
