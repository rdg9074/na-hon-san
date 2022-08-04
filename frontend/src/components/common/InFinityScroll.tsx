import React, { useRef, useEffect, useCallback, useState } from "react";
import loadingSpinner from "@images/LoadingSpinner.svg";
import { useAppDispatch, useAppSelector } from "@store/hooks";
import {
  resetInfinity,
  setConditionList,
  setIsLoading,
  setUuid
} from "@store/ducks/infinity/infinitySlice";
import CardList from "./CardList";

type InFinityScrollProps = {
  type: string;
  searchType: "deal" | "tip";
  keyword: string | null;
  category: string | undefined;
  categorys: Array<string> | undefined;
  state: string | undefined;
};

function InFinityScroll({
  type,
  searchType,
  keyword,
  category,
  categorys,
  state
}: InFinityScrollProps) {
  const observerTarget = useRef<HTMLDivElement>(null);
  const dispatch = useAppDispatch();
  const [firstLender, setFirstLender] = useState(false);
  const { uuid, isEnd, isLoading, conditionList, lastIdx } = useAppSelector(
    ({ infinity }) => ({
      uuid: infinity.uuid,
      isLoading: infinity.isLoading,
      isEnd: infinity.isEnd,
      conditionList: infinity.conditionList,
      lastIdx: infinity.lastIdx
    })
  );
  const onIntersect = (entries: IntersectionObserverEntry[]) => {
    entries.forEach((entry: IntersectionObserverEntry) => {
      if (entry.isIntersecting && !isLoading) {
        dispatch(setUuid());
      }
    });
  };

  useEffect(() => {
    dispatch(resetInfinity());
    setFirstLender(true);
  }, [type, searchType, keyword, category, categorys, state]);

  useEffect(() => {
    if (!isLoading) {
      dispatch(setIsLoading(true));
      dispatch(
        setConditionList({
          type,
          keyword,
          category,
          categorys,
          state
        })
      );
    }
  }, [uuid]);

  useEffect(() => {
    const observer = new IntersectionObserver(onIntersect, { threshold: 0.1 });
    if (observerTarget.current) {
      observer.observe(observerTarget.current);
    }
    return () => {
      observer.disconnect();
    };
  }, [isLoading]);

  return (
    <div id="infinity-study-card-list">
      <ul className="flex column">
        {firstLender &&
          conditionList.length !== 0 &&
          conditionList.map(condition => (
            <CardList
              searchType={searchType}
              condition={condition}
              key={condition.lastIdx}
              pure={false}
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
