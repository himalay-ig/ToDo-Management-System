import { render, screen } from "@testing-library/react";
import { TodoItem } from "./TodoItem";

test("renders todo title", () => {
  const todo = {
    id: 1,
    title: "Test Todo",
    completed: false,
    important: false,
    createdAt: new Date().toISOString(),
  };

  render(<TodoItem todo={todo} refreshTodos={() => {}} />);

  expect(screen.getByText("Test Todo")).toBeInTheDocument();
});