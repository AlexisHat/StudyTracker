import HeatmapCell from "../components/heatmap/HeatmapCell";

export const generateHeatmapCells = (
  weeks: number,
  days: number,
  totalDays: number,
  daysToSkipBeginning: number,
  year: string,
  sessions: Map<string, number>
) => {
  const firstDay = new Date(`${year}-01-01`);
  const cells = [];

  for (let col = 0; col < weeks; col++) {
    for (let row = 0; row < days; row++) {
      const dayIndex = col * days + row;

      const currentDay = new Date(firstDay);
      currentDay.setDate(currentDay.getDate() + dayIndex);
      const dayIso = formatDateString(currentDay);
      console.log(dayIso);
      if (dayIndex >= totalDays + daysToSkipBeginning) break;
      if (dayIndex < daysToSkipBeginning) {
        cells.push(
          <HeatmapCell
            key={dayIndex}
            col={col + 1}
            row={row + 1}
            color="transparent"
            dayIndex={dayIndex}
          />
        );
      } else {
        const duration = sessions.get(dayIso);
        const color = getColorForDuration(duration ?? 0);
        cells.push(
          <HeatmapCell
            key={dayIndex}
            col={col + 1}
            row={row + 1}
            color={color}
            dayIndex={dayIndex}
          />
        );
      }
    }
  }

  return cells;
};

const getColorForDuration = (duration: number) => {
  if (duration === 0) {
    return "#FFFFFF";
  } else if (duration < 15) {
    return "#cae8ff";
  } else if (duration < 30) {
    return "#b5dfff";
  } else if (duration < 60) {
    return "#7ec4f9";
  } else if (duration < 90) {
    return "#459ee1";
  } else if (duration < 120) {
    return "#258bd9";
  } else if (duration < 179) {
    return "#036ab7";
  } else if (duration < 240) {
    return "#004579";
  } else {
    return "#FFFFFF";
  }
};

export const getMonthLabels = (year: string, weekDayOfFirstJan: number) => {
  const monthsShort = [
    "Jan",
    "Feb",
    "Mär",
    "Apr",
    "Mai",
    "Jun",
    "Jul",
    "Aug",
    "Sep",
    "Okt",
    "Nov",
    "Dez",
  ];

  return monthsShort.map((month, m) => {
    const firstOfTheMonth = new Date(parseInt(year, 10), m, 1);
    const dayOfYear = Math.floor(
      (Date.UTC(firstOfTheMonth.getFullYear(), m, 1) -
        Date.UTC(parseInt(year, 10), 0, 1)) /
        (1000 * 60 * 60 * 24)
    );
    const column = Math.floor((dayOfYear + weekDayOfFirstJan) / 7) + 2;

    return (
      <div
        key={`month-${m}`}
        style={{
          gridColumn: column,
          gridRow: 1,
          fontSize: "10px",
          color: "#444",
        }}
      >
        {month}
      </div>
    );
  });
};

export const getWeekdayLabels = () =>
  ["So", "Mo", "Di", "Mi", "Do", "Fr", "Sa"].map((day, row) => (
    <div
      key={`weekday-${row}`}
      style={{
        gridColumn: 1,
        gridRow: row + 2,
        fontSize: "10px",
        textAlign: "right",
        paddingRight: "4px",
        color: "#444",
      }}
    >
      {day}
    </div>
  ));

export const formatDateString = (date: Date) => {
  return date.toISOString().slice(0, 10);
};
