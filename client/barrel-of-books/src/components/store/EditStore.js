import { useEffect, useState,useContext } from "react";
import {useParams, useHistory, Link} from "react-router-dom";
import ErrorMessages from "../ErrorMessages";
import AuthContext from '../../AuthContext';
function EditStore() {
    const[store,setStore] = useState({ 
    address: "",
    city: "",
    state: "",
    postalCode: "",
    phone: "",});
    const [errorList, setErrorList] = useState([]);
    const {id} = useParams();
    const history = useHistory();
    const auth = useContext(AuthContext);

    const getStore = () => {
        fetch(`http://localhost:8080/api/store/${id}`)
        .then(response => response.json())
        .then(data => setStore(data))
        .catch(error => console.log(error));
    }

    useEffect(getStore,[id]);

    const handleChange = (event) => {
        const newStore = {...store};
        newStore[event.target.name] = event.target.value;
        setStore(newStore);
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        const newStore = {...store};
        newStore['storeId'] = id;
  
        const init = {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
                'Authorization': `Bearer ${auth.user.token}`
            },
            body: JSON.stringify(newStore),
        };
  
        fetch(`http://localhost:8080/api/store/${id}`, init)
        .then((response) => {
            if (
                response.status === 201 ||
                response.status === 204 
            ) {
                return null;
            } else if(response.status === 400 ) {
                return response.json();
            }else if(response.status === 409) {
                return "Store ID does not match request ID";
            }
          return Promise.reject("Something else went wrong");
        })
        .then((data) => {
            if(!data) {
              history.push("/stores");
            } else {
              setErrorList(data);
            }
        })
        .catch((error) => console.log(error));
  
    }


return (
    <div>
    <h1>edit store</h1>
    <ErrorMessages errorList={errorList} />
    <form onSubmit = {handleSubmit}>
      <div>
        <label htmlFor="address">Address</label>
        <input type="text" id="address" name="address" value={store.address} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="city">City</label>
        <input type="text" id="city" name="city" value={store.city} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="state">State</label>
        <input type="text" id="state" name="state" value={store.state} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="post-code">Post Code</label>
        <input type="text" id="post-code" name="postalCode" value={store.postalCode} onChange={handleChange}/>
      </div>
      <div>
        <label htmlFor="phone">Phone</label>
        <input type="text" id="phone" name="phone" value={store.phone} onChange={handleChange}/>
      </div>
      <div>
          <button type="submit">Submit</button>
      </div>
    </form>
    <Link className="btn btn-warning" to="/stores">Go Back</Link>
  </div>
)
}

export default EditStore;