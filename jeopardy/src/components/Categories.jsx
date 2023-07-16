import React from "react";
import Category from "./Category.jsx";

const Categories = (props) => {
  return (
    <div id="categories" data-testid="categoryList">
      {props.data.categories.map((category) => {
        return <Category key={category.id} category={category} />;
      })}
    </div>
  );
};

export default Categories;
