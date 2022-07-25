import { createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";
import { BASE_URL } from "../../../apis/index";

const kakaoClientId = process.env.REACT_APP_KAKAO_CLIENT_ID;
const naverClientId = process.env.REACT_APP_NAVER_CLIENT_ID;
export const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID as string;

const kakaoRedirectUrl = `http://localhost:3000/oauth/kakao`;
const naverRedirectUrl = `http://localhost:3000/oauth/naver`;

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${kakaoRedirectUrl}&response_type=code`;
export const NAVER_AUTH_URL = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${naverClientId}&redirect_uri=${naverRedirectUrl}&state=nahonjan`;
export const loginKakao = async (code: string) => {
  const res = await axios.post(
    `https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=${kakaoClientId}&redirect_uri=${kakaoRedirectUrl}&code=${code}`,
    {
      headers: {
        "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
      }
    }
  );
  console.log(res);
  // 여기서 백엔드 전달
};

export const loginNaver = async (code: string) => {
  console.log(code);
  // 여기서 백엔드 전달
};

export const getUserInfo = createAsyncThunk("auth/getUserInfo", async () => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await axios.get(`${BASE_URL}/user`, {
    headers: { Authorization: `${accessToken}` }
  });
  return res.data.data;
});
