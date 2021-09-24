import { useContext, useEffect, useState } from 'react';

import { useParams } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';
import AuthContext from '../../AuthContext';

function Confirmation() {

    const { id } = useParams();
    const [order, setOrder] = useState(null);
    const [errorList, setErrorList] = useState([]);

    const URL = 'http://localhost:8080/api/transaction';
    const auth = useContext(AuthContext);

    useEffect(() => {
        const init = {
            headers: {
                'Authorization': `Bearer ${auth.user.token}`
            }
        }

        fetch(URL + `/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    return [`Order with id ${id} does not exist.`];
                } else if (response.status === 403) {
                    return ['You are not authorized to access this record.'];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (data.transactionId) {
                    setOrder(data);
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }, [id, auth.user.token]);

    return (
        <div className="home-page">
            <div className="ui container white-bg">
            <ErrorMessages errorList={errorList} />
            {order && (<div>
                <h3>Successfull Checkout!</h3>
                <div>Thank you for your order!</div>
                <br />
                <div>What you ordered: </div>
                <div>
                    <div>Order Id: {order.transactionId}</div>
                    {order.books.map(item => (
                        <div key={item.cartItemId}>
                            <div><b>{item.book.title}</b></div>
                            <div>by {item.book.author}</div>
                            <div>Quantity: {item.quantity}</div>
                        </div>
                    ))}
                    <hr/>
                    {order.employeeDiscount &&
                        <div>Employee Discount: 30%</div>}
                    <div>Total: ${order.total}</div>
                </div>


            </div>
            )}
            </div>
        </div>
    )

}

export default Confirmation;