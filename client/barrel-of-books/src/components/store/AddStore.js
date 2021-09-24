import { useState, useContext } from "react";
import {useHistory, Link} from "react-router-dom";
import ErrorMessages from "../ErrorMessages";
import AuthContext from '../../AuthContext';
function AddStore() {
  const [store, setStore] = useState({
    address: "",
    city: "",
    state: "",
    postalCode: "",
    phone: "",
});
  const[errorList, setErrorList] = useState([]);
  const history = useHistory();
  const auth = useContext(AuthContext);

  const handleChange = (event) => {
      const newStore = {...store};
      newStore[event.target.name] = event.target.value;
      setStore(newStore);
  }

  const handleSubmit = (event) => {
      event.preventDefault();
      const newStore = {...store};

      const init = {
          method: "POST",
          headers: {
              "Content-Type": "application/json",
              'Authorization': `Bearer ${auth.user.token}`
          },
          body: JSON.stringify(newStore),
      };

      fetch("http://localhost:8080/api/store", init)
      .then((response) => {
          if (
              response.status === 201 ||
              response.status === 204 ||
              response.status === 400
          ) {
              return response.json();
          }
        return Promise.reject("Something else went wrong");
      })
      .then((data) => {
          if(data.storeId) {
            history.push("/stores");
          } else {
            setErrorList(data);
          }
      })
      .catch((error) => console.log(error));

  }

  return (

    <div className="home-page">
      <h1 className="sub-title">Add New Store</h1>
      <ErrorMessages errorList={errorList} />
      <form className="ui small form" onSubmit = {handleSubmit}>
        <div className="ui stacked segment">
        <div className="field">
          <label htmlFor="address">Address</label>
          <input type="text" id="address" name="address" value={store.address} onChange={handleChange}/>
        </div>
        <div className="field">
          <label htmlFor="city">City</label>
          <input type="text" id="city" name="city" value={store.city} onChange={handleChange}/>
        </div>
        <div className="field">
          <label htmlFor="state">State</label>
          <input type="text" id="state" name="state" value={store.state} onChange={handleChange}/>
        </div>
        <div className="field">
          <label htmlFor="post-code">Post Code</label>
          <input type="text" id="post-code" name="postalCode" value={store.postalCode} onChange={handleChange}/>
        </div>
        <div className="field">
          <label htmlFor="phone">Phone</label>
          <input type="text" id="phone" name="phone" value={store.phone} onChange={handleChange}/>
        </div>
        <div className="ui buttons">
						<button className="ui positive button active" type="submit">Submit</button>
						<div className="or"></div>
						<Link className="ui button" type="button" to='/stores'>Cancel</Link>
					</div>
        </div>
      </form>  
   
    </div>
  );
}

export default AddStore;
