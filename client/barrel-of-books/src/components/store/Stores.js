import { useState, useEffect, useContext } from "react";
import { Link } from "react-router-dom";
import ErrorMessages from "../ErrorMessages";
import AuthContext from '../../AuthContext';
function Stores() {
  const [stores, setStores] = useState([]);
  const [errorList, setErrorList] = useState([]);
  const auth = useContext(AuthContext);


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
      headers: {
      'Authorization': `Bearer ${auth.user.token}`
      }
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
    <div className="home-page">
      <h2 className="sub-title">Stores</h2>
      <ErrorMessages errorList={errorList} />
      {auth.user && auth.user.hasRole("ADMIN") &&
      <Link className="ui primary button add-button" to="/stores/add">Add New Store</Link>}
      <table className="ui celled table table-margin">
        <thead>
          <tr>
            <th scope="col">Address</th>
            <th scope="col">City</th>
            <th scope="col">State</th>
            <th scope="col">Postal Code</th>
            <th scope="col">Phone Number</th>
            <th scope="col">&nbsp;</th>
            <th scope="col">&nbsp;</th>
          </tr>
        </thead>
        <tbody>
          {stores.map((s) => (
            <tr key={s.storeId}>
              <td data-label="Address">{s.address}</td>
              <td data-label="City">{s.city}</td>
              <td data-label="State">{s.state}</td>
              <td data-label="Postal Code">{s.postalCode}</td>
              <td data-label="Phone Number">{s.phone}</td>
              <td>
              {auth.user && auth.user.hasRole("ADMIN") &&
                <Link className="ui primary button" to={`/stores/edit/${s.storeId}`}>Edit</Link>}
              </td>
              <td>
              {auth.user && auth.user.hasRole("ADMIN") &&
                <button className="ui primary button" type="button" onClick={() => handleDelete(s.storeId)}>
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
  );
}

export default Stores;
