import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";

export default function TodoNavbar({ active, onChange }) {
  const items = [
    { key: "dashboard", label: "Dashboard" },
    { key: "important", label: "Important" },
    { key: "completed", label: "Completed" },
    { key: "inprogress", label: "In Progress" },
  ];

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="mb-3">
      <Container>
        <Navbar.Brand
          style={{ cursor: "pointer" }}
          onClick={() => onChange("dashboard")}
        >
          TodoApp
        </Navbar.Brand>

        <Navbar.Toggle aria-controls="todo-navbar" />
        <Navbar.Collapse id="todo-navbar">
          <Nav className="me-auto d-flex align-items-center">
            {items.map((it) => (
              <button
                key={it.key}
                className={`btn btn-sm me-2 ${
                  active === it.key ? "btn-warning" : "btn-outline-light"
                }`}
                onClick={() => onChange(it.key)}
                type="button"
              >
                {it.label}
              </button>
            ))}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}