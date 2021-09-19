import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

function AllOrders() {

    const [orders, setOrders] = useState([]);
    const auth = useContext();

    const URL = 'http://localhost:8080/api/transaction';

    useEffect(() => {

        const init = {
            headers: {
                'Authorization': `Bearer ${auth.user.token}`
            }
        }

        fetch(URL, init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Orders fetch failed.")
                }
                return response.json();
            })
            .then(data => setOrders(data))
            .catch(error => console.log("Error", error));
    }, []);


    return (
        <>
            <h2>My Orders</h2>
            <table>
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Date</th>
                        <th>User Id</th>
                        <th>Status</th>
                        <th>&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <tr key={order.transactionId}>
                            <td>{order.transactionId}</td>
                            <td>{order.date}</td>
                            <td>{order.userId}</td>
                            <td>
                                <form>
                                    <select id="status" name="status" value={order.status} disabled>
                                        <option value="ORDERED">Ordered</option>
                                        <option value="SHIPPED">Shipped</option>
                                        <option value="DELIVERED">Delivered</option>
                                    </select>
                                    <button type="button">Update Status</button>
                                    <button type="submit" hidden>Submit</button>
                                </form>
                            </td>
                            <td>
                                <button>Detail</button>

                            </td>
                        </tr>
                    ))
                    }
                </tbody>
            </table>
        </>
    )
}

export default AllOrders;