import { useContext, useEffect, useState } from 'react';

import { useHistory, useParams } from 'react-router-dom';

import ErrorMessages from './ErrorMessages';
import AuthContext from '../AuthContext';

function Book() {

    const [book, setBook] = useState(null);
    const [errorList, setErrorList] = useState([]);

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

            console.log(item)

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
                    console.log(data);
                    if (data.cartItemId) {
                        history.push('/cart');
                    } else {
                        setErrorList(data);
                    }
                })
                .catch(error => console.log('Error:', error));
        }
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
                </>
            }
        </div>
    )
}

export default Book;