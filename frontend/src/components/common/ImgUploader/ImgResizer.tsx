import React, { useEffect, useRef } from "react";
import "./ImgResizer.scss";

interface ImgResizerProps {
  imgfile: File;
  newImgfile: any;
  imgW: number;
  imgH: number;
}
function ImgResizer({ imgfile, newImgfile, imgW, imgH }: ImgResizerProps) {
  const canvasRef = useRef<any>(null);

  const newImg = new Image();
  const imgUrl = URL.createObjectURL(imgfile);
  newImg.src = imgUrl;
  newImg.onload = () => {
    const canvas = canvasRef.current;
    const ctx = canvas?.getContext("2d");
    if (ctx && canvas) {
      canvas.width = imgW;
      canvas.height = imgH;
      ctx.fillStyle = "white";
      ctx.fillRect(0, 0, imgW, imgH);
      ctx.drawImage(newImg, 0, 0, imgW, imgH);
      const dataUrl = canvas.toDataURL("image/jpeg");
      console.log(dataUrl);
      newImgfile(dataUrl);
      URL.revokeObjectURL(imgUrl);
    }
  };

  return (
    <div id="resizer">
      <canvas ref={canvasRef}> </canvas>
    </div>
  );
}

export default React.memo(ImgResizer);
