import React, { useState, useEffect, useRef, useCallback } from "react";
import ReactQuill, { Quill } from "react-quill";
import "react-quill/dist/quill.snow.css";
import { dataurlToBlob } from "@utils/resizer";
import { v1 } from "uuid";
import { deleteFile, uploadFile } from "./awsS3";
import "./Editor.scss";

interface EditorProps {
  editorValue: (value: string) => void;
  getValue: boolean;
  update: string;
}

function Editor({ editorValue, getValue, update }: EditorProps) {
  const [value, setValue] = useState("");
  const [tmpImg, setTmpImg] = useState([""]);

  // 에디터 커스텀
  const quillRef = useRef<any>(null);
  const toolbarOptions = [
    ["image"],
    [{ color: [] }, { background: [] }],
    [{ align: [] }]
  ];
  const modules = {
    toolbar: {
      container: toolbarOptions
    }
  };
  useEffect(() => {
    // 에디터 이미지 첨부 시 커스텀 이미지핸들러 실행

    const handleImage = () => {
      // 인풋 만들어서 업로드
      const input = document.createElement("input");
      input.setAttribute("type", "file");
      input.setAttribute("accept", "image/*");
      input.click();
      input.onchange = async () => {
        if (input.files) {
          const quill = quillRef.current;
          const range = quill.getEditor().getSelection(true);
          const file = input.files[0];
          const newImg = new Image();
          const imgUrl = URL.createObjectURL(file);
          newImg.src = imgUrl;

          // 캔버스에 그리면서 리사이징, 컴포넌트로 사용하던거랑 조금 달라서 따로 구현
          const canvas = document.createElement("canvas");
          newImg.onload = async () => {
            const ctx = canvas.getContext("2d");
            canvas.width = 400;
            canvas.height = 400;
            ctx?.drawImage(newImg, 0, 0, 400, 400);
            const dataUrl = canvas.toDataURL("image/jpeg");
            URL.revokeObjectURL(imgUrl);

            // 캔버스로 그리면 dataurl 생성, 생성 된 dataurl > Blob > File 순으로 변경
            const newFile = new File([dataurlToBlob(dataUrl)], v1());
            console.log(newFile);
            const url = await uploadFile(newFile);
            setTmpImg(cur => [...cur, url]);
            quill.getEditor().insertEmbed(range.index, "image", url);
            quill.getEditor().setSelection(range.index + 1);
          };
        }
      };
    };

    if (quillRef.current) {
      const toolbar = quillRef.current.getEditor().getModule("toolbar");
      toolbar.addHandler("image", handleImage);
      console.log(update);
    }
  }, []);

  // update 프롭스가 전달되면 디폴트값 설정
  useEffect(() => {
    if (update) {
      setValue(update);
      const quill = quillRef.current;
      const delta = quill.getEditor().clipboard.convert(update);
      quill.getEditor().setContents(delta, "silent");
    }
  }, [update]);

  const formats = [
    "header",
    "align",
    "indent",
    "background",
    "color",
    "image",
    "width"
  ];

  if (getValue) {
    tmpImg.shift();
    tmpImg.forEach(async item => {
      if (!value.includes(item)) {
        await deleteFile(item);
      }
    });
    editorValue(value);
  }

  return (
    <div id="editor">
      <button
        type="button"
        onClick={() => {
          console.log(value);
        }}
      >
        asdasd
      </button>
      <div className="editor">
        <ReactQuill
          theme="snow"
          defaultValue={value}
          modules={modules}
          formats={formats}
          ref={quillRef}
          onChange={setValue}
        />
      </div>
    </div>
  );
}

export default React.memo(Editor);
