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
        headers: {
        'Authorization': `Bearer ${auth.user.token}`
        }
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
  <div className = "home-page">
    <h2 className="sub-title">Books</h2>
    <ErrorMessages errorList={errorList} />
    {auth.user && auth.user.hasRole("MANAGER") &&  
    <Link className="ui primary button add-button" to="/books/add">Add New Book</Link>}
    <table className="ui fixed table table-margin">
      <thead>
        <tr>
          <th scope="col">Title</th>
          <th scope="col">Description</th>
          <th scope="col">Author</th>
          <th scope="col">Price</th>
          <th scope="col">Quantity</th>
          <th scope="col">&nbsp;</th>
          <th scope="col">&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        {books.map((b) => (
          <tr key={b.bookId}>
            <td data-label="Title"><Link to={`books/each/${b.bookId}`}>{b.title}</Link></td>
            <td data-label="Description">{b.description}</td>
            <td data-label="Author">{b.author}</td>
            <td data-label="Price">{b.price}</td>
            <td data-label="Quantity">{b.quantity}</td>
            <td>
            {auth.user && auth.user.hasRole("MANAGER") &&  
              <Link className="ui primary button" to={`/books/edit/${b.bookId}`}>Edit</Link>}
            </td>
            <td>
            {auth.user && auth.user.hasRole("MANAGER") &&  
              <button className="ui primary button" type="button" onClick={() => handleDelete(b.bookId)}>
                Delete
              </button>}
            </td>
          </tr>
        ))}
      </tbody>
    </table>
    <Link className="ui secondary button" to="/">
      Go Back
    </Link>
  </div>
  )
}

export default Books;
