import { protectedApi } from "../api/api"

export const getTopics = async (): Promise<string[]> =>{
    const response = await protectedApi.get("/topics");
  return response.data;
};