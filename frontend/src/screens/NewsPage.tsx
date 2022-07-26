import React from "react";
import { v4 } from "uuid";
import "./NewsPage.scss";
import News from "@components/common/News";

function NewsPage() {
  const dummylist: number[] = [1, 2, 3];
  return (
    <div id="newspage">
      <div className="headline flex justify-center">
        <p className="fs-48 notoBold">
          <span>1인가구</span>와 관련된 최신뉴스들을 읽어보세요.
        </p>
      </div>
      <div className="newspagelist justify-center">
        {dummylist.map(() => (
          <News key={v4()} />
        ))}
      </div>
    </div>
  );
}

export default NewsPage;
