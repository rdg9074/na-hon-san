import { getTime } from "@utils/getTime";

describe("getTime 함수", () => {
  const addZero = num => (num >= 10 ? num : `0${num}`);

  const now = new Date();
  const nowY = now.getFullYear();
  const nowM = addZero(now.getMonth() + 1);
  const nowD = addZero(now.getDate());
  const nowH = addZero(now.getHours());
  const nowMin = addZero(now.getMinutes());
  const nowS = addZero(now.getSeconds());

  it("1분 미만", () => {
    expect(getTime(`${nowY}-${nowM}-${nowD}T${nowH}:${nowMin}:${nowS}`)).toBe(
      "방금전"
    );
  });
  it("1시간 미만", () => {
    let hour = nowH;
    let min = nowMin - 58;
    if (min < 0) {
      hour -= 1;
      min += 60;
    }
    expect(getTime(`${nowY}-${nowM}-${nowD}T${hour}:${min}:${nowS}`)).toBe(
      "58분전"
    );
  });
  it("1일 미만", () => {
    let month = nowM;
    let day = nowD - 3;
    if (day < 0) {
      month -= 1;
      day += 31;
    }
    day = addZero(day);

    expect(getTime(`${nowY}-${month}-${day}T${nowH}:${nowMin}:${nowS}`)).toBe(
      "3일전"
    );
  });
  it("1년 이상", () => {
    const year = nowY - 14;
    expect(getTime(`${year}-${nowM}-${nowD}T${nowH}:${nowMin}:${nowS}`)).toBe(
      "14년전"
    );
  });
});
