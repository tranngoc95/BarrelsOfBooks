import { useState, useRef } from "react";

function EachOrder({ order, getList, auth, setErrorList }) {

    const [update, setUpdate] = useState(false);
    const [status, setStatus] = useState(order.status);

    const URL = 'http://localhost:8080/api/transaction';

    const handleUpdate = () => {
        setUpdate(true);
    }

    const updateStatus = () => {
        setUpdate(false);

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
                    } else if (response.status === 403) {
                        return ['You are not authorized to make changes to this record.'];
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
                    }
                })
                .catch(error => console.log('Error:', error));
        }
    }

    console.log(order);

    return (
        <>
            <tr>
                <td>{order.transactionId}</td>
                <td>{order.date}</td>
                <td>{order.userId}</td>
                <td>
                    <ul>{order.books.map(each => (
                        <li key={each.cartItemId}><div>{each.book.title} - {each.book.author}</div>
                            <div> - qty {each.quantity}</div></li>
                    ))}
                    </ul>
                </td>
                <td>
                    <form>
                        <select id="status" name="status" value={status} onChange={(event) => setStatus(event.target.value)} disabled={!update}>
                            <option value="ORDERED">Ordered</option>
                            <option value="SHIPPED">Shipped</option>
                            <option value="DELIVERED">Delivered</option>
                        </select>
                        {!update && <button className="mini ui teal button" type="button" onClick={handleUpdate}>Update Status</button>}
                        {update && <button className="mini ui green button" type="submit" onClick={updateStatus}>Save</button>}
                    </form>
                </td>
            </tr>

        </>
    )
}

export default EachOrder;