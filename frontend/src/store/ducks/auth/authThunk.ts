import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import { BASE_URL } from "../../../apis/index";

export const getUserInfo = createAsyncThunk("auth/getUserInfo", async () => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await axios.get(`${BASE_URL}/user`, {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data.data;
});

export const test = {};
