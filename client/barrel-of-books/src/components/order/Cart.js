import { useContext, useEffect, useState } from 'react';

import { Link, useHistory } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';
import CartItem from './CartItem';

function Cart() {

    const [items, setItems] = useState([]);
    const [errorList, setErrorList] = useState([]);

    const history = useHistory()
    const URL = 'http://localhost:8080/api/cart-item';

    const getList = () => {

        const init = {
            headers: {
                'Authorization': 'Bearer ${auth.user.token}'
            }
        }

        fetch(URL + '/user/1', init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Cart fetch failed.")
                }
                return response.json();
            })
            .then(data => setItems(data))
            .catch(error => console.log("Error", error));
    }

    useEffect(getList, []);    

    const checkout = () => {

        const order = {
            userId: "1",
            books: items
        }

        console.log(order);

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ${auth.user.token}'
            },
            body: JSON.stringify(order)
        };

        fetch("http://localhost:8080/api/transaction", init)
            .then(response => {
                if (response.status !== 400 && response.status !== 201) {
                    return Promise.reject("Something went wrong. :(")
                }
                return response.json();
            })
            .then(data => {
                if (data.transactionId) {
                    history.push(`/orders/confirmation/${data.transactionId}`);
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log('Error:', error));
    }

    return (
        <>
            <div>
                <h2>My Shopping Cart</h2>
                <ErrorMessages errorList={errorList} />
                <div>
                    {items.map(item => (
                        <CartItem item={item} getList={getList}/>
                    ))}
                </div>
            </div>
            {items.length > 0 &&
                (<div>
                    <h5>Subtotal ({items.length} items): </h5>
                    <button onClick={checkout}>CHECKOUT</button>
                </div>
                )}
        </>
    )

}

export default Cart;