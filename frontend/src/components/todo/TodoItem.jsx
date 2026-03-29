import { deleteTodo, toggleTodo, toggleImportant } from "../../services/todoService";
import Card from "react-bootstrap/Card";
import Button from "react-bootstrap/Button";
import Badge from "react-bootstrap/Badge";

export const TodoItem = ({ todo, refreshTodos }) => {
  const createdValue = todo.createdAt ?? todo.created_at;

  const formattedTime = createdValue
    ? new Date(createdValue).toLocaleString()
    : "—";

  return (
    <Card className="mb-3 shadow-sm">
      <Card.Body className="d-flex justify-content-between align-items-start">
        {/* Left side */}
        <div className="d-flex gap-3">
          <input
            type="checkbox"
            className="form-check-input mt-1"
            checked={todo.completed}
            onChange={async () => {
              await toggleTodo(todo.id);
              refreshTodos();
            }}
          />

          <div>
            <Card.Title className="mb-1">
              <span
                style={{
                  textDecoration: todo.completed ? "line-through" : "none",
                }}
              >
                {todo.title}
              </span>

              {todo.completed ? (
                <Badge bg="success" className="ms-2">
                  Completed
                </Badge>
              ) : (
                <Badge bg="warning" text="dark" className="ms-2">
                  In Progress
                </Badge>
              )}

              {/* ⭐ Important badge */}
              {todo.important && (
                <Badge bg="danger" className="ms-2">
                  Important
                </Badge>
              )}
            </Card.Title>

            <Card.Subtitle className="text-muted">
              Created: {formattedTime}
            </Card.Subtitle>
          </div>
        </div>

        {/* Right side buttons */}
        <div className="d-flex gap-2">
          {/* ⭐ Star button */}
          <Button
            variant={todo.important ? "warning" : "outline-secondary"}
            size="sm"
            title="Mark as Important"
            onClick={async () => {
              await toggleImportant(todo.id);
              refreshTodos();
            }}
          >
            ⭐
          </Button>

          {/* Delete with confirmation */}
          <Button
            variant="outline-danger"
            size="sm"
            onClick={async () => {
              const ok = window.confirm(
                "Are you sure you want to delete this todo?"
              );
              if (!ok) return;

              await deleteTodo(todo.id);
              refreshTodos();
            }}
          >
            Delete
          </Button>
        </div>
      </Card.Body>
    </Card>
  );
};