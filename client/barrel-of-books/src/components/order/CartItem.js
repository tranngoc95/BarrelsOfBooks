import { useState } from "react";

import ErrorMessages from "../ErrorMessages";

function CartItem({ item, getList, auth }) {

    const [quantity, setQuantity] = useState(item.quantity);
    const [errorList, setErrorList] = useState([]);
    const [update, setUpdate] = useState(false);
    const [toggle, setToggle] = useState(item.quantity < 10)

    const URL = 'http://localhost:8080/api/cart-item';

    const handleChange = (event) => {
        setQuantity(event.target.value);
        if (event.target.id === "quantitySel" && event.target.value === "10") {
            setToggle(false);
        }
    }

    const handleUpdate = () => {
        setUpdate(true);
    }

    const updateQuantity = () => {
        setUpdate(false);
        setToggle(quantity < 10);

        if (item.quantity !== quantity) {
            const changedItem = { ...item };
            changedItem.quantity = quantity;

            const init = {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.user.token}`
                },
                body: JSON.stringify(changedItem)
            };

            fetch(URL + `/${item.cartItemId}`, init)
                .then(response => {
                    if (response.status === 204) {
                        return null;
                    } else if (response.status === 404) {
                        return [`Item with id ${item.cartItemId} does not exist.`];
                    } else if (response.status === 403) {
                        return ['You are not authorized to make changes to this record.'];
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
    }

    const removeItem = (item) => {
        const init = {
            method: "Delete",
            headers: {
                'Authorization': `Bearer ${auth.user.token}`
            }
        }

        fetch(URL + `/${item.cartItemId}`, init)
            .then(response => {
                if (response.status === 204) {
                    return null;
                } else if (response.status === 404) {
                    return [`Item with id ${item.cartItemId} does not exist.`];
                } else if (response.status === 403) {
                    return ['You are not authorized to make changes to this record.'];
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

    return (
        <div className="item">
            <ErrorMessages errorList={errorList} />
            <div className="ui six column grid">
            <div className="column"><img className="ui tiny image" src={`/${item.book.bookId}.jpg`} alt="" /></div>
                <div className="six wide column">
                    <h4>{item.book.title}</h4>
                    <div>Price: ${item.book.price}</div>
                    {item.book.quantity === 0 && (
                        <div className="red-text">
                            <div>Out Of Stock.</div>
                            <div>Please remove item before proceeding to checkout.</div>
                        </div>
                    )}
                    {item.book.quantity !== 0 && item.book.quantity < item.quantity
                        && (
                            <div className="red-text">
                                <div>In stock: {item.book.quantity}</div>
                                <div>Please adjust the item quantity to match our instock before proceeding to checkout.</div>
                            </div>
                        )}
                </div>
                <div className="right floated six wide column">
                    <label htmlFor="quantity">Quantity: </label>
                    {toggle && <select className="ui search dropdown" id="quantitySel" name="quantity" value={quantity} onChange={handleChange} disabled={!update}>
                        <option value={1} disabled={item.book.quantity < 1}>1</option>
                        <option value={2} disabled={item.book.quantity < 2}>2</option>
                        <option value={3} disabled={item.book.quantity < 3}>3</option>
                        <option value={4} disabled={item.book.quantity < 4}>4</option>
                        <option value={5} disabled={item.book.quantity < 5}>5</option>
                        <option value={6} disabled={item.book.quantity < 6}>6</option>
                        <option value={7} disabled={item.book.quantity < 7}>7</option>
                        <option value={8} disabled={item.book.quantity < 8}>8</option>
                        <option value={9} disabled={item.book.quantity < 9}>9</option>
                        <option value={10} disabled={item.book.quantity < 10}>10+</option>
                    </select>
                    }
                    {!toggle && <input id="quantityNum" name="quantity" value={quantity} type="number" onChange={handleChange} required disabled={!update} />
                    }
                    {!update && <button className="ui blue button" onClick={handleUpdate} disabled={item.book.quantity === 0}>Update Quantity</button>}
                    {update && <button className="ui green button" onClick={updateQuantity}>Save</button>}
                    <div><button className="ui button" onClick={() => removeItem(item)}>Remove</button></div>
                </div>
            </div>
        </div>
    )

}

export default CartItem;