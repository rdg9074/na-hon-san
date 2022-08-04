import { DealCondition } from "../store/ducks/infinity/infinity.type";
import API from "./index";

export const reqDealList = async (condition: DealCondition) => {
  const {
    type,
    keyword,
    pageSize,
    lastIdx,
    lastView,
    lastLikes,
    categorys,
    state
  } = condition;
  let body;
  if (condition.type === "최신순") {
    body = { type, keyword, pageSize, lastIdx, categorys, state };
  }
  if (condition.type === "좋아요순") {
    body = { type, keyword, pageSize, lastIdx, categorys, state, lastLikes };
  }
  if (condition.type === "조회순") {
    body = { type, keyword, pageSize, lastIdx, categorys, state, lastView };
  }
  console.log(body);
  const res = await API.post("/honeyDeal/view", body);
  console.log(res);
  return res.data;
};

export const test = {};
