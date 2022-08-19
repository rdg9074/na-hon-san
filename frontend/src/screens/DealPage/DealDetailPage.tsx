import React, { useState, useEffect } from "react";
import "./DealDetailPage.scss";
import { useParams, useNavigate, Link } from "react-router-dom";
import UserDummyIcon from "@images/UserDummy.svg";
import EmptyHeart from "@images/ArticleEmptyHeart.svg";
import KakaoMap from "@images/kakao_map.png";
import EditIcon from "@images/EditIcon.svg";
import DeleteIcon from "@images/DeleteIcon.svg";
import { getTime } from "@utils/getTime";
import { useAppSelector } from "@store/hooks";
import { dealRead, dealArticle, dealDelete, dealLike } from "@apis/honeyDeal";
import Comments from "@components/Comments/Comments";
import CommentInput from "@components/Comments/CommentInput";
import { addFollow, delFollow } from "@apis/userFeed";
import Heart from "@images/Heart.svg";
import MapModal from "@components/common/MapModal";

function DealDetailPage() {
  const [newComment, setNewComment] = useState(false);
  const [dealState, setDealState] = useState("");
  const [article, setArticle] = useState<dealArticle>();
  const [isLoading, setIsLoading] = useState(false);
  const [comment, setComment] = useState();
  const { id } = useParams();
  const [userState, setUserState] = useState({
    isFollow: false,
    isLike: false
  });
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const isAuthor = userInfo?.nickname === article?.userNickname;
  const navigate = useNavigate();

  const changed = () => {
    setNewComment(cur => !cur);
  };

  useEffect(() => {
    dealRead(id as string).then(res => {
      setArticle(res.deal);
      if (res.dealComments) {
        const comments = res.dealComments.reverse();
        setComment(comments);
      }
      setDealState(res.deal.state);
      setUserState({
        isFollow: res.isFollow,
        isLike: res.isLike
      });
    });
  }, [newComment, id]);

  const changeColor = (state: string) => {
    if (state === "거래 대기") {
      return "green";
    }
    if (state === "거래 진행") {
      return "yellow";
    }
    return "brown";
  };

  const goEdit = () => {
    navigate(`/deal/edit/${id}`, { state: article });
  };

  const deleteArticle = async () => {
    const chk = window.confirm("정말로 삭제하시겠습니까?");
    if (chk) {
      const res = await dealDelete(id as string);
      if (res === "SUCCESS") {
        navigate("/");
      }
      return res;
    }
    return 0;
  };

  if (!article) {
    return <div />;
  }

  const setLike = async () => {
    if (!userInfo) {
      return navigate("/login");
    }
    if (!isLoading) {
      setIsLoading(true);
      const res = await dealLike(id as string);
      if (res.status === 200) {
        changed();
      }
      setIsLoading(false);
    }
    return 0;
  };

  const setFollow = async () => {
    if (!userInfo) {
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

  return (
    <div id="deal-detail-page">
      <div className="article flex column">
        <p className="title notoMid">{article.title}</p>
        <div className="title-state_info">
          {isAuthor && (
            <p className="notoReg fs-10">
              거래 상태는 수정페이지에서만 변경 가능해요
            </p>
          )}
        </div>
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
            <div className="header-info__text flex column">
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
              className={`header-info__btn notoReg ${isAuthor && "hide"} ${
                userState.isFollow ? "grey" : "yellow"
              }`}
              type="button"
            >
              {userState.isFollow ? "언팔로우" : "팔로우"}
            </button>
          </div>
          <div className="header-func flex">
            {isAuthor ? (
              <div className="header-func-btn flex">
                <div className="header-func-btn_state flex justify-center">
                  <div className={`notoReg fs-10 ${changeColor(dealState)}`} />
                </div>
                <button onClick={goEdit} type="button">
                  <img src={EditIcon} alt="edit" title="edit" />
                </button>
                <button onClick={deleteArticle} type="button">
                  <img src={DeleteIcon} alt="del" title="delete" />
                </button>
              </div>
            ) : (
              <div className="header-func-btn flex">
                {userInfo ? (
                  <MapModal targetUser={article.userNickname} />
                ) : (
                  <button
                    type="button"
                    onClick={() => {
                      navigate("/login");
                    }}
                  >
                    <img src={KakaoMap} alt="KakaoMap" />
                  </button>
                )}

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
          <div
            className="body-content "
            dangerouslySetInnerHTML={{
              __html: article.content
            }}
          />
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
                  userInfo?.profileImg
                    ? `data:image/jpeg;base64,${userInfo.profileImg}`
                    : UserDummyIcon
                }
                alt="dum"
                title="user-icon"
              />
            </div>
            <CommentInput
              type="Deal"
              changed={changed}
              articleIdx={id as string}
            />
          </div>
          {comment ? (
            <Comments
              isArticleAuthor={article.userNickname === userInfo?.nickname}
              postIdx={id as string}
              changed={changed}
              type="Deal"
              comments={comment}
            />
          ) : null}
        </div>
      </div>
    </div>
  );
}

export default DealDetailPage;
