import React, { useState, useEffect } from "react";
import { useAppSelector } from "@store/hooks";
import CommentItem from "./CommentItem";
import ReplyItem from "./ReplyItem";

export interface commentType {
  bannerImg: string | null;
  content: string;
  idx: number;
  time: string;
  upIdx: number;
  updateTime: string | null;
  userNickname: string;
  userProfileImg: string | null;
}

interface commentsProps {
  comments: commentType[];
  type: string;
  changed: () => void;
  postIdx: string;
  isArticleAuthor: boolean;
}
function Comments({
  comments,
  type,
  changed,
  postIdx,
  isArticleAuthor
}: commentsProps) {
  const [commentList, setCommentList] = useState(comments);
  const UserInfo = useAppSelector(state => state.auth.userInfo);

  useEffect(() => {
    setCommentList(comments);
  }, [comments]);

  return (
    <div>
      {commentList.map(item => {
        if (Object.keys(item)) {
          let isAuthor = false;
          if (item.userNickname === UserInfo?.nickname) {
            isAuthor = true;
          }
          if (item.upIdx) {
            return (
              <ReplyItem
                isArticleAuthor={isArticleAuthor}
                isAuthor={isAuthor}
                type={type}
                info={item}
                key={item.idx}
                changed={changed}
                postIdx={postIdx}
              />
            );
          }
          return (
            <CommentItem
              isArticleAuthor={isArticleAuthor}
              isAuthor={isAuthor}
              type={type}
              info={item}
              key={item.idx}
              postIdx={postIdx}
              changed={changed}
            />
          );
        }
        return null;
      })}
    </div>
  );
}

export default Comments;
