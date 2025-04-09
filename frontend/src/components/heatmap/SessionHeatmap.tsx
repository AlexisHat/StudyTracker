import { generateHeatmapCells } from "../../utils/heatmapUtils";

type Props = {
  year: string;
};

const SessionHeatmap = ({ year }: Props) => {
  const weekDayOfFirstJan = new Date(`${year}-01-01`).getDay();
  const parsedYear = parseInt(year, 10);
  const isLeap =
    (parsedYear % 4 === 0 && parsedYear % 100 !== 0) || parsedYear % 400 === 0;
  const totalDays = isLeap ? 366 : 365;
  const days = 7;
  const weeks = Math.ceil((totalDays + weekDayOfFirstJan + 1) / days);

  const cells = generateHeatmapCells(weeks, days, totalDays, weekDayOfFirstJan);

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
