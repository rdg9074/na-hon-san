import React, { useState, useRef, useCallback, useEffect } from "react";
import imageIcon from "@images/image.svg";
import Clip from "@images/Clip.svg";
import X from "@images/X.svg";
import "./CommentInput.scss";
import { commentEdit, commentCreate } from "@apis/comment";
import isImage from "@utils/isImage";
import ImgResizer from "@components/common/ImgUploader/ImgResizer";
import { useAppSelector } from "@store/hooks";
import { useNavigate } from "react-router-dom";
import { commentType } from "./Comments";

interface CommentEditProps {
  commentInfo: commentType;
  changed: () => void;
  signal: () => void;
  postIdx: string;
  isAuthor: boolean;
  type: string;
}

function CommentEdit({
  commentInfo,
  signal,
  changed,
  postIdx,
  isAuthor,
  type
}: CommentEditProps) {
  const [sendFile, setSendFile] = useState<File | null>(null);
  const [commentImg, setCommentImg] = useState("");
  const [preview, setPreview] = useState(false);
  const [loading, setLoading] = useState(false);

  const inputRef = useRef<HTMLInputElement>(null);
  const imgInput = useRef<HTMLInputElement>(null);
  const isLoggedIn = !!useAppSelector(state => state.auth.userInfo);
  const navigate = useNavigate();

  useEffect(() => {
    if (commentInfo.bannerImg && isAuthor) {
      setCommentImg(`data:image/jpeg;base64,${commentInfo.bannerImg}`);
    }
    if (inputRef.current && isAuthor) {
      inputRef.current.value = commentInfo.content;
    }
  }, []);

  const clickInput = () => {
    imgInput.current?.click();
  };

  const fileread = () => {
    if (imgInput.current?.files && isImage(imgInput.current.files[0])) {
      setSendFile(imgInput.current.files[0]);
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

    inputRef.current.disabled = true;
    if (!loading) {
      setLoading(true);
      if (isAuthor) {
        const data = {
          content: inputRef.current?.value as string,
          bannerImg: commentImg.replace("data:image/jpeg;base64,", "")
        };
        await commentEdit(commentInfo.idx, data, type);
      } else {
        const data = {
          postIdx,
          upIdx: commentInfo.idx,
          content: inputRef.current.value,
          bannerImg: commentImg.replace("data:image/jpeg;base64,", "")
        };
        await commentCreate(data, type);
      }
      changed();
      setLoading(false);
      inputRef.current.value = "";
      setCommentImg("");
      inputRef.current.disabled = false;
    }
    return 1;
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
      {commentImg ? (
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
      ) : null}
      <input
        onKeyDown={submit}
        type="text"
        placeholder="댓글을 입력해주세요."
        ref={inputRef}
      />
      <button type="button" onClick={clickInput}>
        <img src={imageIcon} alt="img-upload" title="upload" />
      </button>
    </div>
  );
}

export default CommentEdit;
