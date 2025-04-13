import React from "react";

type HeatmapCellProps = {
  col: number;
  row: number;
  color?: string;
  dayIndex: number;
  onHoverCell?: (dayIndex: number, event: React.MouseEvent) => void;
  onLeaveCell?: () => void;
};

const HeatmapCell: React.FC<HeatmapCellProps> = ({
  col,
  row,
  color,
  dayIndex,
  onHoverCell,
  onLeaveCell,
}) => {
  const handleClick = () => {
    console.log(dayIndex);
  };

  const handleHover = (event: React.MouseEvent) => {
    if (onHoverCell) {
      onHoverCell(dayIndex, event);
    }
  };

  const handleLeave = () => {
    if (onLeaveCell) {
      onLeaveCell();
    }
  };

  return (
    <div
      key={dayIndex}
      onClick={handleClick}
      onMouseEnter={handleHover}
      onMouseLeave={handleLeave}
      style={{
        width: "15px",
        height: "15px",
        backgroundColor: color,
        gridColumn: col + 1,
        gridRow: row + 1,
        borderRadius: "3px",
      }}
    />
  );
};

export default HeatmapCell;
