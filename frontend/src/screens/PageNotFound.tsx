import React from "react";
import MsgPageLayout from "@screens/MsgPageLayout";
import ErrorIcon from "@images/PageError.svg";

function PageNotFound() {
  return (
    <div className="wrapper">
      <MsgPageLayout
        title="404"
        subtitle="페이지를 찾을 수 없습니다"
        imgSrc={ErrorIcon}
      />
    </div>
  );
}

export default PageNotFound;
