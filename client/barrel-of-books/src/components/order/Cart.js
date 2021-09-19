import { useContext, useEffect, useState } from 'react';

import { Link, useHistory } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';

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

    const handleChange = (event, item) => {

        item.quantity = event.target.value;

        const init = {
            method: "PUT",
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ${auth.user.token}'
            },
            body: JSON.stringify(item)
        };

        fetch(URL + `/${item.cartItemId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Item with id ${item.cartItemId} does not exist.`];
                } else if (response.status === 400) {
                    return response.json();
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (!data) {
                    getList();
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log('Error:', error));
    }

    const removeItem = (item) => {
        const init = {
            method: "Delete",
            headers: {
                'Authorization': 'Bearer ${auth.user.token}'
            }
        }

        fetch(URL + `/${item.cartItemId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Item with id ${item.cartItemId} does not exist.`];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (!data) {
                    getList();
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }

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
                        <div key={item.cartItemId}>
                            <h4>{item.book.title}</h4>
                            <div>Price: ${item.book.price}</div>
                            <div>
                                <label htmlFor="quantity">Quantity</label>
                                <select id="quantity" name="quantity" value={item.quantity} onChange={(event) => handleChange(event, item)}>
                                    <option value="1">1</option>
                                    <option value="2">2</option>
                                    <option value="3">3</option>
                                    <option value="4">4</option>
                                    <option value="5">5</option>
                                    <option value="6">7</option>
                                    <option value="7">8</option>
                                    <option value="8">9</option>
                                    <option value="9">10</option>
                                </select>
                            </div>
                            <button onClick={() => removeItem(item)}>Remove</button>
                        </div>
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