import axios from "axios";
import API from "./index";

export const login = async (id: string, password: string) => {
  const res = await axios.post("/user/login", { id, password });

  return res.data;
};

export const emailValidate = async (id: string) => {
  const res = await axios.post("/user/auth", { id, type: 1 });
  return res.data;
};

export const logout = async () => {
  console.log("로그아웃");
};

export const resetPassword = async (newpassword: string) => {
  const res = await axios.post("/user/password", { password: newpassword });

  return res.data;
};
