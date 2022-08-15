import axios from "axios";
import API from "./index";

const kakaoClientId = process.env.REACT_APP_KAKAO_CLIENT_ID;
const naverClientId = process.env.REACT_APP_NAVER_CLIENT_ID;
export const googleClientId = process.env.REACT_APP_GOOGLE_CLIENT_ID as string;

const clientUrl = "https://i7c208.p.ssafy.io";
export const kakaoRedirectUrl = `${clientUrl}/oauth/kakao`;
export const naverRedirectUrl = `${clientUrl}/oauth/naver`;

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?client_id=${kakaoClientId}&redirect_uri=${kakaoRedirectUrl}&response_type=code`;
export const NAVER_AUTH_URL = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${naverClientId}&redirect_uri=${naverRedirectUrl}&state=nahonjan`;

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

export const resetPassword = async (id: string, newpassword: string) => {
  const res = await API.put("/user/password", { id, password: newpassword });
  return res.data.message;
};

export const setUserMoreInfo = async (
  area: string,
  categorys: Array<string>
) => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.put(
    "/user/more",
    { area, categorys },
    {
      headers: { Authorization: `${accessToken}` }
    }
  );
  return res.data.message;
};

export const reqUserInfo = async () => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get("user", {
    headers: { Authorization: `${accessToken}` }
  });
  return res;
};

export const reqUserMoreInfo = async () => {
  const accessToken = sessionStorage.getItem("access-token") as string;
  const res = await API.get("/user/more", {
    headers: { Authorization: `${accessToken}` }
  });
  return res;
};

export const loginWithSocial = async (type: string, authToken: string) => {
  const res = await API.post(`/${type}`, {}, { headers: { authToken } });
  if (res.data.message === "SUCCESS") {
    sessionStorage.setItem("access-token", res.data["access-token"]);
  }
  return res.data;
};

export const getKakaoToken = async (code: string) => {
  const res = await axios.post(
    `https://kauth.kakao.com/oauth/token?grant_type=authorization_code&client_id=${kakaoClientId}&redirect_uri=${kakaoRedirectUrl}&code=${code}`,
    {
      headers: {
        "Content-type": "application/x-www-form-urlencoded;charset=utf-8"
      }
    }
  );
  return res.data.access_token;
};

export const refreshAccessToken = async () => {
  const res = await API.get("/user/login");
  if (res.data.message === "SUCCESS") {
    sessionStorage.setItem("access-token", res.data["access-token"]);
  }
};

export const deleteRefreshToken = async () => {
  const res = await API.get("/user/logout");
};
