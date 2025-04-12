import { useEffect, useState } from "react";
import {
  generateHeatmapCells,
  getMonthLabels,
  getWeekdayLabels,
} from "../../utils/heatmapUtils";
import { getSessionsForYear } from "../../service/SessionService";

type Props = {
  year: string;
};

const SessionHeatmap = ({ year }: Props) => {
  const [sessions, setSessions] = useState<Map<string, number>>(new Map());

  useEffect(() => {
    const loadSessions = async () => {
      try {
        const data = await getSessionsForYear(year);
        setSessions(data);
      } catch (err) {
        console.error(err);
      }
    };

    loadSessions();
  }, []);

  const weekDayOfFirstJan = new Date(`${year}-01-01`).getDay();
  const isLeapYear = parseInt(year, 10) % 4;
  const totalDays = isLeapYear ? 366 : 365;
  const days = 7;
  const weeks = Math.ceil((totalDays + weekDayOfFirstJan + 1) / days);

  const cells = generateHeatmapCells(
    weeks,
    days,
    totalDays,
    weekDayOfFirstJan,
    year,
    sessions
  );
  const weekdayLabels = getWeekdayLabels();
  const monthLabels = getMonthLabels(year, weekDayOfFirstJan);

  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: `auto repeat(${weeks}, 15px)`,
        gridTemplateRows: `auto repeat(${days}, 15px)`,
        gap: "4px",
        alignItems: "center",
      }}
    >
      {monthLabels}
      {weekdayLabels}
      {cells}
    </div>
  );
};

export default SessionHeatmap;
