import { getDummy } from "@apis/dummy";
import React, { useEffect, useState } from "react";
import { v4 } from "uuid";
import Card from "./Card";
import "./CardList.scss";
import CardSkeleton from "./CardSkeleton";

type CardListProps = {
  type: "tip" | "deal";
};

function CardList({ type }: CardListProps) {
  const [dummy, setDummy] = useState<Array<any>>([]);
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    (async () => {
      const res = await getDummy();
      setDummy(res);
      setIsLoading(true);
    })();
  }, []);
  return (
    <div id="card-list" className="flex justify-center">
      {isLoading
        ? dummy.map(value => <Card type={type} data={value} key={v4()} />)
        : [0, 1, 2, 3, 4, 5].map(() => <CardSkeleton key={v4()} />)}
    </div>
  );
}

export default CardList;
