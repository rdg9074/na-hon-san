import { getDummy } from "@apis/dummy";
import { getTipList } from "@apis/tip";
import React, { useEffect, useState } from "react";
import { v4 } from "uuid";
import Card, { CardProps } from "./Card";
import "./CardList.scss";
import CardSkeleton from "./CardSkeleton";

export type conditionType = {
  sort: string;
  searchType: "deal" | "tip";
  keyword: string | null;
  searchCategory: string;
  page: number;
};

type CardListProps = conditionType & {
  handleSpinner: () => void;
  handleIsEnd: () => void;
};

function CardList({
  searchType,
  sort,
  keyword,
  searchCategory,
  page,
  handleSpinner,
  handleIsEnd
}: CardListProps) {
  const [cards, setCards] = useState<Array<CardProps>>([]);
  const [isLoading, setIsLoading] = useState(false);
  useEffect(() => {
    (async () => {
      if (searchType === "tip") {
        const res = await getTipList(searchCategory, keyword, sort, page);
        if (res.isEnd) handleIsEnd();
        setCards(res.data);
        setIsLoading(true);
        handleSpinner();
      }
    })();
  }, []);
  return (
    <div id="card-list" className="flex">
      {isLoading
        ? cards.map(
            ({
              idx,
              userNickname,
              userProfileImg,
              title,
              category,
              bannerImg,
              like,
              comment,
              view
            }) => (
              <Card
                type={searchType}
                idx={idx}
                userNickname={userNickname}
                userProfileImg={userProfileImg}
                title={title}
                bannerImg={bannerImg}
                like={like}
                comment={comment}
                view={view}
                category={category}
                key={v4()}
              />
            )
          )
        : [0, 1, 2, 3, 4, 5].map(() => <CardSkeleton key={v4()} />)}
    </div>
  );
}

export default React.memo(CardList);
