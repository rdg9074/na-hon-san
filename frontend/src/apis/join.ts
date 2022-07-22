import axios from "axios";
import API from "./index";

export const sendAuthCode = async (id: string, type: number) => {
  const res = await API.post(`/user/auth`, { id, type });
  return res.data.message;
};

export const chkAuthCode = async (id: string, number: string, type: number) => {
  const res = await API.get(
    `/user/auth?id=${id}&number=${number}&type=${type}`
  );
  return res.data.message;
};

export const chkNickNameExist = async (nickName: string) => {
  const res = await API.get(`/user/check/${nickName}`);

  return res.data.message;
};

export const join = async (id: string, password: string, nickname: string) => {
  const res = await API.post("/user", { id, password, nickname });
  return res.data.message;
};
