import React from "react";
import Categories from "./Categories.jsx";

const Gameboard = (props) => {
  return (
    <div
      data-testid="gameboard"
      id={props.currentQuestion.question ? "question" : "gameboard"}
    >
      {props.currentQuestion.question ? (
        props.currentQuestion.question
      ) : (
        <Categories data={props.gameData} />
      )}
    </div>
  );
};

export default Gameboard;
