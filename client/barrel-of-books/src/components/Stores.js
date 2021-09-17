import { useState, useEffect, useContext } from "react";
import {Link} from "react-router-dom";
function Stores() {
const [stores, setStores] = useState([]);


const getList = () => {
   return fetch("http://localhost:8080/api/store")
   .then((response) => response.json())
   .then((data) => setStores(data))
   .catch((error) => console.log(error));
}

//sets list state
useEffect(getList,[]);


    return (
        <div>
        <h2 className="mt-5">List of Stores</h2>
        
        <table className="table">
          <thead>
              <tr>
              <Link to="/api/stores/add">Add New Store</Link>
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
                <td><Link to={`/api/stores/edit/${s.storeId}`}>Edit</Link></td>
              </tr>
            ))}
          </tbody>
        </table>
        <Link className="btn btn-warning" to="/">Go Back</Link>
  
       </div>
      
    )
}

export default Stores;