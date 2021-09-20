import { useState, useRef } from "react";
import ErrorMessages from "../ErrorMessages";

function EachOrder({ order, getList, auth }) {

    const [save, setSave] = useState(false);
    const [update, setUpdate] = useState(true);
    const [status, setStatus] = useState(order.status);
    const [errorList, setErrorList] = useState([]);

    const URL = 'http://localhost:8080/api/transaction';

    const selectRef = useRef();

    const handleUpdate = () => {
        selectRef.current.disabled = false;
        setUpdate(false);
        setSave(true);
    }

    const updateStatus = () => {
        selectRef.current.disabled = true;
        setSave(false);
        setUpdate(true);

        if (status !== order.status) {
            const changedOrder = { ...order };
            changedOrder.status = status;

            const init = {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.user.token}`
                },
                body: JSON.stringify(changedOrder)
            };

            fetch(URL + `/${order.transactionId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        return null;
                    } else if (response.status === 404) {
                        return [`Order with id ${order.transactionId} does not exist.`];
                    } else if (response.status === 400) {
                        return response.json();
                    }
                    return Promise.reject("Something went wrong, sorry :(");
                })
                .then(data => {
                    if (!data) {
                        getList();
                    } else {
                        setErrorList(data);
                        console.log(data);
                    }
                })
                .catch(error => console.log('Error:', error));
        }
    }

    return (
        <>
            <tr>
                <td>{order.transactionId}</td>
                <td>{order.date}</td>
                <td>{order.userId}</td>
                <td>
                    <ul>{order.books.map(each => (
                        <li>{each.book.title} - {each.book.author}</li>
                    ))}

                    </ul>
                </td>
                <td>
                    <form>
                        <select id="status" name="status" value={status} ref={selectRef} onChange={(event) => setStatus(event.target.value)} disabled>
                            <option value="ORDERED">Ordered</option>
                            <option value="SHIPPED">Shipped</option>
                            <option value="DELIVERED">Delivered</option>
                        </select>
                        {update && <button type="button" onClick={handleUpdate}>Update Status</button>}
                        {save && <button type="submit" onClick={updateStatus}>Save</button>}
                    </form>
                </td>
            </tr>

        </>
    )
}

export default EachOrder;