import { useState } from "react";
import { addTodo } from "../../services/todoService";

export const TodoForm = ({ refreshTodos }) => {
  const [title, setTitle] = useState("");

  const submitHandler = async (e) => {
    e.preventDefault();
    await addTodo({ title });
    setTitle("");
    refreshTodos();
  };

  return (
    <form onSubmit={submitHandler}>
      <input
        type="text"
        placeholder="Enter todo"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
      />
      <button>Add</button>
    </form>
  );
};