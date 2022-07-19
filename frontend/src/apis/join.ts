import API from "./index";

export const chkEmailExist = async (userId: string) => {
  const res = await API.get(`/emailCheck?userId=${userId}`);
  return res.data.message;
};

export const sendAuthCode = (userId: string) => {
  API.post(`/email`, { userId });
};

export const chkAuthCode = async (
  userId: string,
  authCode: string | undefined
) => {
  const res = await API.get(`/email?userId=${userId}&authKey=${authCode}`);
  return res.data.message;
};

export const chkNickNameExist = async (nickName: string) => {
  return true;
};

export const join = async () => {
  return true;
};
