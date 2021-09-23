import { useContext, useEffect, useState } from 'react';

import { useHistory, useParams } from 'react-router-dom';

import ErrorMessages from '../ErrorMessages';
import AuthContext from '../../AuthContext';
import States from '../States';

function Book() {

    const [book, setBook] = useState(null);
    const [errorList, setErrorList] = useState([]);
    const [find, setFind] = useState(false);
    const [state, setState] = useState("");
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
        if (state === "") {
            return;
        }
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
        <div className="home-page">
        <div className="ui container book-container">
            <ErrorMessages errorList={errorList} />
            {book &&
                <>
                <div>
                    {/* <img src={ require('../../public/1.jpg') } /> */}
                </div>
                    <div className="ui container book-info">
                        <h2>{book.title}</h2>
                        <div>by {book.author}</div>
                        <hr/>
                        <div>Price: {book.price}</div>
                        <div className="ui container book-overview">{book.description}</div>
                        <div>Publisher: {book.publisher}</div>
                        <div>Language: {book.language}</div>
                        <div>Page Count: {book.pages}</div>
                        <div>Age Range: {book.ageRange}</div>
                        <div>Dimensions: {book.dimensions}</div>
                        <div>ISBN-13: {book.isbn13}</div>
                        {auth.user && <button className="ui primary button" type="button" onClick={addToCart}>Add to cart</button>}
                        {!find && <button className="ui button" onClick={handleFind}>Find Available Stores</button>}
                    </div>
                    <div>
                        {find &&
                            <div className="book-info">
                                <label htmlFor="state">State: </label>
                                <select name="state" value={state} onChange={(event) => setState(event.target.value)}>
                                    <option value="">Select State</option>
                                    {States.map(each => (
                                        <option key={each.abbr} value={each.abbr} >{each.name}</option>
                                    ))}
                                </select>
                                <button className="ui mini button" onClick={findStores}>Find</button>
                            </div>
                        }
                    </div>
                    {stores !== null &&
                        <div>
                            {stores.length > 0 ?
                                <div className="book-info">
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
                                <div className="book-info">There is no store available in this state.</div>}
                        </div>

                    }
                </>
            }
        </div>
        </div>
    )
}

export default Book;