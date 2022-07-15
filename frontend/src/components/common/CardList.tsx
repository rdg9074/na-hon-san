import React from "react";
import { v4 } from "uuid";
import Card from "./Card";
import "./CardList.scss";

function CardList() {
  const dummy: Array<"tip" | "deal"> = [
    "tip",
    "tip",
    "deal",
    "deal",
    "tip",
    "tip"
  ];
  return (
    <div id="card-list" className="flex justify-center">
      {dummy.map((value: "tip" | "deal") => (
        <Card type={value} key={v4()} />
      ))}
    </div>
  );
}

export default CardList;
