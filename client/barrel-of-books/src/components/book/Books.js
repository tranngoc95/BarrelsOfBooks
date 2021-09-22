import { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import ErrorMessages from "../ErrorMessages";
import AuthContext from '../../AuthContext';
function Books() {
  const [books, setBooks] = useState([]);
  const [errorList, setErrorList] = useState([]);
  const auth = useContext(AuthContext);

  const getList = () => {
    return fetch("http://localhost:8080/api/book")
    .then((response) => response.json())
    .then((data) => setBooks(data))
    .catch((error) => console.log(error));
  }

  useEffect(getList,[]);


  const handleDelete = (bookId) => {
    const init = {
        method: "DELETE",
        'Authorization': `Bearer ${auth.user.token}`
      };
  
      fetch(`http://localhost:8080/api/book/${bookId}`, init)
        .then((response) => {
          if (response.status === 204 || response.status === 200) {
            return null;
          } else if (response.status === 404 || response.status === 400) {
            return response.json();
          }
          return Promise.reject("Something else went wrong");
        })
        .then((data) => {
          if (!data) {
            getList();
          } else {
            setErrorList(data);
          }
        })
        .catch((error) => console.log(error));
    };

  return (
    <div>
    <h2 className="mt-5">List of Books</h2>
    <ErrorMessages errorList={errorList} />
    <table className="table">
      <thead>
        <tr>
          <Link to="/books/add">Add New Book</Link>
        </tr>
        <tr>
          <th scope="col">ID</th>
          <th scope="col">Title</th>
          <th scope="col">Description</th>
          <th scope="col">Author</th>
          <th scope="col">Price</th>
          <th scope="col">Quantity</th>
          <th scope="col">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        {books.map((b) => (
          <tr key={b.bookId}>
            <td>{b.bookId}</td>
            <td><Link to={`/books/each/${b.bookId}`}>{b.title}</Link></td>
            <td>{b.description}</td>
            <td>{b.author}</td>
            <td>{b.price}</td>
            <td>{b.quantity}</td>
            <td>
              <Link to={`/books/edit/${b.bookId}`}>Edit</Link>
            </td>
            <td>
              <button type="button" onClick={() => handleDelete(b.bookId)}>
                Delete
              </button>
            </td>
          </tr>
        ))}
      </tbody>
    </table>
    <Link to="/">
      Go Back
    </Link>
  </div>
  )
}

export default Books;
