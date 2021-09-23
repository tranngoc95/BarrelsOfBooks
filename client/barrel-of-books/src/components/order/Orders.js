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
    }, [auth.user]);


    return (
        <div className="home-page">
             <h2 className="sub-title">My Orders</h2>
        <div className="ui container white-bg">
           
            <div className="ui celled list">
                {orders.map(order => (
                    <div className="item" key={order.transactionId}>
                        <div>{order.status} {order.date}</div>
                        {order.books.map(item => (
                            <div key={item.cartItemId}>
                                <h3>{item.book.title}</h3>
                                <div>Quantity: {item.quantity}</div>
                                {order.status === "ORDERED" && order.books.length > 1 &&
                                    <Link to={`/orders/cancel/item/${item.cartItemId}`}>Cancel Item</Link>
                                }
                            </div>
                        ))}
                        <div>Total: ${order.total}</div>
                        {order.status === "ORDERED" &&
                            <Link className="ui button" to={`/orders/cancel/${order.transactionId}`}>Cancel Order</Link>
                        }
                    </div>

                ))}
            </div>

        </div>
        </div>
    )
}

export default Orders;