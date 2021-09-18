import { useContext, useEffect, useState } from 'react';

function Orders() {

    const [orders, setOrders] = useState([]);

    const URL = 'http://localhost:8080/api/transaction';

    useEffect(() => {

        const init = {
            headers: {
                'Authorization': 'Bearer'
            }
        }

        return fetch(URL + '/user/1', init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Cart fetch failed.")
                }
                return response.json();
            })
            .then(data => setOrders(data))
            .catch(error => console.log("Error", error));
    }, []);

    console.log(orders);

    return (
        <>
        <h2>My Orders</h2>
        {orders.map(order => (
            <div key = {order.transactionId}>
                <div>{order.status} {order.date}</div>
                <div>Total: ${order.total}</div>
            </div>
        ))}

        </>
    )
}

export default Orders;