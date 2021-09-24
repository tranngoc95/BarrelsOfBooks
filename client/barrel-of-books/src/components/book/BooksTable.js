import { useState } from "react";
import { Link } from "react-router-dom";
import ErrorMessages from "../ErrorMessages";

function BooksTable({ books, auth, getList, linkPath }) {
    const [errorList, setErrorList] = useState([]);

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
        <div>
            <ErrorMessages errorList={errorList} />
            <table className="ui fixed selectable table table-margin">
                <thead>
                    <tr>
                        <th scope="col">Title</th>

                        <th scope="col">Author</th>
                        <th scope="col">Price</th>
                        <th scope="col">Quantity</th>
                        {auth.user && auth.user.hasRole("MANAGER") &&
                                <th scope="col">&nbsp;</th>
                            }
                    </tr>
                </thead>

                <tbody>
                    {books.map((b) => (
                        <tr key={b.bookId}>
                            <td data-label="Title"><Link to={`/books/each/${b.bookId}`}>{b.title}</Link></td>
                            <td data-label="Author">{b.author}</td>
                            <td data-label="Price">{b.price}</td>
                            <td data-label="Quantity">{b.quantity}</td>
                            {auth.user && auth.user.hasRole("MANAGER") &&
                                <>
                                    <td>
                                        <Link  className="ui green left attached button" to={`/books/edit/${b.bookId}`}>Update</Link>
                                        <button className="ui red right attached button" type="button" onClick={() => handleDelete(b.bookId)}>
                                        Delete
                                        </button>
                                    </td>
                                </>
                                }
                        </tr>
                    ))}
                </tbody>
            </table>
  
        </div>
    )
}

export default BooksTable;