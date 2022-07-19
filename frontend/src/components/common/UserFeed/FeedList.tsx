import React from "react";
import { v4 } from "uuid";
import "./FeedList.scss";
import FeedListItem from "./FeedListItem";

function FeedList() {
  const dummy: number[] = [1, 2, 3, 4, 5, 6, 7, 8];
  return (
    <div id="feedlist" className="flex justify-center">
      {dummy.map(() => (
        <FeedListItem key={v4()} />
      ))}
    </div>
  );
}

export default FeedList;
