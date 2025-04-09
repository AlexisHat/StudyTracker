import HeatmapCell from "../components/heatmap/HeatmapCell";

export const generateHeatmapCells = (
  weeks: number,
  days: number,
  total: number
) => {
  const cells = [];

  for (let col = 0; col < weeks; col++) {
    for (let row = 0; row < days; row++) {
      const dayIndex = col * days + row;
      if (dayIndex >= total) break;
      cells.push(
        <HeatmapCell key={dayIndex} col={col} row={row} dayIndex={dayIndex} />
      );
    }
  }

  return cells;
};
