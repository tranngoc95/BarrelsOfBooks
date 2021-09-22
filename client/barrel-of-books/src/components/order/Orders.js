import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import AuthContext from '../../AuthContext';

function Orders() {

    const [orders, setOrders] = useState([]);

    const URL = 'http://localhost:8080/api/transaction';
    const auth = useContext(AuthContext);

    useEffect(() => {

        const init = {
            headers: {
                'Authorization': `Bearer ${auth.user.token}`
            }
        }

        fetch(URL + `/user/${auth.user.id}`, init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Cart fetch failed.")
                }
                return response.json();
            })
            .then(data => setOrders(data))
            .catch(error => console.log("Error", error));
    }, []);
    

    return (
        <>
            <h2>My Orders</h2>
            {orders.map(order => (
                <div key={order.transactionId}>
                    <div>{order.status} {order.date}</div>
                    {order.books.map(item => (
                        <div key={item.cartItemId}>
                            <h6>{item.book.title}</h6>
                            {order.status === "ORDERED" && order.books.length > 1 &&
                                <Link to={`/orders/cancel/item/${item.cartItemId}`}>Cancel Item</Link>
                            }
                        </div>
                    ))}
                    <div>Total: ${order.total}</div>
                    {order.status === "ORDERED" &&
                        <Link to={`/orders/cancel/${order.transactionId}`}>Cancel Order</Link>
                    }
                </div>
            ))}

        </>
    )
}

export default Orders;