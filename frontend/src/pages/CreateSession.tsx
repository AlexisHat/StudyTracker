import { useEffect, useState } from "react";
import { getTopics } from "../service/TopicService";
import { CreateSessionData } from "../types/session.types";

function CreateSession() {
  const [topics, setTopics] = useState<string[]>([]);
  const [createSessionData, setCreateSessionData] = useState<CreateSessionData>(
    {
      modus: "vorhanden",
      topic: "",
      startzeit: "",
      endzeit: "",
    }
  );

  useEffect(() => {
    getTopics()
      .then(setTopics)
      .catch((error) => {
        console.error("Fehler beim Laden der Topics:", error);
      });
  }, []);

  return (
    <>
      <form>
        <label htmlFor="newFach">
          Neues Fach für dich erstellen(dieses wird für die Session genutzt):
        </label>
        <input type="text" id="fach" name="fach" />

        <label htmlFor="fach">wähle ein Fach für die Session:</label>

        <select id="fach" name="fach">
          {topics.map((t) => (
            <option key={t} value={t}>
              {t}
            </option>
          ))}
        </select>

        <label htmlFor="startzeit">Wann hat die Session gestartet?:</label>
        <input type="time" id="startzeit" name="startzeit" />
        <label htmlFor="endzeit">Wann hat die Session geendet?:</label>
        <input type="time" id="endzeit" name="endzeit" />
      </form>
    </>
  );
}
export default CreateSession;
