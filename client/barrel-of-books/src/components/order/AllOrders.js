import { useContext, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';

import AuthContext from '../../AuthContext';
import ErrorMessages from '../ErrorMessages';
import EachOrder from './EachOrder';

function AllOrders() {

    const [orders, setOrders] = useState([]);
    const auth = useContext(AuthContext);

    const URL = 'http://localhost:8080/api/transaction';

    const getList = () => {
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
    }

    useEffect(getList, []);


    console.log(orders);

    return (
        <>
            <h2>All Orders</h2>
            <table>
                <thead>
                    <tr>
                        <th>Order Id</th>
                        <th>Date</th>
                        <th>User Id</th>
                        <th>List of Books</th>
                        <th>Status</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map(order => (
                        <EachOrder key={order.transactionId} order={order} getList={getList} auth={auth} />
                    ))
                    }
                </tbody>
            </table>
        </>
    )
}

export default AllOrders;