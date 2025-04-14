export type CreateSessionData =
  | {
      modus: "neu";
      newTopic: string;
      topic?: never;
      startzeit: string;
      endzeit: string;
      description?: string;
    }
  | {
      modus: "vorhanden";
      newTopic?: never;
      topic: string;
      startzeit: string;
      endzeit: string;
      description?: string;
    };

export type SessionDayOverview = {
  topic: string;
  duration: number;
  description: string;
  startTime: string;
  endTime: string;
}