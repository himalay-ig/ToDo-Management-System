import axios from "axios";

const API_URL = "http://localhost:8091/api/todos";

export const getTodos = () => axios.get(API_URL);
export const addTodo = (todo) => axios.post(API_URL, todo);
export const deleteTodo = (id) => axios.delete(`${API_URL}/${id}`);
export const toggleTodo = (id) => axios.patch(`${API_URL}/${id}/toggle`);
export const toggleImportant = (id) =>
  axios.patch(`${API_URL}/${id}/important`);