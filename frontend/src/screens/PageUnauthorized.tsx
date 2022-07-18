import React from "react";
import MsgPageLayout from "@screens/MsgPageLayout";
import ErrorIcon from "@images/PageError.svg";

function PageUnauthorized() {
  return (
    <div className="wrapper">
      <MsgPageLayout
        title="401"
        subtitle="해당페이지에 대한 권한이 없습니다."
        imgSrc={ErrorIcon}
      />
    </div>
  );
}

export default PageUnauthorized;
