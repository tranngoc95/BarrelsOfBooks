import { useContext, useEffect, useState } from 'react';

import AuthContext from '../../AuthContext';
import EachOrder from './EachOrder';
import ErrorMessages from '../ErrorMessages';

function AllOrders() {

    const [orders, setOrders] = useState([]);
    const [errorList, setErrorList] = useState([]);

    const URL = 'http://localhost:8080/api/transaction';
    const auth = useContext(AuthContext);

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

    useEffect(getList, [auth.user.token]);

    return (
        <div className="ui container">
            <h2>All Orders</h2>
            <ErrorMessages errorList={errorList}/>
            <table className="ui selectable celled table">
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
                        <EachOrder key={order.transactionId} order={order} getList={getList} auth={auth} setErrorList={setErrorList}/>
                    ))
                    }
                </tbody>
            </table>
        </div>
    )
}

export default AllOrders;