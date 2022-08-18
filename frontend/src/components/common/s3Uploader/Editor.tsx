import React, { useState, useEffect, useRef } from "react";
import ReactQuill from "react-quill";
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
  const quillRef = useRef<any>(null);
  const toolbarOptions = [["image"], [{ color: [] }, { background: [] }]];
  const modules = {
    toolbar: {
      container: toolbarOptions
    }
  };
  useEffect(() => {
    const handleImage = () => {
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
          const canvas = document.createElement("canvas");
          newImg.onload = async () => {
            const ctx = canvas.getContext("2d");
            canvas.width = 400;
            canvas.height = 400;
            ctx?.drawImage(newImg, 0, 0, 400, 400);
            const dataUrl = canvas.toDataURL("image/jpeg");
            URL.revokeObjectURL(imgUrl);
            const newFile = new File([dataurlToBlob(dataUrl)], v1());
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
    }
  }, []);

  useEffect(() => {
    if (update) {
      setValue(update);
      const quill = quillRef.current;
      const delta = quill.getEditor().clipboard.convert(update);
      quill.getEditor().setContents(delta, "silent");
    }
  }, [update]);

  const formats = ["header", "background", "color", "image", "width"];

  if (getValue) {
    tmpImg.shift();
    tmpImg.forEach(async item => {
      if (!value.includes(item)) {
        await deleteFile(item);
      }
    });
    editorValue(value);
  }

  const isPaste = (e: React.ClipboardEvent<HTMLDivElement>) => {
    e.preventDefault();
    alert("복사 붙여넣기는 지원하지 않습니다.");
  };

  return (
    <div id="editor">
      <div className="editor" onPaste={e => isPaste(e)}>
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
