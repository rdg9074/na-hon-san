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

export const login = async (id: string, password: string) => {
  const res = await API.post("/user/login", { id, password });
  if (res.data.message === "SUCCESS") {
    sessionStorage.setItem("access-token", res.data["access-token"]);
  }
  return res.data.message;
};

export const logout = async () => {
  console.log("로그아웃");
};

export const resetPassword = async (id: string, newpassword: string) => {
  const res = await API.put("/user/password", { id, password: newpassword });
  return res.data.message;
};
