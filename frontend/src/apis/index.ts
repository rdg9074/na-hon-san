import axios from "axios";

export const BASE_URL = "https://i7c208.p.ssafy.io:8083/api";

const API = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
    "Access-Control-Allow-Origin": "*",
    withCredentials: true
  }
});

export default API;
