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

export const commentCreate = async (data: commentForm, type: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.post(`/honey${type}/comment`, data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export const commentDelete = async (idx: number, type: string) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.delete(`/honey${type}/comment/${idx}`, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export const commentEdit = async (
  idx: number,
  data: commentEditForm,
  type: string
) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.put(`/honey${type}/comment/${idx}`, data, {
    headers: {
      Authorization: accessToken
    }
  });
  return res.data;
};

export default {};
