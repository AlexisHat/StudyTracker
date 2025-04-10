import React from "react";

type HeatmapCellProps = {
  col: number;
  row: number;
  color?: string;
  dayIndex: number;
};

const HeatmapCell: React.FC<HeatmapCellProps> = ({
  col,
  row,
  color = "blue",
  dayIndex,
}) => {
  return (
    <div
      key={dayIndex}
      style={{
        width: "15px",
        height: "15px",
        backgroundColor: color,
        gridColumn: col + 1,
        gridRow: row + 1,
      }}
    />
  );
};

export default HeatmapCell;
