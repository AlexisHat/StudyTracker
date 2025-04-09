import HeatmapCell from "../components/heatmap/HeatmapCell";

export const generateHeatmapCells = (
  weeks: number,
  days: number,
  totalDays: number,
  weekDayOfFistJan: number
) => {
  const daysToSkipBeginning = weekDayOfFistJan + 1;
  const cells = [];

  for (let col = 0; col < weeks; col++) {
    for (let row = 0; row < days; row++) {
      const dayIndex = col * days + row;
      if (dayIndex >= totalDays + daysToSkipBeginning) break;
      if (dayIndex < daysToSkipBeginning) {
        cells.push(
          <HeatmapCell
            key={dayIndex}
            col={col}
            row={row}
            color="transparent"
            dayIndex={dayIndex}
          />
        );
      } else {
        cells.push(
          <HeatmapCell key={dayIndex} col={col} row={row} dayIndex={dayIndex} />
        );
      }
    }
  }

  return cells;
};
