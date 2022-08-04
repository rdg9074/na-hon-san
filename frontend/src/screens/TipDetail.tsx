import React, { useState, useEffect } from "react";
import "./TipDetail.scss";
import { useParams, useNavigate } from "react-router-dom";
import { tipRead, Article, tipDelete } from "@apis/honeyTip";
import UserDummyIcon from "@images/UserDummy.svg";
import EmptyHeart from "@images/ArticleEmptyHeart.svg";
import EditIcon from "@images/EditIcon.svg";
import DeleteIcon from "@images/DeleteIcon.svg";
import { useAppSelector } from "@store/hooks";
import { getTime } from "@utils/getTime";

function TipDetail() {
  const [article, setArticle] = useState<Article>();
  const [comment, setComment] = useState();
  const { id } = useParams();
  const navigate = useNavigate();
  const UserInfo = useAppSelector(state => state.auth.userInfo);

  useEffect(() => {
    const getArticle = tipRead(id as string);
    getArticle
      .then(res => {
        setArticle(res.tip);
        setComment(res.tipComments);
        console.log(res);
      })
      .catch(() => navigate("NotFound"));
  }, []);

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
    const res = await tipDelete(id as string);
    if (res === "SUCCESS") {
      navigate("/");
    } else {
      console.log(res);
    }
  };

  const isAuthor = UserInfo?.nickname === article.userNickname;
  console.log(isAuthor);
  return (
    <div id="tip-detail-page">
      <div className="article flex column">
        <p className="title notoMid">{article.title}</p>
        <div className="header flex">
          <div className="header-info flex">
            <div className="header-info__img-container flex">
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
            </div>
            <div className="header-info__text flex column justify-center">
              <p className="user-name notoMid">{article.userNickname}</p>
              <div className="created flex column align-center">
                <p className=" notoReg">
                  {article.updateTime
                    ? getTime(article.updateTime)
                    : getTime(article.time)}
                </p>
              </div>
            </div>
            <button
              className={`header-info__btn notoReg ${isAuthor ? "hide" : null}`}
              type="button"
            >
              팔로우
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
              <button type="button" className="header-func-btn flex">
                <img src={EmptyHeart} alt="like" title="like" />
              </button>
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
              <img src={UserDummyIcon} alt="dum" title="user-icon" />
            </div>
            <input type="text" placeholder="댓글을 입력해" />
          </div>
          <div className="comment-component"> </div>
        </div>
      </div>
    </div>
  );
}

export default TipDetail;
