import { protectedApi } from "../api/api";
import { formatDateString } from "../utils/heatmapUtils";
import { CreateSessionData } from "../types/session.types";

export const getSessionsForYear = async (year: string): Promise<Map<string, number>> => {
    const response = await protectedApi.get(`/sessions/year?year=${year}`);
    const obj: Record<string, number> = response.data;
  
    const result = new Map<string, number>();
    for (const [dateStr, value] of Object.entries(obj)) {
      result.set(formatDateString(new Date(dateStr)), value);
    }

    console.log(result)
  
    return result;
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