import API from "./index";
import { createForm, Article } from "./honeyTip";

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

export const dealCreate = async (data: dealCreateForm) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post("/honeyDeal", data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
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

export default {};
