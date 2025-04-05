import { protectedApi } from "../api/api"
import { CreateSessionData } from "../types/session.types";

export const getTopics = async (): Promise<string[]> =>{
    const response = await protectedApi.get("/topics");
  return response.data;
};

export const sendSession = async (data: CreateSessionData) => {
    try {
        const response = await protectedApi.post("/sessions/create", data);
    
        if (response.status === 201) {
          return response.data;
        } else {
          console.error("Fehler: Unerwarteter Statuscode", response.status);
        }
      } catch (error) {
        console.error("Fehler beim Senden der Session:", error);
      }
}