import { reqUserInfo, reqUserMoreInfo } from "@apis/auth";
import { createAsyncThunk } from "@reduxjs/toolkit";

export const getUserInfo = createAsyncThunk("auth/getUserInfo", async () => {
  const res = await reqUserInfo();
  return res.data.data;
});

export const getUserMorInfo = createAsyncThunk(
  "auth/getUserMoreInfo",
  async () => {
    const res = await reqUserMoreInfo();
    return res.data.data;
  }
);
