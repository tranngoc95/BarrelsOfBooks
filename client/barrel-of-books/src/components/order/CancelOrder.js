import { useState, useEffect, useContext } from "react";
import { Link, useParams, useHistory } from "react-router-dom";

import ErrorMessages from "../ErrorMessages";
import AuthContext from "../../AuthContext";

function CancelOrder() {
  const [errorList, setErrorList] = useState([]);
  const [order, setOrder] = useState(null);

  const URL = "http://localhost:8080/api/transaction";
  const auth = useContext(AuthContext);
  const history = useHistory();

  const { id } = useParams();

  useEffect(() => {
    const init = {
      headers: {
        Authorization: `Bearer ${auth.user.token}`,
      },
    };

    fetch(URL + `/${id}`, init)
      .then((response) => {
        if (response.status === 200) {
          return response.json();
        } else if (response.status === 404) {
          return [`Order with id ${id} does not exist.`];
        } else if (response.status === 403) {
          return ["You are not authorized to access this record."];
        }
        return Promise.reject("Something went wrong, sorry :(");
      })
      .then((data) => {
        if (data.transactionId) {
          setOrder(data);
        } else {
          setErrorList(data);
        }
      })
      .catch((error) => console.log("Error", error));
  }, [id, auth.user.token]);

  const cancelOrder = () => {
    const init = {
      method: "Delete",
      headers: {
        Authorization: `Bearer ${auth.user.token}`,
      },
    };

    fetch(URL + `/${id}`, init)
      .then((response) => {
        if (response.status === 204) {
          return null;
        } else if (response.status === 404) {
          return [`Order with id ${id} does not exist.`];
        } else if (response.status === 403) {
          return ["You are not authorized to make changes to this record."];
        }
        return Promise.reject("Something went wrong, sorry :(");
      })
      .then((data) => {
        if (!data) {
          history.push("/orders");
        } else {
          setErrorList(data);
        }
      })
      .catch((error) => console.log("Error", error));
  };

  return (
    <div className="home-page">
      <h3 className="sub-title">Cancel Order</h3>
      <div className="ui container seashell-bg">
        <ErrorMessages errorList={errorList} />
        {order && (
          <>
            <div>
              <div>
                {order.status} {order.date}
              </div>
              {order.books.map((item) => (
                <div>
                  <h6>{item.book.title}</h6>
                  <div>Quantity: {item.quantity}</div>
                </div>
              ))}
              <div>Total: ${order.total}</div>
            </div>

            <hr />
            <div>Are you sure you want to cancel this item?</div>
            <div>
              <button className="ui primary button"type="button" onClick={cancelOrder}>
                Yes
              </button>
              <Link className="ui secondary button" to="/orders">No</Link>
            </div>
          </>
        )}
      </div>
    </div>
  );
}

export default CancelOrder;
