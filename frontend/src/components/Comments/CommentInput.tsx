import React, { useState, useRef, useCallback, useEffect } from "react";
import imageIcon from "@images/image.svg";
import Clip from "@images/Clip.svg";
import X from "@images/X.svg";
import "./CommentInput.scss";
import isImage from "@utils/isImage";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import { commentCreate } from "@apis/comment";
import { useAppSelector } from "@store/hooks";
import { useNavigate } from "react-router-dom";

interface CommentInputProps {
  articleIdx: string;
  changed: () => void;
  type: string;
}

function CommentInput({ articleIdx, changed, type }: CommentInputProps) {
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [commentImg, setCommentImg] = useState("");
  const [preview, setPreview] = useState(false);
  const [loading, setLoading] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);
  const imgInput = useRef<HTMLInputElement>(null);
  const isLoggedIn = !!useAppSelector(state => state.auth.userInfo);
  const navigate = useNavigate();

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
    setCommentImg(data);
  }, []);

  const hoverd = useCallback(() => {
    setPreview(cur => !cur);
  }, []);

  const submit = async (e: React.KeyboardEvent) => {
    if (e.key !== "Enter") {
      return inputRef.current?.focus();
    }
    if (!isLoggedIn) {
      return navigate("/login");
    }
    if (!inputRef.current?.value.trim()) {
      return inputRef.current?.focus();
    }
    if (!loading) {
      setLoading(true);
      const data = {
        postIdx: articleIdx,
        upIdx: 0,
        content: inputRef.current.value,
        bannerImg: commentImg.replace("data:image/jpeg;base64,", "")
      };
      inputRef.current.disabled = true;
      await commentCreate(data, type);
      setLoading(false);
      changed();
      inputRef.current.value = "";
      setCommentImg("");
      inputRef.current.disabled = false;
    }
    return 0;
  };

  const deletePreview = () => {
    setCommentImg("");
  };

  return (
    <div id="comment-input">
      {preview && <img className="preview" src={commentImg} alt="preview" />}
      <input type="file" accept="image/*" ref={imgInput} onChange={fileread} />
      {sendFile && (
        <ImgResizer
          imgfile={sendFile}
          newImgfile={receiveFile}
          imgW={200}
          imgH={200}
        />
      )}
      {commentImg && (
        <div className="flex img-preview">
          <img
            className=" "
            src={Clip}
            alt="add"
            onMouseEnter={hoverd}
            onMouseLeave={hoverd}
          />
          <button type="button" onClick={deletePreview}>
            <img src={X} alt="del" />
          </button>
        </div>
      )}
      <input
        type="text"
        placeholder="댓글을 입력해주세요."
        ref={inputRef}
        onKeyDown={e => submit(e)}
      />
      <button type="button" onClick={clickInput}>
        <img src={imageIcon} alt="img-upload" title="upload" />
      </button>
    </div>
  );
}

export default CommentInput;
