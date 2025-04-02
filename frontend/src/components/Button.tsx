import { useState } from "react";

function Button() {
  const btns = [
    "btn btn-primary",
    "btn btn-secondary",
    "btn btn-success",
    "btn btn-info",
    "btn btn-dark",
  ];

  const [selectedIndex, setSelectedIndex] = useState(0);

  const handleClick = () => {
    setSelectedIndex((prevIndex) => (prevIndex + 1) % btns.length);
  };

  return (
    <button className={btns[selectedIndex]} onClick={handleClick}>
      Klick mich!
    </button>
  );
}

export default Button;
