export type CreateSessionData =
  | {
      modus: "neu";
      newTopic: string;
      topic?: never;
      startzeit: string;
      endzeit: string;
    }
  | {
      modus: "vorhanden";
      newTopic?: never;
      topic: string;
      startzeit: string;
      endzeit: string;
    };