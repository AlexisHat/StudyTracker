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
  color,
  dayIndex,
}) => {
  const handleClick = () => {
    console.log(dayIndex);
  };
  return (
    <div
      key={dayIndex}
      onClick={handleClick}
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
