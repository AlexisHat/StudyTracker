import React from "react";
import { generateHeatmapCells } from "../../utils/heatmapUtils";

const SessionHeatmap: React.FC = () => {
  const weeks = 52;
  const days = 7;
  const total = 365;

  const cells = generateHeatmapCells(weeks, days, total);

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: `repeat(${weeks}, 15px)`,
        gridTemplateRows: `repeat(${days}, 15px)`,
        gap: "4px",
      }}
    >
      {cells}
    </div>
  );
};

export default SessionHeatmap;
