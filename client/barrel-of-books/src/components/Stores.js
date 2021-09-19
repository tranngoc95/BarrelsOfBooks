import { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
function Stores() {
  const [stores, setStores] = useState([]);
  const [errorList, setErrorList] = useState([]);

  const getList = () => {
    return fetch("http://localhost:8080/api/store")
      .then((response) => response.json())
      .then((data) => setStores(data))
      .catch((error) => console.log(error));
  };

  //sets list state
  useEffect(getList, []);

  const handleDelete = (storeId) => {
    const init = {
      method: "DELETE",
    };

    fetch(`http://localhost:8080/api/store/${storeId}`, init)
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
      <h2 className="mt-5">List of Stores</h2>
      {errorList.length > 0 ? (
        <div>
          {errorList.map((error) => (
            <li key={error}>{error}</li>
          ))}
        </div>
      ) : null}
      <table className="table">
        <thead>
          <tr>
            <Link to="/stores/add">Add New Store</Link>
          </tr>
          <tr>
            <th scope="col">ID</th>
            <th scope="col">Address</th>
            <th scope="col">City</th>
            <th scope="col">State</th>
            <th scope="col">Postal Code</th>
            <th scope="col">Phone Number</th>
            <th scope="col">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          {stores.map((s) => (
            <tr key={s.storeId}>
              <td>{s.storeId}</td>
              <td>{s.address}</td>
              <td>{s.city}</td>
              <td>{s.state}</td>
              <td>{s.postalCode}</td>
              <td>{s.phone}</td>
              <td>
                <Link to={`/stores/edit/${s.storeId}`}>Edit</Link>
              </td>
              <td>
                <button type="button" onClick={() => handleDelete(s.storeId)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <Link className="btn btn-warning" to="/">
        Go Back
      </Link>
    </div>
  );
}

export default Stores;
