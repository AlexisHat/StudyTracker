import { useEffect, useState } from "react";
import {
  formatDateString,
  generateHeatmapCells,
  getMonthLabels,
  getWeekdayLabels,
} from "../../utils/heatmapUtils";
import { getSessionsForYear } from "../../service/SessionService";

type Props = {
  year: string;
};

type TooltipData = {
  monthAsString: string;
  dayAsString: string;
  duration: number;
  x: number;
  y: number;
};

const SessionHeatmap = ({ year }: Props) => {
  const [sessions, setSessions] = useState<Map<string, number>>(new Map());
  const [tooltipData, setTooltipData] = useState<TooltipData | null>(null);

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
  }, [year]);

  const weekDayOfFirstJan = new Date(`${year}-01-01`).getDay();
  const isLeapYear = parseInt(year, 10) % 4;
  const totalDays = isLeapYear ? 366 : 365;
  const days = 7;
  const weeks = Math.ceil((totalDays + weekDayOfFirstJan + 1) / days);

  const handleHoverCell = (dayIndex: number, event: React.MouseEvent) => {
    const x = event.clientX;
    const y = event.clientY;
    const firstDay = new Date(`${year}-01-01`);
    firstDay.setDate(firstDay.getDate() + dayIndex);
    const monthAsString = firstDay.toLocaleString("de-DE", { month: "long" });
    const dayAsString = firstDay.toLocaleString("de-DE", { day: "2-digit" });
    const duration = sessions.get(formatDateString(firstDay)) ?? 0;
    setTooltipData({ monthAsString, dayAsString, duration, x, y });
  };

  const handleLeaveCell = () => {
    setTooltipData(null);
  };

  const cells = generateHeatmapCells(
    weeks,
    days,
    totalDays,
    weekDayOfFirstJan,
    year,
    sessions,
    handleHoverCell,
    handleLeaveCell
  );

  const weekdayLabels = getWeekdayLabels();
  const monthLabels = getMonthLabels(year, weekDayOfFirstJan);

  return (
    <div style={{ position: "relative" }}>
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

      {tooltipData && (
        <div
          style={{
            position: "fixed",
            top: tooltipData.y + 10,
            left: tooltipData.x + 10,
            backgroundColor: "rgba(0, 0, 0, 0.75)",
            color: "#fff",
            padding: "5px",
            borderRadius: "3px",
            pointerEvents: "none",
            fontSize: "12px",
            zIndex: 1000,
          }}
        >
          Am {tooltipData.dayAsString} {tooltipData.monthAsString} hast du:{" "}
          {tooltipData.duration} min geübt!
        </div>
      )}
    </div>
  );
};

export default SessionHeatmap;
