import axios from "axios";

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

export default API;
