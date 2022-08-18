import React, { useState, useCallback, useRef, useEffect } from "react";
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
  const [category, setCategory] = useState("기타");
  const [errMsg, setErrMsg] = useState("");
  const [tumErr, setTumErr] = useState(false);
  const [chk, setChk] = useState(false);
  const [spinner, setSpinner] = useState(false);
  const imgInput = useRef<HTMLInputElement>(null);
  const titleRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();
  const userInfo = useAppSelector(state => state.auth.userInfo);

  useEffect(() => {
    if (!userInfo) {
      navigate("/login");
    } else if (userInfo && !userInfo.area) {
      navigate("/join/more");
    }
  }, []);

  const back = () => {
    navigate("/deal");
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
      state: "거래 대기",
      category,
      title: titleRef.current?.value as string,
      content: data,
      bannerImg: thumnail.replace("data:image/jpeg;base64,", "")
    };
    const res = await dealCreate(payload);
    navigate(`/deal/detail/${res.data.data.idx}`);
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
        <div className="deal-header-title notoBold flex justify-between">
          <p>
            <span>꿀</span>딜<span> 쓰</span>기
          </p>
        </div>
        <div className="deal-header-category flex">
          <p className="category-label notoMid">카테고리</p>
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
