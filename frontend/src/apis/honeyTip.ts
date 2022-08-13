import API from "./index";
import { TipCondition } from "../store/ducks/infinity/infinity.type";

export interface createForm {
  category: string;
  title: string;
  content: string;
  bannerImg: string;
}

export interface Article {
  bannerImg: string;
  category: string;
  comment: number;
  content: string;
  like: number;
  time: string;
  title: string;
  updateTime: null | string;
  userNickname: string;
  userProfileImg: string;
  view: number;
}

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
  const res = await API.post("/honeyTip/list", body);
  return res.data;
};

export const getTipTotalCnt = async () => {
  const res = await API.get("/honeyTip/totalCount");

  return res.data;
};

export const reqFollowTipList = async (condition: TipCondition) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get(
    `/mainFeed/honeyTip?lastIdx=${
      condition.lastIdx === null ? 0 : condition.lastIdx
    }&pageSize=${condition.pageSize}`,
    {
      headers: { Authorization: `${accessToken}` }
    }
  );
  return res.data;
};

export const tipCreate = async (data: createForm) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post("/honeyTip", data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data.postIdx;
};

export const tipUpdate = async (data: createForm, idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.put(`/honeyTip/${idx}`, data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const tipRead = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  if (accessToken) {
    const res = await API.get(`/honeyTip/detail/${idx}`, {
      headers: {
        Authorization: accessToken
      }
    });
    return res.data;
  }
  const res = await API.get(`/honeyTip/detail/${idx}`);
  return res.data;
};

export const tipDelete = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.delete(`/honeyTip/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data.message;
};

export const tipLike = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get(`/honeyTip/like/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export default {};
