import { useEffect, useMemo, useState } from "react";
import { getTodos } from "../../services/todoService.js";
import { TodoItem } from "./TodoItem.jsx";
import { TodoForm } from "./TodoForm.jsx";
import TodoNavbar from "./TodoNavbar.jsx";

export const TodoApp = () => {
  const [todos, setTodos] = useState([]);
  const [activeTab, setActiveTab] = useState("dashboard");

  useEffect(() => {
    loadTodos();
  }, []);

  const loadTodos = async () => {
    const response = await getTodos();
    setTodos(response.data);
  };

  // ---- Helpers (robust: works even if backend fields differ) ----
  const isCompleted = (t) => {
    // common possibilities: completed (boolean), status (string), isCompleted (boolean)
    if (typeof t?.completed === "boolean") return t.completed;
    if (typeof t?.isCompleted === "boolean") return t.isCompleted;
    if (typeof t?.status === "string") return t.status.toLowerCase() === "completed";
    return false;
  };

  const isImportant = (t) => {
    // common possibilities: important (boolean), priority ("HIGH"), isImportant (boolean)
    if (typeof t?.important === "boolean") return t.important;
    if (typeof t?.isImportant === "boolean") return t.isImportant;
    if (typeof t?.priority === "string") return t.priority.toLowerCase() === "high";
    return false;
  };

  const filteredTodos = useMemo(() => {
    if (activeTab === "dashboard") return todos;
    if (activeTab === "important") return todos.filter(isImportant);
    if (activeTab === "completed") return todos.filter(isCompleted);
    if (activeTab === "inprogress") return todos.filter((t) => !isCompleted(t));
    return todos;
  }, [activeTab, todos]);

  // optional: counts on header
  const counts = useMemo(() => {
    const completed = todos.filter(isCompleted).length;
    const inprogress = todos.length - completed;
    const important = todos.filter(isImportant).length;
    return { total: todos.length, completed, inprogress, important };
  }, [todos]);

  return (
    <>
      <TodoNavbar active={activeTab} onChange={setActiveTab} />

      <div className="container">
        <div className="d-flex align-items-center justify-content-between mb-2">
          <h2 className="m-0">Todo Application</h2>

          <small className="text-muted">
            Total: {counts.total} | Important: {counts.important} | Completed: {counts.completed} | In Progress: {counts.inprogress}
          </small>
        </div>

        <TodoForm refreshTodos={loadTodos} />

        {filteredTodos.map((todo) => (
          <TodoItem key={todo.id} todo={todo} refreshTodos={loadTodos} />
        ))}

        {filteredTodos.length === 0 && (
          <div className="alert alert-info mt-3">
            No todos found for <b>{activeTab}</b>.
          </div>
        )}
      </div>
    </>
  );
};