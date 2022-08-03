import React, { useRef, useState, useEffect, useCallback } from "react";
import loadingSpinner from "@images/LoadingSpinner.svg";
import CardList, { conditionType } from "./CardList";

type InFinityScrollProps = {
  sort: string;
  searchType: "deal" | "tip";
  keyword: string | null;
  searchCategory: string;
};

function InFinityScroll({
  sort,
  searchType,
  keyword,
  searchCategory
}: InFinityScrollProps) {
  const [conditionList, setConditionList] = useState<Array<conditionType>>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isEnd, setIsEnd] = useState(false);
  const [page, setPage] = useState(0);
  const observerTarget = useRef<HTMLDivElement>(null);

  const onIntersect = (entries: IntersectionObserverEntry[]) => {
    entries.forEach((entry: IntersectionObserverEntry) => {
      if (entry.isIntersecting && !isLoading) {
        setPage(prev => prev + 1);
      }
    });
  };

  useEffect(() => {
    if (page === -1) {
      setPage(0);
      return;
    }
    setIsLoading(true);
    setConditionList([
      ...conditionList,
      { sort, searchType, keyword, searchCategory, page }
    ]);
  }, [page]);

  useEffect(() => {
    const observer = new IntersectionObserver(onIntersect, { threshold: 0.1 });
    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }
    return () => {
      observer.disconnect();
    };
  }, [isLoading]);

  useEffect(() => {
    setConditionList(() => []);
    setPage(-1);
    setIsEnd(false);
  }, [sort, searchCategory, keyword]);

  const handleSpinner = useCallback(() => setIsLoading(false), []);
  const handleIsEnd = useCallback(() => setIsEnd(true), []);
  return (
    <div id="infinity-study-card-list">
      <ul className="flex column">
        {conditionList.length !== 0 &&
          conditionList.map(condition => (
            <CardList
              sort={condition.sort}
              searchType={searchType}
              keyword={condition.keyword}
              searchCategory={condition.searchCategory}
              page={condition.page}
              key={condition.page}
              handleSpinner={handleSpinner}
              handleIsEnd={handleIsEnd}
            />
          ))}
      </ul>
      {!isEnd && (
        <div className="flex justify-center">
          {isLoading ? (
            <img
              src={loadingSpinner}
              title="로딩스피너"
              alt="로딩스피너"
              className="loading-spinner"
            />
          ) : (
            <div ref={observerTarget} className="observerTarget" />
          )}
        </div>
      )}
    </div>
  );
}

export default InFinityScroll;
