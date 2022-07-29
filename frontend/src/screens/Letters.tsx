import Letter, { LetterProps } from "@components/Letters/Letter";
import React, { useEffect, useState } from "react";
import "./Letters.scss";
import { v4 } from "uuid";
import { getDmList } from "@apis/dm";

function Letters() {
  const [dmList, setDmList] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  useEffect(() => {
    (async () => {
      const res = await getDmList();
      if (res.message === "SUCCESS") {
        setIsLoading(false);
        setDmList(res.data);
      }
    })();
  }, []);
  return (
    <div className="wrapper">
      <div id="letters">
        <header className="header">
          <p className="letters__title notoBold fs-28">쪽지함</p>
        </header>
        <div className="letters-list">
          {dmList.length !== 0 ? (
            dmList.map((dm: LetterProps) => (
              <Letter
                toId={dm.toId}
                fromId={dm.fromId}
                content={dm.content}
                read={dm.read}
                count={dm.count}
                time={dm.time}
                nickname={dm.nickname}
                key={v4()}
              />
            ))
          ) : (
            <div className="no-dm flex align-center justify-center fs-24">
              {!isLoading && "받은 쪽지가 없습니다."}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default Letters;
