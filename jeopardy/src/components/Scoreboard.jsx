import React from "react";

const Scoreboard = (props) => {
  return (
    <div id="scoreboard" data-testid="scoreboard">
      ${props.score}
    </div>
  );
};

export default Scoreboard;
