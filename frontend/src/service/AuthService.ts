import api from "../api/api"
import { RegisterData, LoginData } from "../types/auth.types";

export const login = (data: LoginData) => {
    return api.post("/api/auth/login", data);
  };
  
  export const register = (data: RegisterData) => {
    return api.post("/api/auth/register", data);
  };
  