import { useState } from "react";
import SessionHeatmap from "../components/heatmap/SessionHeatmap";

const SessionOverview = () => {
  const [selectedYear, setSelectedYear] = useState<string>("2025");

  const handleYearSelection = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSelectedYear(event.target.value);
  };

  return (
    <>
      <label htmlFor="year">Wähle ein Jahr aus</label>
      <select id="year" value={selectedYear} onChange={handleYearSelection}>
        <option value="2024">2024</option>
        <option value="2025">2025</option>
      </select>
      <div className="d-flex justify-content-center align-items-center min-vh-100">
        <SessionHeatmap year={selectedYear} />
      </div>
    </>
  );
};

export default SessionOverview;
