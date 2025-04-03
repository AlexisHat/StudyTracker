import axios from "axios";
import { getToken } from "../utils/auth";

export const authApi = axios.create({
    baseURL : "http://localhost:8080/api/auth",
    timeout : 5000,
    headers: {
        "Content-Type": "application/json",
      },
});

export const protectedApi = axios.create({
  baseURL: "http://localhost:8080/api/protected",
  timeout: 5555,
  headers:{
    "Content-Type": "application/json",
  }
})

protectedApi.interceptors.request.use(
  (config) => {
    const token = getToken();
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);