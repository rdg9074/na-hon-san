import { resetUserInfo } from "@store/ducks/auth/authSlice";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { deleteRefreshToken, refreshAccessToken } from "./auth";

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
export const setUpInterceptors = (store: any) => {
  const { dispatch } = store;
  API.interceptors.response.use(
    response => {
      return response;
    },
    async err => {
      const originalRequest = err.config;
      if (err.response.data.message === "refreshTimeout") {
        dispatch(resetUserInfo());
        deleteRefreshToken();
        window.location.href = "/";
      } else if (err.response.status === 401) {
        await refreshAccessToken();
        const accessToken = sessionStorage.getItem("access-token");
        originalRequest.headers = {
          ...originalRequest.headers,
          Authorization: accessToken
        };
        return axios(originalRequest);
      }
      return Promise.reject(err);
    }
  );
};
export default API;
