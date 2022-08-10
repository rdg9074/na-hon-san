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
  async err => {
    const originalRequest = err.config;
    if (err.response.status === 401) {
      await refreshAccessToken();
      return API(originalRequest);
    }
    return Promise.reject(err);
  }
);

export default API;
