import axios from "axios";

const BASE_URL = "http://34.199.120.230:9999/sseuktudy-0.0.1-SNAPSHOT";
// 임시 테스트 URL입니다.

const API = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    withCredentials: true
  }
});

export default API;
