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
            <div className="orders-container white-bg">
                <div>
                    {orders.map(order => (
                        <div className="ui segments" key={order.transactionId}>
                            <div className="ui secondary segment">
                                <div className="ui six column grid">
                                    <div className="column">
                                        <div >Order Placed</div>
                                        <div>{dateFormat(order.date)}</div>
                                    </div>
                                    <div className="column">
                                        <div>Total</div>
                                        <div>${order.total}</div>
                                    </div>
                                    <div className="column">
                                        <div>Status</div>
                                        <div>{order.status}</div>
                                    </div>
                                    <div className="right floated column">
                                        <div>Order #{order.transactionId}</div>
                                        {order.status === "ORDERED" &&
                                            <Link className="ui small button" to={`/orders/cancel/${order.transactionId}`}>Cancel Order</Link>
                                        }
                                    </div>
                                </div>
                            </div>
                            {order.books.map(item => (
                                <div className="ui segment" key={item.cartItemId}>
                                    <div className="ui six column grid">
                                        <div className="column" />
                                        <div className="column"><img className="ui tiny image" src={`/${item.book.bookId}.jpg`} alt="" /></div>
                                        <div className="six wide column">
                                            <h5>{item.book.title}</h5>
                                            <div>Quantity: {item.quantity}</div>
                                            {order.status === "ORDERED" && order.books.length > 1 &&
                                                <Link to={`/orders/cancel/item/${item.cartItemId}`}>Cancel Item</Link>
                                            }
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>

                    ))}
                </div>

            </div>
        </div>
    )
}

function dateFormat(date) {
    let parts = date.split('-');
    return new Date(parts[0], parts[1] - 1, parts[2]).toDateString();
}

export default Orders;