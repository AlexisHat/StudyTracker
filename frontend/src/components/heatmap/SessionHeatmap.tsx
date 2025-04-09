import { generateHeatmapCells } from "../../utils/heatmapUtils";

type Props = {
  year: string;
};

const SessionHeatmap = ({ year }: Props) => {
  const weekDayOfFistJan = new Date(`${year}-01-01`).getDay();

  console.log(weekDayOfFistJan);

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
