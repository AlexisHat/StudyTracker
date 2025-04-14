import React from "react";

type HeatmapCellProps = {
  col: number;
  row: number;
  color?: string;
  dayIndex: number;
  onHoverCell?: (dayIndex: number, event: React.MouseEvent) => void;
  onLeaveCell?: () => void;
  onClickCell?: (dayIndex: number) => void;
};

const HeatmapCell: React.FC<HeatmapCellProps> = ({
  col,
  row,
  color,
  dayIndex,
  onHoverCell,
  onLeaveCell,
  onClickCell,
}: HeatmapCellProps) => {
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
      onClick={() => dayIndex !== -1 && onClickCell?.(dayIndex)}
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
