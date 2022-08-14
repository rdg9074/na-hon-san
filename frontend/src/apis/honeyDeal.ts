import API from "./index";
import { createForm, Article } from "./honeyTip";
import { DealCondition } from "../store/ducks/infinity/infinity.type";

export interface dealCreateForm extends createForm {
  state: string;
  area: string;
}

export interface dealArticle extends Article {
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
  state: string;
  area: string;
}

export const dealMap = async (username: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get(`/honeyDeal/position/${username}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const dealCreate = async (data: dealCreateForm) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post("/honeyDeal", data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const dealUpdate = async (data: dealCreateForm, idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.put(`/honeyDeal/${idx}`, data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const dealRead = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  if (accessToken) {
    const res = await API.get(`/honeyDeal/detail/${idx}`, {
      headers: {
        Authorization: accessToken
      }
    });
    return res.data;
  }
  const res = await API.get(`/honeyDeal/detail/${idx}`);
  return res.data;
};

export const dealDelete = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.delete(`/honeyDeal/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data.message;
};

export const dealLike = async (idx: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get(`/honeyDeal/like/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res;
};

export const reqDealList = async (condition: DealCondition) => {
  const {
    type,
    keyword,
    pageSize,
    lastIdx,
    lastView,
    lastLikes,
    categorys,
    state,
    area
  } = condition;
  let body;
  if (condition.type === "최신순") {
    body = { type, keyword, pageSize, lastIdx, categorys, state, area };
  }
  if (condition.type === "좋아요순") {
    body = {
      type,
      keyword,
      pageSize,
      lastIdx,
      categorys,
      state,
      lastLikes,
      area
    };
  }
  if (condition.type === "조회순") {
    body = {
      type,
      keyword,
      pageSize,
      lastIdx,
      categorys,
      state,
      lastView,
      area
    };
  }
  const res = await API.post("/honeyDeal/view", body);
  return res.data;
};

export const areaCount = async (area: string) => {
  const res = await API.get(`/honeyDeal/count/${area}`);
  return res.data;
};

export default {};
