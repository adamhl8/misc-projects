import React from "react";
import Clue from "./Clue.jsx";

const Category = (props) => {
  return (
    <div className="category" data-testid="category">
      <div className="categoryTitle">{props.category.title.toUpperCase()} </div>
      {props.category.clues.map((clue) => (
        <Clue key={clue.id} clue={clue} />
      ))}
    </div>
  );
};

export default Category;
