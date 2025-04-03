import authApi from "../api/api"
import { RegisterData, LoginData } from "../types/auth.types";

export const login = (data: LoginData) => {
    return authApi.post("/login", data);
  };
  
  export const register = (data: RegisterData) => {
    return authApi.post("/register", data);
  };
  