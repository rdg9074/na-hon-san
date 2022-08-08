import React, { useState, useCallback, useRef } from "react";
import { useNavigate } from "react-router-dom";
import Editor from "@components/common/s3Uploader/Editor";
import "./DealEdit.scss";
import noimg from "@images/noimg.svg";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import isImage from "@utils/isImage";
import { dealCreate } from "@apis/honeyDeal";
import LoadingSpinner from "@images/LoadingSpinner.svg";
import X from "@images/X.svg";
import { v4 } from "uuid";
import dealCategory from "@constants/dealCategory";
import { useAppSelector } from "@store/hooks";

function DealEdit() {
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [thumnail, setThumnail] = useState("");
  const [category, setCategory] = useState("");
  const [errMsg, setErrMsg] = useState("");
  const [chk, setChk] = useState(false);
  const [spinner, setSpinner] = useState(false);
  const imgInput = useRef<HTMLInputElement>(null);
  const titleRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const userInfo = useAppSelector(state => state.auth.userInfo);

  const back = () => {
    navigate("/deal");
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
      area: "광주",
      state: "거래 대기",
      category,
      title: titleRef.current?.value as string,
      content: data,
      bannerImg: thumnail.replace("data:image/jpeg;base64,", "")
    };
    const res = await dealCreate(payload);
    console.log(res);
    navigate(`/deal/detail/${res}`);
  };

  return (
    <div id="deal-edit">
      <input type="file" accept="image/*" ref={imgInput} onChange={fileread} />
      {sendFile ? (
        <ImgResizer
          imgfile={sendFile}
          newImgfile={receiveFile}
          imgW={200}
          imgH={200}
        />
      ) : null}
      <div className="deal-header ">
        <div className="deal-header-title notoBold flex">
          <p>
            <span>꿀</span>딜<span> 쓰</span>기
          </p>
        </div>
        <div className="deal-header-category flex">
          <p className="category-label notoMid">Category</p>
          <div className="deal-header-category-box flex">
            {dealCategory.map(item => {
              return (
                <button
                  onClick={() => {
                    setCategory(item);
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
              Thumnail
            </p>
            <div className="deal-header-preview_img flex justify-center align-center">
              {thumnail ? (
                <img
                  className="deal-header-preview_img-img"
                  src={thumnail}
                  alt="preview"
                />
              ) : (
                <img
                  className="deal-header-preview_img-img"
                  src={noimg}
                  alt="no-img"
                  title="preview"
                />
              )}
            </div>
            <span className="notoReg">
              jpg, png, gif, jpeg 파일만 지원해요.
            </span>
          </button>
        </div>
      </div>
      <div className="deal-content flex column align-center">
        <p className="notoMid">Content</p>
        <input
          ref={titleRef}
          className="deal-title"
          type="text"
          placeholder="제목은 30자까지 입력할 수 있어요."
        />
        {errMsg ? <span className="notoReg fs-16">{errMsg}</span> : null}

        <Editor editorValue={receiveValue} getValue={chk} update={`${""}`} />
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

export default DealEdit;
