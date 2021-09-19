import { useState } from "react";
import {useHistory} from "react-router-dom";
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
              "Content-Type": "application/json"
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
            history.push("/");
          } else {
            setErrorList(data);
          }
      })
      .catch((error) => console.log(error));

  }

  return (
    <div>
      <h1>add new store</h1>
 {errorList.length > 0 ? (
    <div>
        {errorList.map((error) => (
        <li key={error}>{error}</li>
    ))}
  </div>
) : null}
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
    </div>
  );
}

export default AddStore;
