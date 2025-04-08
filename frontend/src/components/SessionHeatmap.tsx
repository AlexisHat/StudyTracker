const SessionHeatmap = () => {
  const weeks = 52;
  const days = 7;
  const total = 356;

  const cells = [];

  for (let col = 0; col < weeks; col++) {
    for (let row = 0; row < days; row++) {
      const dayIndex = col * days + row;
      if (dayIndex >= total) break;
      cells.push(
        <div
          key={dayIndex}
          style={{
            width: "15px",
            height: "15px",
            backgroundColor: "blue",
            gridColumn: col + 1,
            gridRow: row + 1,
          }}
        />
      );
    }
  }

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
