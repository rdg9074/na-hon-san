import API from "./index";

export interface commentForm {
  postIdx: string;
  upIdx: number;
  content: string;
  bannerImg: string | null;
}

export interface commentEditForm {
  content: string;
  bannerImg: string | null;
}

export const commentCreate = async (data: commentForm) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post("/honeyTip/comment", data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export const commentDelete = async (idx: number) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.delete(`/honeyTip/comment/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export const commentEdit = async (idx: number, data: commentEditForm) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.put(`/honeyTip/comment/${idx}`, data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export default {};
