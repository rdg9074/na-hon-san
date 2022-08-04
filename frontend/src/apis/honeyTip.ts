import API from "./index";

interface createForm {
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

export default {};
