import { useEffect, useState } from "react";
import { getTopics, sendSession } from "../service/TopicService";
import { CreateSessionData } from "../types/session.types";
import { useNavigate } from "react-router-dom";
import LogoutButton from "../components/LogoutButton";
function CreateSession() {
  const [topics, setTopics] = useState<string[]>([]);
  const [createSessionData, setCreateSessionData] = useState<CreateSessionData>(
    {
      modus: "neu",
      newTopic: "",
      startzeit: "",
      endzeit: "",
    }
  );

  const navigate = useNavigate();

  useEffect(() => {
    getTopics()
      .then(setTopics)
      .catch((error) => {
        console.error("Fehler beim Laden der Topics:", error);
      });
  }, []);

  const handleSubmitSession = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();
    try {
      const response = await sendSession(createSessionData);
      console.log("Erstellen der Session erfolgreich", response.data);
      navigate("/");
    } catch (error) {
      console.log("Fehler beim Erstellen der Session", error);
    }
  };

  return (
    <div className="container mt-5">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h2>Neue Lerneinheit erstellen</h2>
        <LogoutButton />
      </div>

      <form onSubmit={handleSubmitSession}>
        <div className="mb-3">
          <label className="form-label d-block">
            Möchtest du ein neues Fach erstellen oder ein vorhandenes auswählen?
          </label>
          <div className="form-check form-check-inline">
            <input
              type="radio"
              id="modusNeu"
              name="modus"
              value="neu"
              className="form-check-input"
              checked={createSessionData.modus === "neu"}
              onChange={() =>
                setCreateSessionData({
                  modus: "neu",
                  newTopic: "",
                  startzeit: createSessionData.startzeit,
                  endzeit: createSessionData.endzeit,
                })
              }
            />
            <label htmlFor="modusNeu" className="form-check-label">
              Neues Fach
            </label>
          </div>
          <div className="form-check form-check-inline">
            <input
              type="radio"
              id="modusVorhanden"
              name="modus"
              value="vorhanden"
              className="form-check-input"
              checked={createSessionData.modus === "vorhanden"}
              onChange={() =>
                setCreateSessionData({
                  modus: "vorhanden",
                  topic: "",
                  startzeit: createSessionData.startzeit,
                  endzeit: createSessionData.endzeit,
                })
              }
            />
            <label htmlFor="modusVorhanden" className="form-check-label">
              Vorhandenes Fach
            </label>
          </div>
        </div>

        {createSessionData.modus === "neu" && (
          <div className="mb-3">
            <label htmlFor="newFach" className="form-label">
              Neues Fach:
            </label>
            <input
              type="text"
              id="newFach"
              name="newFach"
              className="form-control"
              value={createSessionData.newTopic || ""}
              onChange={(e) =>
                setCreateSessionData({
                  ...createSessionData,
                  newTopic: e.target.value,
                })
              }
              required
            />
          </div>
        )}

        {/* Bestehendes Fach */}
        {createSessionData.modus === "vorhanden" && (
          <div className="mb-3">
            <label htmlFor="fach" className="form-label">
              Fach auswählen:
            </label>
            <select
              id="fach"
              name="fach"
              className="form-select"
              value={createSessionData.topic}
              onChange={(e) =>
                setCreateSessionData({
                  ...createSessionData,
                  topic: e.target.value,
                })
              }
              required
            >
              <option value="">Bitte wählen</option>
              {topics.map((t) => (
                <option key={t} value={t}>
                  {t}
                </option>
              ))}
            </select>
          </div>
        )}

        <div className="mb-3">
          <label htmlFor="startzeit" className="form-label">
            Startzeit:
          </label>
          <input
            type="datetime-local"
            id="startzeit"
            name="startzeit"
            className="form-control"
            value={createSessionData.startzeit}
            onChange={(e) =>
              setCreateSessionData({
                ...createSessionData,
                startzeit: e.target.value,
              })
            }
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="endzeit" className="form-label">
            Endzeit:
          </label>
          <input
            type="datetime-local"
            id="endzeit"
            name="endzeit"
            className="form-control"
            value={createSessionData.endzeit}
            onChange={(e) =>
              setCreateSessionData({
                ...createSessionData,
                endzeit: e.target.value,
              })
            }
            required
          />
        </div>

        <div className="d-flex justify-content-between">
          <button type="submit" className="btn btn-primary">
            Session erstellen
          </button>
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={() => navigate("/")}
          >
            Zurück zur Startseite
          </button>
        </div>
      </form>
    </div>
  );
}

export default CreateSession;
