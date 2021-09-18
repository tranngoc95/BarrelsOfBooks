import { useContext, useEffect, useState } from 'react';

import { Link } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';

function Cart() {

    const [items, setItems] = useState([]);
    const [errorList, setErrorList] = useState([]);
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

    console.log(items);

    const handleChange = (event, item) => {

        item.quantity=event.target.value;

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

    const checkout = () => {

    }

    return (
        <>
            <div>
                <h2>My Shopping Cart</h2>
                <ErrorMessages errorList={errorList}/>
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
                            <button>Remove</button>
                        </div>
                    ))}
                </div>
            </div>
            <div>
                <h5>Subtotal ({items.length} items): </h5>
                <button onClick={checkout}>CHECKOUT</button>
            </div>

        </>
    )

}

export default Cart;