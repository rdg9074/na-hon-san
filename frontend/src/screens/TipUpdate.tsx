import React, { useState, useCallback, useRef, useEffect } from "react";
import { useNavigate, useLocation, useParams } from "react-router-dom";
import Editor from "@components/common/s3Uploader/Editor";
import "./TipUpdate.scss";
import noimg from "@images/noimg.svg";
import HoneyRecipe from "@images/HoneyRecipe.svg";
import HoneyTem from "@images/HoneyTem.svg";
import HoneyTip from "@images/HoneyTip.svg";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import isImage from "@utils/isImage";
import { Article, tipUpdate } from "@apis/honeyTip";
import LoadingSpinner from "@images/LoadingSpinner.svg";

function TipUpdate() {
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [thumnail, setThumnail] = useState("");
  const [category, setCategory] = useState("tip");
  const [title, setTitle] = useState("");
  const [errMsg, setErrMsg] = useState("");
  const [chk, setChk] = useState(false);
  const [updateData, setUpdateData] = useState("");
  const [spinner, setSpinner] = useState(false);
  const imgInput = useRef<HTMLInputElement>(null);
  const titleRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const location = useLocation();

  // 수정 전 데이터 불러오기
  useEffect(() => {
    const articleInfo = location.state as Article;
    setThumnail(`data:image/jpeg;base64,${articleInfo.bannerImg}`);
    setCategory(articleInfo.category);
    setTitle(articleInfo.title);
    setUpdateData(articleInfo.content);
  }, []);

  const { id } = useParams();

  const back = () => {
    navigate(`/tip/detail/${id}`);
  };

  // 썸네일 인풋태그열기 > 파일내리기 > 파일 받기
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

  // 카테고리 변경
  const changeCategory = (cate: string) => {
    setCategory(cate);
  };

  // 유효성 검사
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
    setSpinner(true);
    setChk(true);
  };

  // 검사 > 에디터에서 밸류 받기 > DB 전송
  const receiveValue = async (data: string) => {
    const payload = {
      category,
      title: titleRef.current?.value as string,
      content: data,
      bannerImg: thumnail.replace("data:image/jpeg;base64,", "")
    };
    const res = await tipUpdate(payload, id as string);
    if (res.status === 500) {
      alert("글이 너무 길어요 ㅠㅠ");
      setSpinner(false);
    }
    navigate(`/tip/detail/${id}`);
  };

  return (
    <div id="tip-edit">
      <input type="file" accept="image/*" ref={imgInput} onChange={fileread} />
      {sendFile ? (
        <ImgResizer
          imgfile={sendFile}
          newImgfile={receiveFile}
          imgW={200}
          imgH={200}
        />
      ) : null}
      <div className="header ">
        <div className="header-title notoBold flex">
          <p>
            <span>꿀</span>팁<span> 쓰</span>기
          </p>
        </div>
        <div className="header-category flex">
          <p className="category-label notoMid">Category</p>
          <button
            type="button"
            onClick={() => changeCategory("recipe")}
            className={`notoReg ${category === "recipe" ? "active" : null}`}
          >
            <img src={HoneyRecipe} alt="꿀시피" title="꿀시피" />
            <p className="notoReg">꿀시피</p>
          </button>
          <button
            type="button"
            onClick={() => changeCategory("tip")}
            className={`notoReg ${category === "tip" ? "active" : null}`}
          >
            <img src={HoneyTip} alt="꿀생" title="꿀생" />
            <p className="notoReg">꿀생</p>
          </button>
          <button
            type="button"
            onClick={() => changeCategory("item")}
            className={`notoReg ${category === "item" ? "active" : null}`}
          >
            <img src={HoneyTem} alt="꿀템" title="꿀템" />
            <p className="notoReg">꿀템</p>
          </button>
        </div>
        <div className="header-preview flex justify-center">
          <button
            onClick={clickInput}
            type="button"
            className="header-preview_container flex column justify-center align-center"
          >
            <p className="header-preview_container-title notoMid">Thumnail</p>
            {thumnail ? (
              <img src={thumnail} alt="preview" />
            ) : (
              <div className="header-preview_img flex justify-center align-center">
                <img src={noimg} alt="no-img" title="preview" />
              </div>
            )}
            <span className="notoReg">
              jpg, png, gif, jpeg 파일만 지원해요.
            </span>
          </button>
        </div>
      </div>
      <div className="content flex column align-center">
        <p className="notoMid">Content</p>
        <input
          ref={titleRef}
          className="title"
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

export default TipUpdate;
