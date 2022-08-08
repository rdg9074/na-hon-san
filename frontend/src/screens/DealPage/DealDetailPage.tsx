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
import { dealRead, dealArticle, dealDelete } from "@apis/honeyDeal";
import Comments from "@components/Comments/Comments";
import CommentInput from "@components/Comments/CommentInput";

function DealDetailPage() {
  const [newComment, setNewComment] = useState(false);
  const [dealState, setDealState] = useState("");
  const [article, setArticle] = useState<dealArticle>();
  const [comment, setComment] = useState();
  const { id } = useParams();
  const userInfo = useAppSelector(state => state.auth.userInfo);
  const isAuthor = userInfo?.nickname === article?.userNickname;
  const navigate = useNavigate();

  const changed = () => {
    setNewComment(cur => !cur);
  };

  useEffect(() => {
    dealRead(id as string).then(res => {
      setArticle(res.data);
      setComment(res.data.comments);
      setDealState(res.data.state);
    });
  }, [newComment, id]);

  const changeColor = (state: string) => {
    console.log(dealState);
    if (dealState === "거래 대기") {
      return "green";
    }
    if (dealState === "거래 진행") {
      return "yellow";
    }
    return "brown";
  };

  const goEdit = () => {
    navigate(`/deal/edit/${id}`, { state: article });
  };

  const deleteArticle = async () => {
    const res = await dealDelete(id as string);
    if (res === "SUCCESS") {
      navigate("/");
    }
    return res;
  };

  if (!article) {
    return <div />;
  }

  return (
    <div id="deal-detail-page">
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
            <div className="header-info__text flex column">
              <p className="user-name notoMid">{article.userNickname}</p>
              <div className="created flex column align-center">
                <p className=" notoReg">
                  {article.updateTime
                    ? `${getTime(article.updateTime)} (수정됨)`
                    : getTime(article.time)}
                </p>
              </div>
            </div>
            <button
              className={`header-info__btn notoReg ${isAuthor && "hide"} `}
              type="button"
            >
              팔로우
            </button>
          </div>
          <div className="header-func flex">
            {isAuthor ? (
              <div className="header-func-btn flex">
                <div className="header-func-btn_state flex justify-center">
                  <div className={`${changeColor(dealState)}`}> </div>
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
                <img src={KakaoMap} alt="123" title="map" />
                <img src={EmptyHeart} alt="123" title="like" />
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
