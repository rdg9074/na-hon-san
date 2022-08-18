import React, { useState, useEffect } from "react";
import "./TipDetail.scss";
import { useParams, useNavigate, Link } from "react-router-dom";
import { tipRead, Article, tipDelete, tipLike } from "@apis/honeyTip";
import { addFollow, delFollow } from "@apis/userFeed";
import UserDummyIcon from "@images/UserDummy.svg";
import EmptyHeart from "@images/ArticleEmptyHeart.svg";
import Heart from "@images/Heart.svg";
import EditIcon from "@images/EditIcon.svg";
import DeleteIcon from "@images/DeleteIcon.svg";
import { useAppSelector } from "@store/hooks";
import { getTime } from "@utils/getTime";
import Comments from "@components/Comments/Comments";
import CommentInput from "@components/Comments/CommentInput";

function TipDetail() {
  const [newComment, setNewComment] = useState(false);
  const [article, setArticle] = useState<Article>();
  const [comment, setComment] = useState();
  const [userState, setUserState] = useState({
    isFollow: false,
    isLike: false
  });
  const [isLoading, setIsLoading] = useState(false);
  const { id } = useParams();
  const navigate = useNavigate();
  const UserInfo = useAppSelector(state => state.auth.userInfo);

  const changed = () => {
    setNewComment(cur => !cur);
  };

  useEffect(() => {
    tipRead(id as string)
      .then(res => {
        setUserState({
          isFollow: res.isFollow,
          isLike: res.isLike
        });
        setArticle(res.tip);
        const comments = res.tipComments.reverse();
        setComment(comments);
      })
      .catch(() => navigate("NotFound"));
  }, [newComment, id]);

  if (!article) {
    return <div />;
  }
  const content = {
    __html: article.content
  };

  const goEdit = () => {
    navigate(`/tip/edit/${id}`, { state: article });
  };

  const deleteArticle = async () => {
    const chk = window.confirm("정말로 삭제하시겠습니까?");
    if (chk) {
      const res = await tipDelete(id as string);
      if (res === "SUCCESS") {
        navigate("/");
      }
      return res;
    }
    return 0;
  };

  const setLike = async () => {
    if (!UserInfo) {
      return navigate("/login");
    }
    if (!isLoading) {
      setIsLoading(true);
      const res = await tipLike(id as string);
      if (res.status === 200) {
        changed();
      }
      setIsLoading(false);
    }
    return 0;
  };

  const setFollow = async () => {
    if (!UserInfo) {
      return navigate("/login");
    }
    if (!isLoading) {
      setIsLoading(true);
      if (userState.isFollow) {
        await delFollow(article.userNickname);
      } else {
        await addFollow(article.userNickname);
      }
      setIsLoading(false);
      changed();
    }
    return 0;
  };

  const isAuthor = UserInfo?.nickname === article.userNickname;
  return (
    <div id="tip-detail-page">
      <div className="article flex column">
        <p className="title notoMid">{article.title}</p>
        <div className="header flex">
          <div className="header-info flex">
            <div className="header-info__img-container flex">
              <button
                type="button"
                onClick={() => {
                  navigate(`/userfeed/${article.userNickname}`);
                }}
              >
                <img
                  src={
                    article.userProfileImg
                      ? `data:image/jpeg;base64,${article.userProfileImg}`
                      : UserDummyIcon
                  }
                  alt="User"
                  className="profile-user__img"
                  title="User"
                />
              </button>
            </div>
            <div className="header-info__text flex column justify-center">
              <Link
                to={`/userfeed/${article.userNickname}`}
                className="user-name notoMid"
              >
                {article.userNickname}
              </Link>
              <div className="created flex column align-center">
                <p className=" notoReg">
                  {article.updateTime
                    ? `${getTime(article.updateTime)} (수정됨)`
                    : getTime(article.time)}
                </p>
              </div>
            </div>
            <button
              onClick={setFollow}
              className={`header-info__btn notoReg ${
                isAuthor ? "hide" : null
              } ${userState.isFollow ? "grey" : "yellow"}`}
              type="button"
            >
              {userState.isFollow ? "언팔로우" : "팔로우"}
            </button>
          </div>
          <div className="header-func flex">
            {isAuthor ? (
              <div className="header-func-btn flex">
                <button onClick={goEdit} type="button">
                  <img src={EditIcon} alt="edit" title="edit" />
                </button>
                <button onClick={deleteArticle} type="button">
                  <img src={DeleteIcon} alt="del" title="delete" />
                </button>
              </div>
            ) : (
              <div className="header-func-btn flex">
                <button onClick={setLike} type="button">
                  <img
                    src={userState.isLike ? Heart : EmptyHeart}
                    alt="like"
                    title="like"
                  />
                </button>
              </div>
            )}
          </div>
        </div>
        <div className="body flex">
          <div className="body-content " dangerouslySetInnerHTML={content} />
        </div>
        <div className="comment flex column">
          <div className="comment-head">
            <p className="notoMid">
              댓글<span className="">{article.comment}</span>
            </p>
          </div>
          <div className="comment-input flex">
            <div className="input-img-container flex">
              <img
                src={
                  UserInfo?.profileImg
                    ? `data:image/jpeg;base64,${UserInfo.profileImg}`
                    : UserDummyIcon
                }
                alt="dum"
                title="user-icon"
              />
            </div>
            <CommentInput
              type="Tip"
              changed={changed}
              articleIdx={id as string}
            />
          </div>
          {comment ? (
            <Comments
              isArticleAuthor={false}
              postIdx={id as string}
              changed={changed}
              type="Tip"
              comments={comment}
            />
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default TipDetail;
