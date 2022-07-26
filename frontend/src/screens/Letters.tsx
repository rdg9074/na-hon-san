import Letter from "@components/Letters/Letter";
import React from "react";
import "./Letters.scss";
import { v4 } from "uuid";

function Letters() {
  const dummy = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  return (
    <div className="wrapper">
      <div id="letters">
        <header className="header">
          <p className="letters__title notoBold fs-28">쪽지함</p>
        </header>
        <div className="letters-list">
          {dummy.map(() => (
            <Letter key={v4()} />
          ))}
        </div>
      </div>
    </div>
  );
}

export default Letters;
