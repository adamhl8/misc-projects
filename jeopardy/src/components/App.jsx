import React, { createContext, useEffect, useState } from "react";
import Gameboard from "./Gameboard.jsx";
import Scoreboard from "./Scoreboard.jsx";
import Response from "./Response.jsx";

export const GameboardContext = createContext({});

const App = () => {
  const [currentQuestion, setCurrentQuestion] = useState({});
  const [answeredQuestions, setAnsweredQuestions] = useState([]);
  const [score, setScore] = useState(0);
  const [gameData, setGameData] = useState(null);
  const GameboardContextValue = {
    currentQuestion,
    setCurrentQuestion,
    answeredQuestions,
    setAnsweredQuestions,
  };

  async function fetchCategory() {
    const id = Math.floor(Math.random() * 25000 + 1);
    const response = await fetch(`http://jservice.io/api/category?id=${id}`);
    const category = await response.json();
    return category;
  }

  async function buildGameData() {
    const gameData = [];

    for (let i = 0; i < 5; i++) {
      const category = await fetchCategory();

      category.clues = category.clues.slice(0, 5);
      for (const [index, clue] of category.clues.entries()) {
        clue.value = (index + 1) * 200;
      }

      gameData.push(category);
    }

    setGameData({ categories: gameData });
  }

  useEffect(() => {
    buildGameData();
  }, []);

  if (!gameData) return <div id={"app"}>Loading...</div>;
  else
    return (
      <div id={"app"}>
        <GameboardContext.Provider value={GameboardContextValue}>
          <Gameboard currentQuestion={currentQuestion} gameData={gameData} />
        </GameboardContext.Provider>
        <Scoreboard score={score} />
        <Response
          currentQuestion={currentQuestion}
          setCurrentQuestion={setCurrentQuestion}
          setScore={setScore}
          score={score}
        />
      </div>
    );
};

export default App;
