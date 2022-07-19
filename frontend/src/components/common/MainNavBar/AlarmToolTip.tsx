import React from "react";
import { v4 } from "uuid";
import Alarm from "./Alarm";
import "./AlarmToolTip.scss";

function AlarmToolTip() {
  const dummy = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  return (
    <div id="alarm-tool-tip">
      <ul className="alarm-list">
        {dummy.length === 1 ? (
          <p className="no-content flex align-center justify-center notoMid fs-16">
            확인 안한 알림이 없습니다.
          </p>
        ) : (
          dummy.map(() => <Alarm key={v4()} />)
        )}
      </ul>
    </div>
  );
}

export default AlarmToolTip;
