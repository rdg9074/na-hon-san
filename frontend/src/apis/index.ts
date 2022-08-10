import axios from "axios";
import { config } from "process";
import { refreshAccessToken } from "./auth";

// export const BASE_URL = "http://i7c208.p.ssafy.io:8083/api"; // 개발 주소
export const BASE_URL = "/api";

const API = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    withCredentials: true
  }
});

API.interceptors.response.use(
  response => {
    return response;
  },
  err => {
    console.log("intercept", err);
    if (err.response.status === 401) {
      refreshAccessToken();
      const accessToken = sessionStorage.getItem("access-token") as string;
      err.config.headers = { Authorization: `${accessToken}` };
      return API(err.config);
    }
    return Promise.reject(err);
  }
);

export default API;
