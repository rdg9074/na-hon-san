import { TipCondition } from "../store/ducks/infinity/infinity.type";
import API from "./index";

export const reqTipList = async (condition: TipCondition) => {
  const { type, keyword, pageSize, lastIdx, lastView, lastLikes, category } =
    condition;
  let body;
  if (condition.type === "최신순") {
    body = { type, keyword, pageSize, lastIdx, category };
  }
  if (condition.type === "좋아요순") {
    body = { type, keyword, pageSize, lastIdx, category, lastLikes };
  }
  if (condition.type === "조회순") {
    body = { type, keyword, pageSize, lastIdx, category, lastView };
  }
  console.log(body);
  const res = await API.post("/honeyTip/list", body);
  return res.data;
};

export const getTipTotalCnt = async () => {
  const res = await API.get("/honeyTip/totalCount");

  return res.data;
};
