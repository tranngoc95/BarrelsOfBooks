import { useContext, useEffect, useState } from 'react';

import { Link } from 'react-router-dom';

function Cart() {

    const [items, setItems] = useState([]);
    const URL = 'http://localhost:8080/api/cart-item';

    useEffect(() => {

        const init = {
            headers: {
                'Authorization': 'Bearer'
            }
        }

        return fetch(URL + '/1', init)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Cart fetch failed.")
                }
                return response.json();
            })
            .then(data => setItems(data))
            .catch(error => console.log("Error", error));
    }, []);

    console.log(items);

    const handleChange = (item) => {
        console.log(item);
        
    }

    return (
        <>
            <div>
                <h2>My Shopping Cart</h2>
                <div>
                    {items.map(item => (
                        <div key={item.cartItemId}>
                            <h4>{item.book.title}</h4>
                            <div>Price: ${item.book.price}</div>
                            <div>
                                <label htmlFor="quantity">Quantity</label>
                                <select id="quantity" name="quantity" value={item.quantity} onChange={handleChange(item)}>
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
            </div>

        </>
    )

}

export default Cart;