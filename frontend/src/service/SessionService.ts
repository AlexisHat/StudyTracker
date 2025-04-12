import { protectedApi } from "../api/api";
import { formatDateString } from "../utils/heatmapUtils";

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