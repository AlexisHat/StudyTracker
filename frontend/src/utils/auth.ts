export const setToken = (token: string): void => {
    localStorage.setItem("token", token);
  };
  
  export const getToken = (): string | null => {
    return localStorage.getItem("token");
  };
  
  export const isLoggedIn = (): boolean => {
    return getToken()!== null; 
  };
  
  export const logout = (): void => {
    localStorage.removeItem("token");
  };