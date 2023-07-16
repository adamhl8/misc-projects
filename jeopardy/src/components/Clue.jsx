import React, { useState } from "react";
import { GameboardContext } from "./App.jsx";

const Clue = (props) => {
  function onClueClick(GameboardContextValue) {
    if (GameboardContextValue.answeredQuestions.includes(props.clue)) return;
    const newAnsweredQuestions = [...GameboardContextValue.answeredQuestions];
    newAnsweredQuestions.push(props.clue);
    GameboardContextValue.setAnsweredQuestions(newAnsweredQuestions);
    GameboardContextValue.setCurrentQuestion(props.clue);
  }

  return (
    <GameboardContext.Consumer>
      {(GameboardContextValue) => (
        <button
          onClick={() => onClueClick(GameboardContextValue)}
          className={"clueValue"}
          data-testid="value"
        >
          {GameboardContextValue.answeredQuestions.includes(props.clue)
            ? ""
            : `$${props.clue.value}`}
        </button>
      )}
    </GameboardContext.Consumer>
  );
};

export default Clue;
