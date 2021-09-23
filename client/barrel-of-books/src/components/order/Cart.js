import { useContext, useEffect, useState } from 'react';

import { useHistory } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';
import CartItem from './CartItem';
import AuthContext from '../../AuthContext';

function Cart() {

    const emptyCart = {
        books: [],
        subtotal: 0,
        itemNum: 0
    }

    const [cart, setCart] = useState(emptyCart);
    const [errorList, setErrorList] = useState([]);

    const URL = 'http://localhost:8080/api/cart-item';
    const auth = useContext(AuthContext);
    const history = useHistory();

    const getList = () => {

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
            .then(data => {
                setCart(data);
            })
            .catch(error => console.log("Error", error));
    }

    useEffect(getList, [auth.user]);

    const checkOut = () => {

        if (cart.books.some(one => one.book.quantity < one.quantity)) {
            setErrorList(["Please adjust item quantity to match with our in stock quantity before proceeding to checkout."]);
            return;
        }

        const order = {
            userId: auth.user.id,
            books: cart.books,
            employeeDiscount: auth.user.hasRole('MANAGER')
        }

        const init = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${auth.user.token}`
            },
            body: JSON.stringify(order)
        };

        fetch("http://localhost:8080/api/transaction", init)
            .then(response => {
                if (response.status !== 400 && response.status !== 201 && response.status !== 403) {
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
        <div className="ui container grid">
            <div className="twelve wide column">
                <h2>My Shopping Cart</h2>
                <ErrorMessages errorList={errorList} />
                <div className="ui middle aligned celled divided list">
                    {cart.books.map(item => (
                        <CartItem key={item.cartItemId} item={item} getList={getList} auth={auth} />
                    ))}
                </div>
            </div>
            {cart.itemNum > 0 &&
                (<div className="four wide column">
                    <h5>Subtotal ({cart.itemNum} items): ${cart.subtotal}</h5>
                    <button className="ui green button" onClick={checkOut}>CHECKOUT</button>
                </div>
                )}
        </div>
    )

}

export default Cart;