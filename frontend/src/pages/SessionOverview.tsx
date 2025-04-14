import { useState } from "react";
import SessionHeatmap from "../components/heatmap/SessionHeatmap";
import { SessionDayOverview } from "../types/session.types";
import { getSessionsForDay } from "../service/SessionService";
import SessionInfoBox from "../components/SessionInfoBox";
import { getDayIso } from "../utils/heatmapUtils";

const SessionOverview = () => {
  const [selectedYear, setSelectedYear] = useState<string>("2025");
  const [dayInfo, setDayInfo] = useState<SessionDayOverview | null>(null);

  const handleYearSelection = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedYear(event.target.value);
  };

  const handleClick = async (dayIndex: number) => {
    console.log("ICH WURDE AUFGERUFEN");
    try {
      const dayIsoString = getDayIso(dayIndex, selectedYear);
      const data = await getSessionsForDay(dayIsoString);
      console.log(data);
      setDayInfo(data);
    } catch (error: any) {
      console.log(error);
    }
  };

  return (
    <>
      <label htmlFor="year">Wähle ein Jahr aus</label>
      <select id="year" value={selectedYear} onChange={handleYearSelection}>
        <option value="2024">2024</option>
        <option value="2025">2025</option>
      </select>
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <SessionHeatmap year={selectedYear} onCellClick={handleClick} />
      </div>
      {dayInfo && (
        <p>
          {dayInfo?.description} {dayInfo?.duration}
        </p>
      )}
    </>
  );
};

export default SessionOverview;
