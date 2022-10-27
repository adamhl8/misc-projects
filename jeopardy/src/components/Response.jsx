import React, { useState } from "react";

const Response = (props) => {
  const [userResponse, setUserResponse] = useState("");

  const recordResponse = (event) => {
    setUserResponse(event.target.value);
  };

  const submitResponse = (event) => {
    if (event.key !== "Enter" || !props.currentQuestion.question) return;
    if (
      userResponse.toLowerCase() === props.currentQuestion.answer.toLowerCase()
    )
      props.setScore(props.score + props.currentQuestion.value);
    else props.setScore(props.score - props.currentQuestion.value);
    props.setCurrentQuestion({});
  };

  return (
    <div id="response" data-testid="response">
      <input
        type="text"
        placeholder="Answers go here!"
        onChange={recordResponse}
        onKeyDown={submitResponse}
      />
    </div>
  );
};

export default Response;
