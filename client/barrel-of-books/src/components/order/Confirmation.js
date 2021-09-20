import { useContext, useEffect, useState } from 'react';

import { Link, useParams } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';

function Confirmation() {

    const { id } = useParams();
    const [order, setOrder] = useState(null);
    const [errorList, setErrorList] = useState([]);
    const URL = 'http://localhost:8080/api/transaction';

    useEffect(() => {
        const init = {
            headers: {
                'Authorization': 'Bearer ${auth.user.token}'
            }
        }

        fetch(URL + `/${id}`, init)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    return [`Order with id ${id} does not exist.`];
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
    }, [id]);

    return (
        <>
            <ErrorMessages errorList={errorList} />
            {order && (<div>
                <h3>Successfull Checkout!</h3>
                <div>Thank you for your order!</div>
                <br />
                <div>What you ordered: </div>
                <div>
                    <div>Order Id: {order.transactionId}</div>
                    {order.books.map(item => (
                        <div>
                            <h6>{item.book.title}</h6>
                        </div>
                    ))}
                    {order.books.employeeDiscount &&
                        <div>Employee Discount: 30%</div>}
                    <div>Total: ${order.total}</div>
                </div>


            </div>
            )}
        </>
    )

}

export default Confirmation;