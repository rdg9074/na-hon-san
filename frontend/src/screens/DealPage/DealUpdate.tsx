import React, { useState, useCallback, useRef, useEffect } from "react";
import {
  useNavigate,
  useLocation,
  useParams,
  Navigate
} from "react-router-dom";
import { useAppSelector } from "@store/hooks";
import Editor from "@components/common/s3Uploader/Editor";
import "./DealUpdate.scss";
import noimg from "@images/noimg.svg";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import isImage from "@utils/isImage";
import { dealArticle, dealUpdate } from "@apis/honeyDeal";
import { v4 } from "uuid";
import dealCategory from "@constants/dealCategory";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import X from "@images/X.svg";

function DealUpdate() {
  const [dealState, setDealState] = useState("");
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [thumnail, setThumnail] = useState("");
  const [category, setCategory] = useState("");
  const [title, setTitle] = useState("");
  const [errMsg, setErrMsg] = useState("");
  const [chk, setChk] = useState(false);
  const [tumErr, setTumErr] = useState(false);
  const [updateData, setUpdateData] = useState("");
  const [spinner, setSpinner] = useState(false);
  const imgInput = useRef<HTMLInputElement>(null);
  const titleRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const location = useLocation();
  const userInfo = useAppSelector(state => state.auth.userInfo);

  if (!location.state) {
    alert("비정상적인 접근입니다.");
    return <Navigate to="/deal" />;
  }

  useEffect(() => {
    const articleInfo = location.state as dealArticle;
    if (articleInfo.bannerImg) {
      setThumnail(`data:image/jpeg;base64,${articleInfo.bannerImg}`);
    }
    setCategory(articleInfo.category);
    setTitle(articleInfo.title);
    setUpdateData(articleInfo.content);
    setDealState(articleInfo.state);
  }, []);

  const { id } = useParams();

  const back = () => {
    navigate(`/deal/detail/${id}`);
  };

  const changeDealState = (e: React.ChangeEvent<HTMLSelectElement>) => {
    setDealState(e.target.value);
  };

  const changeColor = (state: string) => {
    if (state === "거래 대기") {
      return "green";
    }
    if (state === "거래 진행") {
      return "yellow";
    }
    return "brown";
  };

  const clickInput = () => {
    imgInput.current?.click();
  };

  const fileread = () => {
    if (imgInput.current?.files) {
      const file = imgInput.current.files[0];

      if (file && isImage(file)) {
        setSendFile(file);
      }
    }
  };

  const receiveFile = useCallback((data: string) => {
    setThumnail(data);
  }, []);

  const changeCategory = (cate: string) => {
    setCategory(cate);
  };

  const chkForm = () => {
    if (!titleRef.current?.value) {
      setErrMsg("제목을 입력해주세요.");
      titleRef.current?.focus();
      return;
    }
    if (titleRef.current.value.length > 30) {
      setErrMsg("제목은 30자까지에요.");
      titleRef.current?.focus();
      return;
    }
    if (!thumnail) {
      imgInput.current?.focus();
      setTumErr(true);
      return;
    }
    setSpinner(true);
    setChk(true);
  };

  const receiveValue = async (data: string) => {
    const payload = {
      area: "광주",
      state: dealState,
      category,
      title: titleRef.current?.value as string,
      content: data,
      bannerImg: thumnail.replace("data:image/jpeg;base64,", "")
    };
    const res = await dealUpdate(payload, id as string);
    if (res.status === 500) {
      alert("글이 너무 길어요 ㅠㅠ");
      setSpinner(false);
    }
    navigate(`/deal/detail/${id}`);
  };

  return (
    <div id="deal-edit">
      <input type="file" accept="image/*" ref={imgInput} onChange={fileread} />
      {sendFile ? (
        <ImgResizer
          imgfile={sendFile}
          newImgfile={receiveFile}
          imgW={400}
          imgH={400}
        />
      ) : null}
      <div className="deal-header ">
        <div className="deal-header-title notoBold flex">
          <p>
            <span>꿀</span>딜<span> 쓰</span>기
          </p>
          <div className="deal-header-select">
            <select
              className={`deal-header-select__select notoReg ${changeColor(
                dealState
              )}`}
              onChange={e => {
                changeDealState(e);
              }}
              value={dealState}
            >
              <option value="거래 대기">거래 대기</option>
              <option value="거래 진행">거래 진행</option>
              <option value="거래 완료">거래 완료</option>
            </select>
          </div>
        </div>
        <div className="deal-header-category flex">
          <p className="category-label notoMid">카테고리</p>
          <div className="deal-header-category-box flex">
            {dealCategory.map(item => {
              return (
                <button
                  onClick={() => {
                    changeCategory(item);
                  }}
                  type="button"
                  key={v4()}
                  className={`notoReg ${category === item && "button-active"}`}
                >
                  {item}
                </button>
              );
            })}
          </div>
        </div>
        <div className="deal-header-area">
          <p className="deal-header-area_label notoMid">거래 지역</p>
          <div className="deal-header-area-info flex">
            <input
              type="text"
              readOnly
              value={userInfo?.area as string}
              className="deal-header-area-info_text notoReg"
            />

            <button
              className="deal-header-area-info_button notoReg"
              type="button"
              onClick={() => {
                if (userInfo?.social === "normal") {
                  navigate("/account");
                } else {
                  navigate("/join/more");
                }
              }}
            >
              변경
            </button>
          </div>
          <div className="deal-header-area_desc notoReg flex column">
            <span>주소는 마이페이지에서만 변경 가능해요.</span>
            <span>주소는 위치 추천 용도로만 사용됩니다.</span>
          </div>
        </div>
        <div className="deal-header-preview flex justify-center">
          <button
            onClick={() => {
              setThumnail("");
            }}
            type="button"
            className={`deal-close ${!thumnail && "hide"}`}
          >
            <img src={X} alt="close" />
          </button>
          <button
            onClick={clickInput}
            type="button"
            className="deal-header-preview_container flex column justify-center align-center"
          >
            <p className="deal-header-preview_container-title notoMid">
              배너이미지
            </p>
            <div className="deal-header-preview_img flex justify-center align-center">
              {thumnail ? (
                <img
                  src={thumnail}
                  alt="preview"
                  className="deal-header-preview_img-img"
                />
              ) : (
                <img
                  src={noimg}
                  alt="no-img"
                  title="preview"
                  className="deal-header-preview_img-img"
                />
              )}
            </div>
            <span className="notoReg">
              <span className={`${tumErr ? "err" : "hide"}`}>
                꿀딜 게시글 썸네일은 필수에요.
              </span>
              <br />
              jpg, png, gif, jpeg 파일만 지원해요.
            </span>
          </button>
        </div>
      </div>
      <div className="deal-content flex column align-center">
        <p className="notoMid">내용</p>
        <input
          ref={titleRef}
          className="deal-title"
          type="text"
          placeholder="제목은 30자까지 입력할 수 있어요."
          defaultValue={title}
        />
        {errMsg ? <span className="notoReg fs-16">{errMsg}</span> : null}

        <Editor editorValue={receiveValue} getValue={chk} update={updateData} />
      </div>
      {spinner ? (
        <div className="send flex">
          <img
            src={LoadingSpinner}
            className="loading-spinner"
            alt="로딩스피너"
          />
        </div>
      ) : (
        <div className="send flex notoReg">
          <button type="button" onClick={chkForm}>
            작성
          </button>
          <button className="cancle" type="button" onClick={back}>
            취소
          </button>
        </div>
      )}
    </div>
  );
}

export default DealUpdate;
