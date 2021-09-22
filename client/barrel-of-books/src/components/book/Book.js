import { useContext, useEffect, useState } from 'react';

import { useHistory, useParams } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';
import AuthContext from '../../AuthContext';
import States from '../States';

function Book() {

    const [book, setBook] = useState(null);
    const [errorList, setErrorList] = useState([]);
    const [find, setFind] = useState(false);
    const [state, setState] = useState(null);
    const [stores, setStores] = useState(null);

    const URL = 'http://localhost:8080/api/book';
    const auth = useContext(AuthContext);
    const history = useHistory();

    const { id } = useParams();

    useEffect(() => {

        fetch(URL + `/${id}`)
            .then(response => {
                if (response.status === 200) {
                    return response.json();
                } else if (response.status === 404) {
                    return [`Book with id ${id} does not exist.`];
                }
                return Promise.reject("Something went wrong, sorry :(");
            })
            .then(data => {
                if (data.bookId) {
                    setBook(data);
                } else {
                    setErrorList(data);
                }
            })
            .catch(error => console.log("Error", error));
    }, [id]);

    const addToCart = () => {

        if (auth.user) {
            const item = {
                userId: auth.user.id,
                book,
                quantity: 1
            }

            const init = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${auth.user.token}`
                },
                body: JSON.stringify(item)
            };

            fetch('http://localhost:8080/api/cart-item', init)
                .then(response => {
                    if (response.status === 403) {
                        return ['You are not authorized to make changes to this record.'];
                    }
                    if (response.status !== 400 && response.status !== 201) {
                        return Promise.reject("Something went wrong. :(")
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.cartItemId) {
                        history.push('/cart');
                    } else {
                        setErrorList(data);
                    }
                })
                .catch(error => console.log('Error:', error));
        }
    }

    const findStores = () => {
        fetch(`http://localhost:8080/api/store-book/${id}/${state}`)
            .then(response => {
                if (response.status !== 200) {
                    return Promise.reject("Stores fetch failed.")
                }
                return response.json();
            })
            .then(data => setStores(data))
            .catch(error => console.log("Error", error));
    }

    const handleFind = () => {
        setFind(true);
    }

    return (
        <div>
            <ErrorMessages errorList={errorList} />
            {book &&
                <>
                    <h2>{book.title}</h2>
                    <div>by {book.author}</div>
                    <div>Price: {book.price}</div>
                    <div>{book.description}</div>
                    <div>{book.price}</div>
                    {auth.user && <button type="button" onClick={addToCart}>Add to cart</button>}
                    {!find && <button onClick={handleFind}>Find Available Stores</button>}
                    <div>
                        {find &&
                            <>
                                <label htmlFor="state">State:</label>
                                <select name="state" value={state} onChange={(event) => setState(event.target.value)}>
                                    {States.map(each => (
                                        <option key={each.abbr} value={each.abbr} >{each.name}</option>
                                    ))}
                                </select>
                                <button onClick={findStores}>Find</button>
                            </>
                        }
                    </div>
                    {stores !== null &&
                        <div>
                            {stores.length > 0 ?
                                <div>
                                    {
                                        stores.map(each => (
                                            <>
                                            <h5>{each.store.city} bookstore</h5>
                                            <div>{each.store.address}, {each.store.city}, {each.store.state}, {each.store.postalCode}</div>
                                            {each.quantity > 0 ? <div>In stock</div> : <div>Out of Stock</div>}
                                            </>
                                        ))
                                    }
                                </div>
                                :
                                <div>There is no store available in this state.</div>}
                        </div>

                    }
                </>
            }
        </div>
    )
}

export default Book;